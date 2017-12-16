package local.kilg.fw

import android.content.Context
import android.database.Cursor
import android.database.DataSetObserver
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import local.kilg.fw.databinding.Forecast10ItemBinding
import local.kilg.fw.provider.ForecastWeatherContract.Forecast

/**
 *  kilg on 29.10.17.
 */
class RecycleCursorAdapter(val context: Context, var cursor: Cursor?, val listener: (Day) -> Unit) : RecyclerView.Adapter<RecycleCursorAdapter.VH>() {

    private var mDataValid: Boolean = false
    private var mRowIdColumn: Int = 0
    private val mDataSetObserver: DataSetObserver?

    init {
        mDataValid = cursor != null
        mRowIdColumn = if (mDataValid) this.cursor!!.getColumnIndex("_id") else -1
        mDataSetObserver = NotifyingDataSetObserver()
        if (this.cursor != null) {
            this.cursor!!.registerDataSetObserver(mDataSetObserver)
        }
    }
    override fun getItemCount(): Int {
        if (cursor != null) {
            return cursor!!.count
        }
        return 0
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VH {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val binding = Forecast10ItemBinding.inflate(inflater, parent, false)

        return VH(binding, listener)
    }


    override fun onBindViewHolder(holder: VH?, position: Int) {
        if (cursor != null && holder != null) {
            cursor!!.moveToPosition(position)
            holder.bind(cursor!!)
        }
    }

    data class VH(val binding: Forecast10ItemBinding, val listener: (Day) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cursor: Cursor) {
            val item = Day(
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.DATE)),
                    cursor.getString(cursor.getColumnIndex(Forecast.COLUMN.ICON)),
                    cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_HIGH)),
                    cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_LOW)),
                    { listener(it) }
            )

            binding.day = item
            binding.executePendingBindings()
        }
    }

    fun changeCursor(cursor: Cursor) {
        val old = swapCursor(cursor)
        old?.close()
    }

    fun swapCursor(newCursor: Cursor?): Cursor? {
        if (newCursor === cursor) {
            return null
        }
        val oldCursor = cursor
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver)
        }
        cursor = newCursor
        if (cursor != null) {
            if (mDataSetObserver != null) {
                cursor!!.registerDataSetObserver(mDataSetObserver)
            }
            mRowIdColumn = newCursor!!.getColumnIndexOrThrow("_id")
            mDataValid = true
            notifyDataSetChanged()
        } else {
            mRowIdColumn = -1
            mDataValid = false
            notifyDataSetChanged()
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor
    }

    private inner class NotifyingDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            super.onChanged()
            mDataValid = true
            notifyDataSetChanged()
        }

        override fun onInvalidated() {
            super.onInvalidated()
            mDataValid = false
            notifyDataSetChanged()
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }
}