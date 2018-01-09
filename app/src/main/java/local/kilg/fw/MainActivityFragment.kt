package local.kilg.fw

import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
import org.jetbrains.anko.defaultSharedPreferences

/**
 * Include in xml layout
 * Show listview with 10 day forecast, loading from database. Use Loader to resolver, databinding.
 * Open OneDayActivity by onclick on item with forecast clicked day
 */
class MainActivityFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var mAdapter: RecycleCursorAdapter


    //select fields
    private val projection = arrayOf(
            Forecast.COLUMN._ID,
            Forecast.COLUMN.ICON,
            Forecast.COLUMN.DATE,
            Forecast.COLUMN.COUNTRY_CITY,
            Forecast.COLUMN.TEMP_HIGH,
            Forecast.COLUMN.TEMP_LOW
    )


    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mAdapter.swapCursor(null)
    }


    //restart loader by id in on resume method for show changes from settings.
    // just until I know how update it from settings
    private var LOADER_ID: Int = -1
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        LOADER_ID = id

        val countryCityString = context!!.defaultSharedPreferences.getString(
                resources.getString(R.string.city_list_key),
                resources.getStringArray(R.array.pref_city_list_values)[0])!!

        val uri = Uri.parse("${Forecast.CONTENT_URI}/$countryCityString")


        return CursorLoader(context!!,
                uri,
                projection,
                null,
                null,
                null)
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(LOADER_ID, null, this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAdapter = RecycleCursorAdapter(this.context!!, null) {
            val intent: Intent = OneDayActivity.newIntent(context!!, it.getEpoch())
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
        }

        val layoutManager = LinearLayoutManager(context)
        rv_forecast.layoutManager = layoutManager
        rv_forecast.adapter = mAdapter

        loaderManager.initLoader(0, null, this)
    }
}
