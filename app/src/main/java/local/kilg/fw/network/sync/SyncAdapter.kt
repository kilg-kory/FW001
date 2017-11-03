package local.kilg.fw.network.sync

import android.accounts.Account
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import local.kilg.fw.network.rest.WeatherApi
import local.kilg.fw.network.rest.pojo.WeatherResponse
import local.kilg.fw.provider.FW_Contract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by kilg on 17.10.17/.
 */
class SyncAdapter : AbstractThreadedSyncAdapter {
    constructor(context: Context, autoInitialize: Boolean): super(context, autoInitialize)
    constructor(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean): super(context, autoInitialize, allowParallelSyncs)


    override fun onPerformSync(account: Account?, extras: Bundle?, authority: String?, provider: ContentProviderClient?, syncResult: SyncResult?) {
        val api = WeatherApi.create()

        api.get10DayForecast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    weatherResponse -> insertWeatherResponseToDb(weatherResponse)
                },{

                    error -> Log.d("SYNC" , error.localizedMessage)
                })

    }




    private fun insertWeatherResponseToDb(weatherResponse: WeatherResponse){
        val forecast = weatherResponse.forecast!!.simpleforecast!!.forecastday
        var contentValues : ArrayList<ContentValues> = ArrayList()

        //select datetime from day w
        //select last date and add to CV only if forecast > than last date

        val cursor: Cursor? = context.contentResolver.query(
                Uri.withAppendedPath(FW_Contract.BASE_CONTENT_URI, FW_Contract.PATH_DAYS),
                arrayOf("MAX(${FW_Contract.Day.COLUMN_NAME_DATE})"), null, null, null
        )
        cursor?.moveToFirst()
        val maxDate: Long? = cursor?.getLong(cursor.getColumnIndex("max(${FW_Contract.Day.COLUMN_NAME_DATE})"))

        forecast?.forEach {
            val date =  it!!.date!!.epoch!!.toLong()*1000
            val high = it.high!!.celsius!!.toDouble()
            val low = it.low!!.celsius!!.toDouble()

            if(maxDate == null || date > maxDate){
                val elem = ContentValues()
                elem.put(FW_Contract.Day.COLUMN_NAME_DATE, date)
                elem.put(FW_Contract.Day.COLUMN_NAME_TEMP_HIGH, high)
                elem.put(FW_Contract.Day.COLUMN_NAME_TEMP_LOW, low)
                contentValues.add(elem)
            }
        }

        val insertedValuesCount = context.contentResolver.bulkInsert(
                Uri.withAppendedPath(FW_Contract.BASE_CONTENT_URI, FW_Contract.PATH_DAYS),
                contentValues.toTypedArray()
        )
        Log.d("SYNC", "insert $insertedValuesCount vals")
    }

    private fun logWeatherResponse(weatherResponse: WeatherResponse){

        for (item in weatherResponse.forecast!!.simpleforecast!!.forecastday!!) {
            val date = Date(item!!.date!!.epoch!!.toLong() * 1000)
            Log.d("SYNC", "$date - ${item.low} - ${item.high}")
        }

    }
}


