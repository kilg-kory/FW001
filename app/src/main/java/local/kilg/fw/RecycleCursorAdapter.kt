package local.kilg.fw

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.forecast10_item.view.*
import local.kilg.fw.provider.ForecastWeatherContract.Forecast

/**
 * Created by kilg on 29.10.17.
 */
class RecycleCursorAdapter(val context: Context, val cursor: Cursor?, listener: (Long) -> Unit) : RecyclerView.Adapter<RecycleCursorAdapter.VH>() {
    private val mCursorAdapter: CursorAdapter = object:CursorAdapter(context, cursor, 0){

       override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View =
               LayoutInflater.from(context).inflate(R.layout.forecast10_item, parent, false)


        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            if(view != null && cursor != null) {


                val dayDate = cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.DATE))


                view.tv_date.text = dayDate.toString()
                view.tv_high.text = cursor.getDouble(cursor.getColumnIndex(Forecast.COLUMN.TEMP_HIGH)).toString()
                view.tv_low.text  = cursor.getDouble(cursor.getColumnIndex(Forecast.COLUMN.TEMP_LOW)).toString()
                view.setOnClickListener{listener(dayDate)}
            }
        }
    }

    override fun getItemCount(): Int {
        if(mCursorAdapter.cursor != null){
            return mCursorAdapter.cursor.count
        }
        //kotlin can do it smarty?
        return  0
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
       return VH(mCursorAdapter.newView(context, mCursorAdapter.cursor, parent))
    }


    override fun onBindViewHolder(holder: VH?, position: Int) {
        mCursorAdapter.cursor.moveToPosition(position)
        mCursorAdapter.bindView(holder!!.i, context, mCursorAdapter.cursor)
    }

    data class VH( val i: View?) : RecyclerView.ViewHolder(i)

    fun swapCursor(data: Cursor?) {
        mCursorAdapter.swapCursor(data)
        Log.d("SYNC", "Swap cursor")
        notifyDataSetChanged()
    }
}