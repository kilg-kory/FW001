package local.kilg.fw.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import org.jetbrains.anko.db.transaction

/**
* Created by kilg
*/
class FW_Provider : ContentProvider() {

    companion object {
        val ROUTE_DAYS = 1
        val ROUTE_DAY_DATE = 2
        val uriMatcher : UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            uriMatcher.addURI("local.kilg.fw", "day", ROUTE_DAYS)
            uriMatcher.addURI("local.kilg.fw", "day/#", ROUTE_DAY_DATE)
        }

    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri =
            Uri.parse("${FW_Contract.Day.CONTENT_URI}/" + when(uriMatcher.match(uri)){
                ROUTE_DAYS -> context.database.use {

                    insertOrThrow(FW_Contract.Day.TABLE_NAME, null, values)
                }
                else -> {
                    throw UnsupportedOperationException("Unknown uri: " + uri)
                }
        })




    override fun query(uri: Uri?,
                       projection: Array<out String>?,
                       selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {

        val cursor = when(uriMatcher.match(uri)){
            ROUTE_DAYS -> {
                Log.d("PROVIDER", "ROUTE_DAYS")
                context.database.readableDatabase.query(FW_Contract.Day.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder)
            }
            ROUTE_DAY_DATE ->{
                Log.d("PROVIDER", "ROUTE_DAY - ${uri?.lastPathSegment}")
                val date: String = uri?.lastPathSegment ?: "replace it to current day date"
                context.database.readableDatabase.query(FW_Contract.Day.TABLE_NAME,
                        projection, "${FW_Contract.Day.COLUMN_NAME_DATE} = ?", arrayOf(date), null, null,null)

            }
            else -> throw UnsupportedOperationException("Unknown uri $uri")
        }

        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean = true

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int =
            when(uriMatcher.match(uri)){
                ROUTE_DAYS -> context.database.use {
                    update(FW_Contract.Day.TABLE_NAME, values, selection, selectionArgs)
                }
                else -> throw UnsupportedOperationException("Unknown uri $uri")
            }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int =
            when(uriMatcher.match(uri)){
                ROUTE_DAYS -> context.database.use {
                    delete(FW_Contract.Day.TABLE_NAME, selection, selectionArgs)
                }
                ROUTE_DAY_DATE -> context.database.use{
                    val id : String = uri!!.lastPathSegment
                    delete(FW_Contract.Day.TABLE_NAME, "${FW_Contract.Day._ID} = ?", arrayOf(id))
                }
                else -> throw UnsupportedOperationException("Unknown uri $uri")
            }

    override fun getType(uri: Uri?): String = when(uriMatcher.match(uri)){
        ROUTE_DAYS -> FW_Contract.Day.CONTENT_TYPE
        ROUTE_DAY_DATE -> FW_Contract.Day.CONTENT_ITEM_TYPE
        else -> {
            throw UnsupportedOperationException("Unknown uri $uri")
        }
    }

    override fun bulkInsert(uri: Uri?, values: Array<out ContentValues>?): Int {
        var numInserted: Int = 0
        context.database.use {
            transaction {
                values?.forEach {
                    insertOrThrow(FW_Contract.Day.TABLE_NAME, null, it)
                    numInserted++
                }
            }
        }

        context.contentResolver.notifyChange(uri, null)
        return numInserted
    }
}

