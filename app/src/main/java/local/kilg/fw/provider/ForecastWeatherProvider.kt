package local.kilg.fw.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
import org.jetbrains.anko.db.transaction

/**
 * Created by kilg
 */
class ForecastWeatherProvider : ContentProvider() {

    companion object {
        private val ROUTE_FORECAST = 1
        private val ROUTE_FORECAST_DATE = 2
        val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI("local.kilg.fw", Forecast.PATH, ROUTE_FORECAST)
            uriMatcher.addURI("local.kilg.fw", "${Forecast.PATH}/#", ROUTE_FORECAST_DATE)
        }

    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val entryDate = when (uriMatcher.match(uri)) {
            ROUTE_FORECAST -> {
                context.database.writableDatabase.insertOrThrow(Forecast.TABLE_NAME, null, values)
                values!!.get(Forecast.COLUMN.DATE)
            }
            else -> {
                throw UnsupportedOperationException("Unknown uri: " + uri)
            }
        }

        return Uri.parse("${Forecast.CONTENT_URI}/$entryDate")
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
            ROUTE_FORECAST_DATE -> {
                val date: String = uri?.lastPathSegment ?: "replace it to current day date"
                context.database.readableDatabase.query(Forecast.TABLE_NAME,
                        projection,
                        "${Forecast.COLUMN.DATE} = ?",
                        arrayOf(date),
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
                ROUTE_FORECAST_DATE -> {
                    val date = uri!!.lastPathSegment
                    val whereClause = " strftime('%m%d', date(date, 'unixepoch', 'localtime')) " +
                            "= strftime('%m%d', date(?, 'unixepoch', 'localtime'))"

                    Log.d("UPDATE", "select * forecast  where ${whereClause} + $date")
                    context.database.writableDatabase.update(Forecast.TABLE_NAME, values, whereClause, arrayOf(date))
                }
                else -> throw UnsupportedOperationException("Unknown uri $uri")
            }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            ROUTE_FORECAST -> {
                //null in whereArgs because it not work or my hands grow out of ass.
                context.database.writableDatabase.delete(Forecast.TABLE_NAME, selection, null)
            }
            ROUTE_FORECAST_DATE -> {
                val id: String = uri!!.lastPathSegment
                context.database.writableDatabase.delete(Forecast.TABLE_NAME, "${Forecast.COLUMN.DATE}=?", arrayOf(id))
            }
            else -> throw UnsupportedOperationException("Unknown uri $uri")
        }
    }

    override fun getType(uri: Uri?): String = when (uriMatcher.match(uri)) {
        ROUTE_FORECAST -> Forecast.CONTENT_TYPE
        ROUTE_FORECAST_DATE -> Forecast.CONTENT_ITEM_TYPE
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

