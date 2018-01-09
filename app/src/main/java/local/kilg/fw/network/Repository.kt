package local.kilg.fw.network

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import local.kilg.fw.R
import local.kilg.fw.network.rest.WeatherApi
import local.kilg.fw.network.rest.pojo.Astronomy
import local.kilg.fw.network.rest.pojo.WeatherResponse
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

/**
 *  kilg on 07.12.17.
 */

class Repository(val context: Context) {

    private val countryCityString: String = context.defaultSharedPreferences.getString(
            context.resources.getString(R.string.city_list_key),
            context.resources.getStringArray(R.array.pref_city_list_values)[0])

    /**
     * Async load weather to database
     * */
    fun loadWeather() {


        val api = WeatherApi.create()
        api.get10DayForecast(countryCityString.split("/")[0], countryCityString.split("/")[1])
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ weatherResponse ->
                    insertWeatherToDb(weatherResponse)
                }, { error ->
                    throw error
                })
    }

    /**
     * Async load astronomy for current day to database
     * */
    fun loadAstronomy() {
        val api = WeatherApi.create()
        api.getAstronomy(countryCityString.split("/")[0], countryCityString.split("/")[1])
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ astronomy ->
                    insertAstronomyToDb(astronomy)
                }, { error ->
                    throw error
                })
    }

    private fun insertAstronomyToDb(astronomy: Astronomy) {
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
        val currentDate = (Calendar.getInstance().timeInMillis) / 1000L
        val uri = Uri.parse("${Forecast.CONTENT_URI}/$countryCityString/$currentDate")
        context.contentResolver.update(uri, contentValues, null, null)
    }

    private fun insertWeatherToDb(weatherResponse: WeatherResponse) {
        val forecast = weatherResponse.forecast!!.simpleforecast!!.forecastday
        val contentValues: ArrayList<ContentValues> = ArrayList()

        //select last date and add to content values only if forecast > than last date
        val uri = Uri.parse("${Forecast.CONTENT_URI}/$countryCityString")

        val cursor: Cursor? = context.contentResolver.query(
                uri,
                arrayOf("MAX(${Forecast.COLUMN.DATE}) as max"), null, null, null
        )
        cursor?.moveToFirst()
        val maxDate: Long? = cursor?.getLong(0)

        //fill content values
        //insert if not exists...
        forecast?.forEach {
            if (maxDate == null || it!!.date!!.epoch!!.toLong() > maxDate) {
                val elem = ContentValues()
                elem.put(Forecast.COLUMN.DATE, it!!.date!!.epoch!!.toLong())
                elem.put(Forecast.COLUMN.TEMP_HIGH, it.high!!.celsius!!.toDouble())
                elem.put(Forecast.COLUMN.TEMP_LOW, it.low!!.celsius!!.toDouble())
                elem.put(Forecast.COLUMN.ICON, it.icon)
                elem.put(Forecast.COLUMN.COUNTRY_CITY, countryCityString)
                contentValues.add(elem)
            }
        }

        //insert in one transaction
        context.contentResolver.bulkInsert(
                Uri.parse("${Forecast.CONTENT_URI}/$countryCityString"),
                contentValues.toTypedArray()
        )


        //delete old value
        context.contentResolver.delete(
                Forecast.CONTENT_URI,
                "${Forecast.COLUMN.DATE}<strftime('%s', 'now', 'localtime', '-3 day')",
                arrayOf("")
        )

        cursor?.close()
    }


    fun isOnline(): Boolean {
        val connectivity: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivity.activeNetworkInfo
        return networkInfo.isConnectedOrConnecting

    }

    fun isEmpty(): Boolean {

        val uri = Uri.parse("${Forecast.CONTENT_URI}/$countryCityString")

        val cursor = context.contentResolver.query(
                uri,
                arrayOf("count(*) AS count"),
                null,
                null,
                null)

        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.moveToFirst()

        cursor.close()
        return count == 0
    }
}