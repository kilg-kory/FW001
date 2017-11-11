package local.kilg.fw

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import local.kilg.fw.databinding.ActivityOneDayBinding
import local.kilg.fw.provider.ForecastWeatherContract.Forecast

class OneDayActivity : AppCompatActivity() {

    companion object {
        private val DAY_DATE = "date"
        fun newIntent(context: Context, timeStamp: Long): Intent {
            val intent = Intent(context, OneDayActivity::class.java)
            intent.putExtra(DAY_DATE, timeStamp)
            return intent
        }

        //select fields
        private val projection = arrayOf(
                Forecast.COLUMN._ID,
                Forecast.COLUMN.ICON,
                Forecast.COLUMN.DATE,
                Forecast.COLUMN.TEMP_HIGH,
                Forecast.COLUMN.TEMP_LOW
        )


    }


    private lateinit var mCurrentDay: Day

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_day)


        if (!intent.hasExtra(DAY_DATE))
            throw IllegalStateException("field $DAY_DATE is missing for intent")

        val date: Long = intent.getLongExtra(DAY_DATE, 0L)
        val cursor: Cursor? = contentResolver.query(Uri.withAppendedPath(
                Forecast.CONTENT_URI, date.toString()),
                projection,
                null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()

            mCurrentDay = Day(
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.DATE)),
                    cursor.getString(cursor.getColumnIndex(Forecast.COLUMN.ICON)),
                    cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_HIGH)),
                    cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_LOW))
            ){}
        }

        cursor?.close()

        val binding : ActivityOneDayBinding = DataBindingUtil.setContentView(this, R.layout.activity_one_day)
        binding.day = mCurrentDay

    }



}










