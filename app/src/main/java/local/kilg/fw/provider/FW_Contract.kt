package local.kilg.fw.provider

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by kilg on 11.10.17.
 */
class FW_Contract {
    companion object {
        val CONTENT_AUTORITY : String = "local.kilg.fw"
        val BASE_CONTENT_URI : Uri = Uri.parse("content://$CONTENT_AUTORITY")
        val PATH_DAYS : String = "day"
    }

    interface BaseColumns : android.provider.BaseColumns {
        //it's funny but in kotlin interfaces can't stored fields
        val _ID: String
            get() = android.provider.BaseColumns._ID
        val _COUNT: String
            get() = android.provider.BaseColumns._COUNT
    }

    object Day : BaseColumns {
        val CONTENT_TYPE : String = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.fw.days"
        val CONTENT_ITEM_TYPE : String = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.fw.day"
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DAYS).build()

        val TABLE_NAME : String = "day"

        enum class COLUMN(val cn:String){
            REGION("region"),
            TEMP_HIGH("temp_high"),
            TEMP_LOW("temp_low")
        }
        val COLUMN_NAME_REGION : String = "region"
        val COLUMN_NAME_DATE : String = "date"
        val COLUMN_NAME_SUNRISE : String = "sunrise"
        val COLUMN_NAME_SUNSET : String = "sunset"
        val COLUMN_NAME_MOON_CODE : String = "moon_code"
        val COLUMN_NAME_TEMP_LOW: String = "temp_min"
        val COLUMN_NAME_TEMP_HIGH: String = "temp_high"
        val COLUMN_NAME_TEMP_AVG : String = "temp_avg"
        val COLUMN_NAME_FEELS_LIKE : String = "feels_like"
        val COLUMN_NAME_WIND_SPEED : String = "wind_speed"
        val COLUMN_NAME_WIND_GUST : String = "wind_gust"
        val COLUMN_NAME_WIND_DIR : String = "wind_dir"
        val COLUMN_NAME_PRESSURE_MM : String = "pressure_mm"
        val COLUMN_NAME_HUMIDITY : String = "humidity"
        val COLUMN_NAME_PREC_TYPE : String = "prec_type"
        val COLUMN_NAME_PREC_STRENGTH : String = "prec_strength"
        val COLUMN_NAME_CLOUDNESS : String = "cloudness"


    }
    object Hour: BaseColumns {
        val TABLE_NAME : String = "hour"
    }

}