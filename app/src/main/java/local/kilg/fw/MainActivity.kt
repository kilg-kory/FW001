package local.kilg.fw

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private val ACCOUNT_NAME = "sync"
    private val ACCOUNT_TYPE = "local.kilg.fw"
    private val SYNC_INTERVAL = 60L * 60L * 12L  //in seconds

    private val account = Account(ACCOUNT_NAME, ACCOUNT_TYPE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            //TODO if data count = 0 > visible, exists data or on click -> gone
            fab.visibility = View.GONE
            demandSync()
        }

        createSyncAccount(this)

        ContentResolver.setIsSyncable(account, ACCOUNT_TYPE, 1)
        ContentResolver.setSyncAutomatically(account, ACCOUNT_TYPE, true)
        ContentResolver.addPeriodicSync(account, ACCOUNT_TYPE, Bundle.EMPTY, SYNC_INTERVAL)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createSyncAccount(context: Context) {
        val am: AccountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager

        if (am.addAccountExplicitly(account, null, null)) {
            Log.d("SYNC", "Acc ok")
        } else {
            Log.d("SYNC", "Not add acc")
        }


    }


    private fun demandSync() {
        val bundle = Bundle()
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
        ContentResolver.requestSync(account, ACCOUNT_TYPE, bundle)
    }

}