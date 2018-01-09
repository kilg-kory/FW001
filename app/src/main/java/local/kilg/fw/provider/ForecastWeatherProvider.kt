package local.kilg.fw.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import local.kilg.fw.R
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
import org.jetbrains.anko.db.transaction
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by kilg
 */
class ForecastWeatherProvider : ContentProvider() {


    companion object {
        private val ROUTE_FORECAST = 1
        private val ROUTE_FORECAST_COUNTRY_CITY = 2
        private val ROUTE_FORECAST_COUNTRY_CITY_DATE = 3
        val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)


        init {
            uriMatcher.addURI("local.kilg.fw", Forecast.PATH, ROUTE_FORECAST)
            uriMatcher.addURI("local.kilg.fw", "${Forecast.PATH}/*/*", ROUTE_FORECAST_COUNTRY_CITY)
            uriMatcher.addURI("local.kilg.fw", "${Forecast.PATH}/*/*/#", ROUTE_FORECAST_COUNTRY_CITY_DATE)
        }

    }


    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        when (uriMatcher.match(uri)) {
            ROUTE_FORECAST_COUNTRY_CITY_DATE -> {

                val date = uri!!.lastPathSegment
                val country = uri.pathSegments[uri.pathSegments.size - 1]
                val city = uri.pathSegments[uri.pathSegments.size - 2]

                context.database.writableDatabase.insertOrThrow(Forecast.TABLE_NAME, null, values)
                return Uri.parse("${Forecast.CONTENT_URI}/$country/$city/$date")
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)

        }
    }


    override fun query(uri: Uri?,
                       projection: Array<out String>?,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {


        val cursor = when (uriMatcher.match(uri)) {
            ROUTE_FORECAST -> {
                context.database.readableDatabase.query(Forecast.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder)
            }
            ROUTE_FORECAST_COUNTRY_CITY -> {
                val country = uri!!.pathSegments[uri.pathSegments.size - 2]
                val city = uri.pathSegments[uri.pathSegments.size - 1]

                context.database.readableDatabase.query(Forecast.TABLE_NAME,
                        projection,
                        "${Forecast.COLUMN.COUNTRY_CITY} = ?",
                        arrayOf("$country/$city"),
                        null, null, sortOrder)
            }
            ROUTE_FORECAST_COUNTRY_CITY_DATE -> {
                val date = uri!!.lastPathSegment
                val country = uri.pathSegments[uri.pathSegments.size - 3]
                val city = uri.pathSegments[uri.pathSegments.size - 2]

                context.database.readableDatabase.query(Forecast.TABLE_NAME,
                        projection,
                        "${Forecast.COLUMN.DATE} = ? AND ${Forecast.COLUMN.COUNTRY_CITY} = ?",
                        arrayOf(date, "$country/$city"),
                        null,
                        null,
                        null
                )

            }
            else -> throw UnsupportedOperationException("Unknown uri $uri")
        }

        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean = true

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int =
            when (uriMatcher.match(uri)) {
                ROUTE_FORECAST -> {
                    context.database.writableDatabase.update(Forecast.TABLE_NAME, values, selection, selectionArgs)
                }

                ROUTE_FORECAST_COUNTRY_CITY_DATE -> {
                    val date = uri!!.lastPathSegment
                    val country = uri.pathSegments[uri.pathSegments.size - 1]
                    val city = uri.pathSegments[uri.pathSegments.size - 2]

                    val whereClause = " strftime('%m%d', date(${Forecast.COLUMN.DATE}, 'unixepoch', 'localtime')) " +
                            "= strftime('%m%d', date(?, 'unixepoch', 'localtime'))" +
                            "AND ${Forecast.COLUMN.COUNTRY_CITY} = ?"

                    Log.d("UPDATE", "[[[ $whereClause + $date + $country/$city ]]]")
                    context.database.writableDatabase.update(Forecast.TABLE_NAME, values, whereClause, arrayOf(date, "$country/$city"))
                }
                else -> throw UnsupportedOperationException("Unknown uri $uri")
            }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            ROUTE_FORECAST -> {
                //null in whereArgs because it not work or my hands grow out of ass.
                context.database.writableDatabase.delete(Forecast.TABLE_NAME, selection, null)
            }
            else -> throw UnsupportedOperationException("Unknown uri $uri")
        }
    }

    override fun getType(uri: Uri?): String = when (uriMatcher.match(uri)) {
        ROUTE_FORECAST -> Forecast.CONTENT_TYPE
        ROUTE_FORECAST_COUNTRY_CITY -> Forecast.CONTENT_TYPE
        ROUTE_FORECAST_COUNTRY_CITY_DATE -> Forecast.CONTENT_ITEM_TYPE
        else -> {
            throw UnsupportedOperationException("Unknown uri $uri")
        }
    }

    override fun bulkInsert(uri: Uri?, values: Array<out ContentValues>?): Int {
        var numInserted = 0
        context.database.use {
            transaction {
                values?.forEach {
                    insertOrThrow(Forecast.TABLE_NAME, null, it)
                    numInserted++
                }
            }
        }

        context.contentResolver.notifyChange(uri, null)
        return numInserted
    }
}

