package local.kilg.fw.network.sync

import android.accounts.Account
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import local.kilg.fw.network.rest.WeatherApi
import local.kilg.fw.network.rest.pojo.Astronomy
import local.kilg.fw.network.rest.pojo.WeatherResponse
import java.util.*
import kotlin.collections.ArrayList
import local.kilg.fw.provider.ForecastWeatherContract.Forecast


/**
 * Created by kilg on 17.10.17/.
 */


class SyncAdapter : AbstractThreadedSyncAdapter {
    constructor(context: Context, autoInitialize: Boolean) : super(context, autoInitialize)
    constructor(context: Context, autoInitialize: Boolean, allowParallelSyncs: Boolean) : super(context, autoInitialize, allowParallelSyncs)


    override fun onPerformSync(account: Account?, extras: Bundle?, authority: String?, provider: ContentProviderClient?, syncResult: SyncResult?) {
        val api = WeatherApi.create()

        api.get10DayForecast()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ weatherResponse ->
                    insertWeatherResponseToDb(weatherResponse)
                }, { error ->
                    throw error
                })

        api.getAstronomy()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ astronomy ->
                    insertAstronomyToCurrentDay(astronomy)
                }, { error ->
                    throw error
                })

    }

    private fun insertAstronomyToCurrentDay(astronomy: Astronomy) {
        Log.d("ASTRONOMY", "Run astronomy function ")
        Log.d("ASTRONOMY", astronomy.sunPhase!!.sunrise!!.hour.toString())

        val calendar = Calendar.getInstance()
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                astronomy.sunPhase!!.sunrise!!.hour!!.toInt(),
                astronomy.sunPhase.sunrise!!.minute!!.toInt(),
                0
        )
        val sunrise = calendar.time.time

        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                astronomy.sunPhase.sunset!!.hour!!.toInt(),
                astronomy.sunPhase.sunset.minute!!.toInt(),
                0
        )
        val sunset = calendar.time.time

        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                astronomy.moonPhase!!.moonrise!!.hour!!.toInt(),
                astronomy.moonPhase.moonrise!!.minute!!.toInt(),
                0
        )
        val moonrise = calendar.time.time


        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                astronomy.moonPhase.moonset!!.hour!!.toInt(),
                astronomy.moonPhase.moonset.minute!!.toInt(),
                0
        )
        val moonset = calendar.time.time

        val contentValues = ContentValues()
        contentValues.put(Forecast.COLUMN.SUNRISE, sunrise)
        contentValues.put(Forecast.COLUMN.SUNSET, sunset)
        contentValues.put(Forecast.COLUMN.MOONRISE, moonrise)
        contentValues.put(Forecast.COLUMN.MOONSET, moonset)

        // i already hate epoch
        val currentDate = (Calendar.getInstance().timeInMillis)/1000L
        context.contentResolver.update(Uri.withAppendedPath(Forecast.CONTENT_URI, currentDate.toString()), contentValues, null, null)
    }


    private fun insertWeatherResponseToDb(weatherResponse: WeatherResponse) {
        val forecast = weatherResponse.forecast!!.simpleforecast!!.forecastday
        val contentValues: ArrayList<ContentValues> = ArrayList()

        //select last date and add to content values only if forecast > than last date
        val cursor: Cursor? = context.contentResolver.query(
                Forecast.CONTENT_URI,
                arrayOf("MAX(${Forecast.COLUMN.DATE})"), null, null, null
        )
        cursor?.moveToFirst()
        val maxDate: Long? = cursor?.getLong(cursor.getColumnIndex("max(${Forecast.COLUMN.DATE})"))

        //fill content values
        //can be by insert if not exists...
        forecast?.forEach {
            if (maxDate == null || it!!.date!!.epoch!!.toLong() > maxDate) {
                val elem = ContentValues()
                elem.put(Forecast.COLUMN.DATE, it!!.date!!.epoch!!.toLong())
                elem.put(Forecast.COLUMN.TEMP_HIGH, it.high!!.celsius!!.toDouble())
                elem.put(Forecast.COLUMN.TEMP_LOW, it.low!!.celsius!!.toDouble())
                elem.put(Forecast.COLUMN.ICON, it.icon)
                contentValues.add(elem)
            }
        }

        //insert in one transaction
        val insertedValuesCount = context.contentResolver.bulkInsert(
                Forecast.CONTENT_URI,
                contentValues.toTypedArray()
        )
        Log.d("SYNC", "insert $insertedValuesCount vals")


        //delete old value
        val deletedValuesCount = context.contentResolver.delete(
                Forecast.CONTENT_URI,
                "${Forecast.COLUMN.DATE}<strftime('%s', 'now', 'localtime', '-3 day')",
                arrayOf("")
        )
        Log.d("SYNC", "delete $deletedValuesCount vals")



        cursor?.close()
    }


    private fun logWeatherResponse(weatherResponse: WeatherResponse) {

        for (item in weatherResponse.forecast!!.simpleforecast!!.forecastday!!) {
            val date = Date(item!!.date!!.epoch!!.toLong() * 1000)
            Log.d("SYNC", "$date - ${item.low} - ${item.high}")
        }

    }
}


