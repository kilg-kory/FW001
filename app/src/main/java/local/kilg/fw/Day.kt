package local.kilg.fw

import android.databinding.BindingAdapter
import android.util.Log
import android.view.View
import android.widget.ImageView
import java.util.*

/**
 *  kilg on 02.11.17.
 */
data class Day(val date: Date?,
               var icon: String?,
               val high: Int?,
               val low: Int?,
               val sunrise: Date?,
               val sunset: Date?,
               val moonrise: Date?,
               val moonset: Date?,
               private val body: (date: Day) -> Unit) {

    constructor(date: Long,
                icon: String?,
                high: Int,
                low: Int,
                sunrise: Long?,
                sunset: Long?,
                moonrise: Long?,
                moonset: Long?,
                body: (date: Day) -> Unit) : this(Date(date * 1000L), icon, high, low,
            sunrise?.let { if (it != 0L) Date(it) else null }, sunset?.let { if (it != 0L) Date(it) else null },
            moonrise?.let { if (it != 0L) Date(it) else null }, moonset?.let { if (it != 0L) Date(it) else null },
            body)

    constructor(date: Long,
                icon: String?,
                high: Int,
                low: Int,
                body: (date: Day) -> Unit) : this(Date(date * 1000L), icon, high, low, null, null, null, null, body)

    constructor() : this(null, null, null, null, null, null, null, null, {})


    val strHigh = high.toString() + '\u2103'
    val strLow = low.toString() + '\u2103'

    private val calendar: Calendar = Calendar.getInstance()

    val strDay: String
        get() {
            return if (date != null) {
                calendar.time = date
                String.format("%td", calendar)
            } else {
                ""
            }
        }

    private fun strFormatter(format: String, calendar: Calendar, date: Date?): String {
        return if (date != null) {
            calendar.time = date
            String.format(Locale.getDefault(), format, calendar)
        }else{
            ""
        }
    }

    val strMonth: String = strFormatter("%tB", calendar, date)
    val strSunrise: String
        get() {
            return if (sunrise != null) {
                calendar.time = sunrise
                String.format("%tR", calendar)
            } else {
                ""
            }
        }
    val strSunset: String
        get() {
            return if (sunset != null) {
                calendar.time = sunset
                String.format("%tR", calendar)
            } else {
                ""
            }
        }
    val strMoonrise: String
        get() {
            return if (moonrise != null) {
                calendar.time = moonrise
                String.format("%tR", calendar)
            } else {
                ""
            }
        }
    val strMoonset: String
        get() {
            return if (moonset != null) {
                calendar.time = moonset
                String.format("%tR", calendar)
            } else {
                ""
            }
        }


    fun click(view: View) = body(this)

    fun getEpoch(): Long {
        return date!!.time

    }

}

@BindingAdapter("android:src")
fun setImageDrawable(view: ImageView, iconName: String?) {
    var icon: Int = view.context.resources.getIdentifier(iconName, "drawable", view.context.packageName)
    if (icon == 0) icon = view.context.resources.getIdentifier("nothaveimage", "drawable", view.context.packageName)
    view.setImageDrawable(view.context.getDrawable(icon))

}
