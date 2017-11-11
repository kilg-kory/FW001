package local.kilg.fw.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import local.kilg.fw.provider.ForecastWeatherContract.Forecast
/**
 * Created by kilg on 13.10.17.
 */

class ForecastWeatherHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "FW001", null, 2) {
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
        db.createTable(Forecast.TABLE_NAME, true,
                Forecast.COLUMN._ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                Forecast.COLUMN.REGION to INTEGER,
                Forecast.COLUMN.DATE to SqlType.create("DATE"),
                Forecast.COLUMN.TEMP_HIGH to REAL,
                Forecast.COLUMN.TEMP_LOW to REAL,
                Forecast.COLUMN.ICON to TEXT
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