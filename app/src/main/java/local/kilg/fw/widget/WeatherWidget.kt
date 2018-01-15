package local.kilg.fw.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import local.kilg.fw.R
import local.kilg.fw.provider.ForecastWeatherContract
import java.util.*
import local.kilg.fw.provider.ForecastWeatherContract.Forecast

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [WeatherWidgetConfigureActivity]
 */
class WeatherWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            WeatherWidgetConfigureActivity.deleteCountryCityPref(context, appWidgetId)
            WeatherWidgetConfigureActivity.deleteTextColorPref(context, appWidgetId)
            WeatherWidgetConfigureActivity.deleteUseBackgroundPref(context, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {

        Log.d("NEWSIZEWIDGET", "${newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)}-" +
                "${newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)} X " +
                "${newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)}-" +
                "${newOptions?.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)}")

        val views = RemoteViews(context!!.packageName, R.layout.weather_widget)
        configViews(views, newOptions!!.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH))
        appWidgetManager!!.updateAppWidget(appWidgetId, views)
    }


    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            val cityValue = WeatherWidgetConfigureActivity.loadCountryCityPref(context, appWidgetId)

            val index = context.resources.getStringArray(R.array.pref_city_list_values).indexOf(cityValue)

            val cityTitle = if (index >= 0) context.resources.getStringArray(R.array.pref_city_list_titles)[index] else ""

            val uri = Uri.parse("${ForecastWeatherContract.Forecast.CONTENT_URI}/" +
                    cityValue +
                    "/" +
                    Calendar.getInstance(Locale.getDefault()).timeInMillis / 1000L)

            val cursor = context.contentResolver.query(uri,
                    arrayOf(Forecast.COLUMN.TEMP_LOW, Forecast.COLUMN.TEMP_HIGH, Forecast.COLUMN.ICON),
                    null,
                    null,
                    null)


            var widgetText: String = "mis"
            var iconIdentifier: Int = context.resources.getIdentifier("nothaveimage", "drawable", context.packageName)
            if (cursor != null && cursor.count > 0) {
                cursor.moveToFirst()
                val high = cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_HIGH))
                val low = cursor.getInt(cursor.getColumnIndex(Forecast.COLUMN.TEMP_LOW))
                val icon = cursor.getString(cursor.getColumnIndex(Forecast.COLUMN.ICON))

                val ii = context.resources.getIdentifier(icon, "drawable", context.packageName)
                if (ii != 0) iconIdentifier = ii

                val mid = (high + low) / 2
                widgetText = mid.toString()
            }


            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.weather_widget)

            if (WeatherWidgetConfigureActivity.loadUseBackgroundPref(context, appWidgetId))
                views.setInt(R.id.widget_main_layout, "setBackgroundResource", R.drawable.blue_layout_background)
            else {
                views.setInt(R.id.widget_main_layout, "setBackgroundResource", 0)
            }



            views.setImageViewResource(R.id.weather_icon, iconIdentifier)

            views.setTextViewText(R.id.appwidget_text, widgetText)
            views.setTextColor(R.id.appwidget_text, WeatherWidgetConfigureActivity.loadTextColorPref(context, appWidgetId))

            views.setTextColor(R.id.city, WeatherWidgetConfigureActivity.loadTextColorPref(context, appWidgetId))
            views.setTextViewText(R.id.city, cityTitle)


            val minWidth = appWidgetManager.getAppWidgetOptions(appWidgetId).getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            configViews(views, minWidth)
            appWidgetManager.updateAppWidget(appWidgetId, views)
            cursor.close()
        }

        internal fun configViews(remoteViews: RemoteViews, minWidth: Int) {

            if (minWidth > 80) {
                remoteViews.setViewVisibility(R.id.weather_icon, View.VISIBLE)
//                remoteViews.setInt(R.id.weather_icon, "setColorFilter", Color.parseColor("#FFFFFF"))
            } else {
                remoteViews.setViewVisibility(R.id.weather_icon, View.GONE)
            }

            if (minWidth > 170) {
                remoteViews.setViewVisibility(R.id.city, View.VISIBLE)
            } else {
                remoteViews.setViewVisibility(R.id.city, View.GONE)
            }
        }
    }
}

