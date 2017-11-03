package local.kilg.fw.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import local.kilg.fw.provider.FW_Contract.Day
/**
 * Created by kilg on 13.10.17.
 */

class Helper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    companion object {
        private var instance: Helper? = null

        @Synchronized
        fun getInstance(ctx: Context): Helper {
            if (instance == null) {
                instance = Helper(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(Day.TABLE_NAME, true,
                Day._ID to SqlType.create("INTEGER PRIMARY KEY AUTOINCREMENT"),
                Day.COLUMN_NAME_DATE to SqlType.create("DATETIME"),
                Day.COLUMN_NAME_TEMP_HIGH to REAL,
                Day.COLUMN_NAME_TEMP_LOW to REAL
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(Day.TABLE_NAME, true)
        onCreate(db)
    }
}

// Access property for Context
val Context.database: Helper
    get() = Helper.getInstance(applicationContext)