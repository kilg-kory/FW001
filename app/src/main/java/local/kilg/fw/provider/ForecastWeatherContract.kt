package local.kilg.fw.provider

import android.content.ContentResolver
import android.net.Uri

/**
 * Created by kilg on 11.10.17.
 */
class ForecastWeatherContract {
    companion object {
        val CONTENT_AUTORITY: String = "local.kilg.fw"
        val BASE_CONTENT_URI: Uri = Uri.parse("content://$CONTENT_AUTORITY")

    }

    interface BaseColumns : android.provider.BaseColumns {
        //it's funny but in kotlin interfaces can't stored fields
        val _ID: String
            get() = android.provider.BaseColumns._ID
        val _COUNT: String
            get() = android.provider.BaseColumns._COUNT
    }

    object Forecast {

        val CONTENT_TYPE: String = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.fw.forecast"
        val CONTENT_ITEM_TYPE: String = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.fw.forecast.entry"

        val PATH = "forecast"
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH).build()

        val TABLE_NAME: String = "forecast"

        object COLUMN : BaseColumns {
            val DATE: String = "date"
            val REGION: String = "region"
            val TEMP_HIGH: String = "temp_high"
            val TEMP_LOW: String = "temp_low"
            val ICON: String = "icon"
            val SUNRISE: String = "sunrise"
            val SUNSET: String = "sunset"
            val MOONRISE: String = "moonrise"
            val MOONSET: String = "moonset"

        }
    }

    object Region {
        val TABLE_NAME: String = "region"

        object COLUMN: BaseColumns {
            val COUNTRY: String = "country"
            val COUNTRY_SHORT: String = "country_short"
            val CITY: String = "city"
        }
    }

    object Hour : BaseColumns {
        val TABLE_NAME: String = "hour"
    }

}

