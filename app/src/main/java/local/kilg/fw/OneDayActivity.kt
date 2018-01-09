package local.kilg.fw

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.util.Log
import android.view.MenuItem
import local.kilg.fw.databinding.ActivityOneDayBinding
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
import org.jetbrains.anko.defaultSharedPreferences

class OneDayActivity : AppCompatActivity() {

    companion object {
        private val FORECAST_DATE = "date"
        fun newIntent(context: Context, epoch: Long): Intent {
            val intent = Intent(context, OneDayActivity::class.java)
            intent.putExtra(FORECAST_DATE, epoch)
            return intent
        }

        //select fields
        private val projection = arrayOf(
                Forecast.COLUMN._ID,
                Forecast.COLUMN.ICON,
                Forecast.COLUMN.DATE,
                Forecast.COLUMN.TEMP_HIGH,
                Forecast.COLUMN.TEMP_LOW,
                Forecast.COLUMN.MOONSET,
                Forecast.COLUMN.MOONRISE,
                Forecast.COLUMN.SUNSET,
                Forecast.COLUMN.SUNRISE
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            return when (item.itemId) {
                android.R.id.home -> {
//                    NavUtils.navigateUpFromSameTask(this)
                    onBackPressed()
                    true
                }
                else -> return super.onOptionsItemSelected(item)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var mCurrentDay: Day

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_day)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!intent.hasExtra(FORECAST_DATE)) {
            throw IllegalStateException("field $FORECAST_DATE is missing for intent")
        }

        val date: Long = intent.getLongExtra(FORECAST_DATE, 0L)


        val countryCityString = defaultSharedPreferences.getString(
                resources.getString(R.string.city_list_key),
                resources.getStringArray(R.array.pref_city_list_values)[0])

        val queryUri = Uri.parse("${Forecast.CONTENT_URI}/$countryCityString/${(date / 1000L)}")
        Log.d("QUERYURISTRING", "query uri for one entry - $queryUri")

        val cursor: Cursor? = contentResolver.query(
                queryUri,
                projection,
                null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()

            mCurrentDay = Day(
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.DATE)),
                    cursor.getString(cursor.getColumnIndex(Forecast.COLUMN.ICON)),
                    cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_HIGH)),
                    cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_LOW)),
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.SUNRISE)),
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.SUNSET)),
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.MOONRISE)),
                    cursor.getLong(cursor.getColumnIndex(Forecast.COLUMN.MOONSET))

            ) {}

            val binding: ActivityOneDayBinding = DataBindingUtil.setContentView(this, R.layout.activity_one_day)!!
            binding.day = mCurrentDay
        }

        cursor?.close()


    }


}










