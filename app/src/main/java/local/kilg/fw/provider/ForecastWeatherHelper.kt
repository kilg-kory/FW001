package local.kilg.fw.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
import org.jetbrains.anko.db.*

/**
*  kilg on 13.10.17.
*/

class ForecastWeatherHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FW001", null, 4) {
    companion object {
        private var instance: ForecastWeatherHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): ForecastWeatherHelper {
            if (instance == null) {
                instance = ForecastWeatherHelper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(Forecast.TABLE_NAME,
                true,
                Forecast.COLUMN._ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                Forecast.COLUMN.DATE to INTEGER,
                Forecast.COLUMN.TEMP_HIGH to INTEGER,
                Forecast.COLUMN.TEMP_LOW to INTEGER,
                Forecast.COLUMN.ICON to TEXT,
                Forecast.COLUMN.SUNRISE to INTEGER,
                Forecast.COLUMN.SUNSET to INTEGER,
                Forecast.COLUMN.MOONRISE to INTEGER,
                Forecast.COLUMN.MOONSET to INTEGER,
                Forecast.COLUMN.COUNTRY_CITY to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(Forecast.TABLE_NAME, true)
        onCreate(db)
    }
}

// Access property for Context
val Context.database: ForecastWeatherHelper
    get() = ForecastWeatherHelper.getInstance(applicationContext)