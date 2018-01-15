package local.kilg.fw.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.weather_widget_configure.*
import local.kilg.fw.R

/**
 * The configuration screen for the [WeatherWidget] AppWidget.
 */
class WeatherWidgetConfigureActivity : Activity() {
    internal var mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    internal var mOnClickListener: View.OnClickListener = View.OnClickListener {
        val context = this@WeatherWidgetConfigureActivity

        val countryCityValuesArray = resources.getStringArray(R.array.pref_city_list_values)
        val selectedCityCountryValuesString = countryCityValuesArray[spn_cities.selectedItemPosition]

        saveCountryCityPref(context, mAppWidgetId, selectedCityCountryValuesString)

        val useBackground: Boolean = chbx_use_background.isChecked
        saveUseBackgroundPref(context, mAppWidgetId, useBackground)


        val textColor: Int = when (rbg_text_color.checkedRadioButtonId) {
            rb_black.id -> context.resources.getColor(R.color.widget_text_color_black)
            else -> context.resources.getColor(R.color.widget_text_color_white)
            // context.resources.getColor(R.color.widget_text_color_white, context.theme) API > 23
        }

        saveTextColorPref(context, mAppWidgetId, textColor)


        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(context)
        WeatherWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(Activity.RESULT_CANCELED)

        setContentView(R.layout.weather_widget_configure)
        findViewById<View>(R.id.add_button).setOnClickListener(mOnClickListener)

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
    }

    companion object {

        private val PREFS_NAME = "local.kilg.fw.widget.WeatherWidget"
        private val PREF_PREFIX_KEY = "appwidget_"


        internal fun saveCountryCityPref(context: Context, appWidgetId: Int, selectedCountryCityValuesString: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putString(PREF_PREFIX_KEY + "_country_city_" + appWidgetId, selectedCountryCityValuesString)
            prefs.apply()
        }


        internal fun saveUseBackgroundPref(context: Context, appWidgetId: Int, useBackground: Boolean) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putBoolean(PREF_PREFIX_KEY + "_use_background_" + appWidgetId, useBackground)
            prefs.apply()
        }


        internal fun saveTextColorPref(context: Context, appWidgetId: Int, textColor: Int) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
            prefs.putInt(PREF_PREFIX_KEY + "_text_color_" + appWidgetId, textColor)
            prefs.apply()
        }

        internal fun loadCountryCityPref(context: Context, appWidgetId: Int): String =
                context.getSharedPreferences(PREFS_NAME, 0)
                        .getString(PREF_PREFIX_KEY + "_country_city_" + appWidgetId,
                                context.resources.getStringArray(R.array.pref_city_list_titles)[0])


        internal fun loadUseBackgroundPref(context: Context, appWidgetId: Int): Boolean =
                context.getSharedPreferences(PREFS_NAME, 0).
                        getBoolean(PREF_PREFIX_KEY + "_use_background_" + appWidgetId,
                                true)


        internal fun loadTextColorPref(context: Context, appWidgetId: Int): Int =
                context.getSharedPreferences(PREFS_NAME, 0)
                        .getInt(PREF_PREFIX_KEY + "_text_color_" + appWidgetId,
                                context.resources.
                                        getColor(R.color.widget_text_color_white)
                        // getColor(R.color.widget_text_color_white, context.theme)  // API > 23
                        )


        internal fun deleteCountryCityPref(context: Context, appWidgetId: Int)=
                context.getSharedPreferences(PREFS_NAME, 0)
                        .edit()
                        .remove(PREF_PREFIX_KEY + "_country_city_" + appWidgetId )
                        .apply()


        internal fun deleteUseBackgroundPref(context: Context, appWidgetId: Int) =
                context.getSharedPreferences(PREFS_NAME, 0)
                        .edit()
                        .remove(PREF_PREFIX_KEY + "_use_background_" + appWidgetId)
                        .apply()


        internal fun deleteTextColorPref(context: Context, appWidgetId: Int) =
                context.getSharedPreferences(PREFS_NAME, 0)
                        .edit()
                        .remove(PREF_PREFIX_KEY + "_text_color_" + appWidgetId)
                        .apply()
    }
}

