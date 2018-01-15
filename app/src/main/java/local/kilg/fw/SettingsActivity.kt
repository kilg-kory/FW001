package local.kilg.fw

import android.accounts.Account
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.preference.*
import android.util.Log
import android.view.MenuItem

/**
 * A [PreferenceActivity] that presents a set of application settings. On
 */
class SettingsActivity : PreferenceActivity() {
    companion object {
        private val LOG_TAG = "SETTINGS_ACTIVITY"

        private val ACCOUNT_NAME = "sync"
        private val ACCOUNT_TYPE = "local.kilg.fw"
        private val SYNC_INTERVAL = 60L * 60L * 12L  //in seconds
        private val account = Account(ACCOUNT_NAME, ACCOUNT_TYPE)

        private fun enablePeriodicSync(context: Context) {
            createSyncAccount(context)
            ContentResolver.setIsSyncable(account, ACCOUNT_TYPE, 1)
            ContentResolver.setSyncAutomatically(account, ACCOUNT_TYPE, true)
            ContentResolver.addPeriodicSync(account, ACCOUNT_TYPE, Bundle.EMPTY, SYNC_INTERVAL)
        }

        private fun disablePeriodicSync(context: Context) {
            ContentResolver.cancelSync(account, ACCOUNT_TYPE)
            ContentResolver.removePeriodicSync(account, ACCOUNT_TYPE, Bundle.EMPTY)
            removeSyncAccount(context)
        }


        private fun createSyncAccount(context: Context) {
            val am: AccountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
            am.addAccountExplicitly(account, null, null)
        }

        private fun removeSyncAccount(context: Context) {
            val am: AccountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
            am.removeAccountExplicitly(account)
        }

        private val sOnPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, value ->
            if (preference is ListPreference) {
                val stringValue = value as String
                val index = preference.findIndexOfValue(stringValue)

                preference.setSummary(
                        if (index >= 0)
                            preference.entries[index]
                        else
                            null)


//                val uri = Uri.parse("${ForecastWeatherContract.Forecast.CONTENT_URI}/${preference.entryValues[index]}")
//                preference.context.contentResolver.notifyChange(uri, null)

            } else if (preference is SwitchPreference) {
                if (value as Boolean) {
                    enablePeriodicSync(preference.context)
                    preference.setSummary("Disable auto sync")
                } else {
                    disablePeriodicSync(preference.context)
                    preference.setSummary("Enable auto sync")
                }
            }
            true
        }

        internal fun bindPreferenceSummaryToValue(preference: Preference) {
            //add
            preference.onPreferenceChangeListener = sOnPreferenceChangeListener

            //triggered manually
            if (preference is SwitchPreference) {
                sOnPreferenceChangeListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getBoolean(preference.key, false))

            } else {
                sOnPreferenceChangeListener.onPreferenceChange(preference,
                        PreferenceManager
                                .getDefaultSharedPreferences(preference.context)
                                .getString(preference.key, ""))
            }

        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        fragmentManager
                .beginTransaction()
                .replace(android.R.id.content, GeneralPreferenceFragment())
                .commit()


    }



      class GeneralPreferenceFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.pref_general)
            setHasOptionsMenu(true)


            bindPreferenceSummaryToValue(findPreference("sync_switch"))
            bindPreferenceSummaryToValue(findPreference("city_list"))
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {
                activity.onBackPressed()
                return true
            }
            return super.onOptionsItemSelected(item)
        }
    }

}
