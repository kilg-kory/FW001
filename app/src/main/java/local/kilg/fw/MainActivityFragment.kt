package local.kilg.fw

import android.database.Cursor
import android.net.Uri
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.widget.SimpleCursorAdapter
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import local.kilg.fw.provider.FW_Contract

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    lateinit var mAdapter:RecycleCursorAdapter

    private val projection = arrayOf(
            FW_Contract.Day._ID,
            FW_Contract.Day.COLUMN_NAME_DATE,
            FW_Contract.Day.COLUMN_NAME_TEMP_HIGH,
            FW_Contract.Day.COLUMN_NAME_TEMP_LOW
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
                Uri.withAppendedPath(FW_Contract.BASE_CONTENT_URI, FW_Contract.PATH_DAYS),
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

        mAdapter = RecycleCursorAdapter(activity, null){
            val intent = OneDayActivity.newIntent(activity, it)
            startActivity(intent)
        }

        rv_forecast10_list.setHasFixedSize(true)
        rv_forecast10_list.layoutManager = LinearLayoutManager(activity)
        rv_forecast10_list.adapter = mAdapter

        loaderManager.initLoader(0, null, this)
    }
}
