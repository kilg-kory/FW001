package local.kilg.fw

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.forecast10_item.*
import kotlinx.android.synthetic.main.fragment_main.*
import local.kilg.fw.databinding.Forecast10ItemBinding
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
/**
 * Include in xml layout
 * Show listview with 10 day forecast, loading from database. Use Loader to resolver, databinding.
 * Open OneDayActivity by onclick on item with forecast clicked day
 */
class MainActivityFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var mAdapter: CursorAdapter


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
        return CursorLoader(activity,
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
        mAdapter = object : CursorAdapter(activity, null, false) {
            override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
                //empty Day for new View
                val databinding: Forecast10ItemBinding = Forecast10ItemBinding.inflate(LayoutInflater.from(activity), parent, false)
                databinding.day = Day()

                val view = databinding.root
                //hack from Google. Put databinding in tag for store
                view.tag = databinding
                return view
            }


            override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
                if (view != null && cursor != null) {


                    val item = Day(
                            cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.DATE)),
                            cursor.getString(cursor.getColumnIndex(Forecast.COLUMN.ICON)),
                            cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_HIGH)),
                            cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_LOW))
                    ) {
                        //on click action - open OneDayActivity with timestamp clicked day
                        val intent:Intent = OneDayActivity.newIntent(activity, it.getTimestamp())
                        startActivity(intent)
                    }

                    //export databind from tag and binding Day
                    val databinding = view.tag as Forecast10ItemBinding
                    databinding.day = item

                }
            }

        }
        lv_forecast.adapter = mAdapter


        loaderManager.initLoader(0, null, this)
    }
}
