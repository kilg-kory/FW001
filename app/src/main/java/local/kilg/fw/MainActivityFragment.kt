package local.kilg.fw

import android.app.ActivityOptions
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import local.kilg.fw.provider.ForecastWeatherContract.Forecast

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
            Forecast.COLUMN.TEMP_HIGH,
            Forecast.COLUMN.TEMP_LOW
    )

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mAdapter.swapCursor(null)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        //loader observe provider by default
        return CursorLoader(context!!,
                Forecast.CONTENT_URI,
                projection,
                null,
                null,
                null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //Create Cursor Adapter and binding data to layout:

        // redefine abstract class in place.
        mAdapter = RecycleCursorAdapter(this.context!!, null) {
                        //on click action - open OneDayActivity with timestamp clicked day
                        val intent:Intent = OneDayActivity.newIntent(context!!, it.getEpoch())
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
                        //startActivity(intent)
                    }

        val layoutManager = LinearLayoutManager(context)
        rv_forecast.layoutManager = layoutManager
        rv_forecast.adapter = mAdapter


        loaderManager.initLoader(0, null, this)
    }
}
