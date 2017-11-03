package local.kilg.fw

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import local.kilg.fw.databinding.ActivityOneDayBinding
import local.kilg.fw.provider.FW_Contract


class OneDayActivity : AppCompatActivity() {

    companion object {
        private val DAY_DATE = "date"
        fun newIntent(context: Context, timeStamp: Long): Intent {
            val intent = Intent(context, OneDayActivity::class.java)
            intent.putExtra(DAY_DATE, timeStamp)
            return intent
        }

        private val projection = arrayOf(
                FW_Contract.Day._ID,
                FW_Contract.Day.COLUMN_NAME_DATE,
                FW_Contract.Day.COLUMN_NAME_TEMP_HIGH,
                FW_Contract.Day.COLUMN_NAME_TEMP_LOW
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
                FW_Contract.Day.CONTENT_URI, date.toString()),
                projection,
                null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()

            mCurrentDay = Day(
                    cursor.getLong(cursor.getColumnIndex(FW_Contract.Day.COLUMN_NAME_DATE)),
                    cursor.getDouble(cursor.getColumnIndex(FW_Contract.Day.COLUMN_NAME_TEMP_HIGH)),
                    cursor.getDouble(cursor.getColumnIndex(FW_Contract.Day.COLUMN_NAME_TEMP_LOW))
            )
        }

        cursor?.close()

        val binding : ActivityOneDayBinding = DataBindingUtil.setContentView(this, R.layout.activity_one_day)
        binding.day = mCurrentDay

    }



}












