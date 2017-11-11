package local.kilg.fw

import android.databinding.BindingAdapter
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
               private val body: (date: Day) -> Unit) {
    constructor(date: Long,
                icon: String?,
                high: Int,
                low: Int,
                body: (date: Day) -> Unit) : this(Date(date), icon, high, low, body)

    constructor(date: String,
                icon: String?,
                high: Int,
                low: Int,
                body: (date: Day) -> Unit) : this(Date(date.toLong()), icon, high, low, body)

    constructor() : this(null, null, null, null, {})


    val strHigh = high.toString() + '\u2103'
    val strLow = low.toString() + '\u2103'

    private val calendar: Calendar = Calendar.getInstance()

    val strDay: String
        get() {
            return if (date != null) {
                calendar.time = date
                calendar.get(Calendar.DAY_OF_MONTH).toString()
            } else {
                ""
            }
        }

    val strMonth: String
        get() {
            return if (date != null) {
                calendar.time = date
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
            } else {
                ""
            }
        }


    fun click(view: View) = body(this)

    fun getTimestamp(): Long {
        val output = date!!.time / 1000L
        val str = java.lang.Long.toString(output)
        return java.lang.Long.parseLong(str) * 1000

    }

}

@BindingAdapter("android:src")
fun setImageDrawable(view: ImageView, iconName: String?) {
    var icon: Int = view.context.resources.getIdentifier(iconName, "drawable", view.context.packageName)
    if (icon == 0) icon = view.context.resources.getIdentifier("nothaveimage", "drawable", view.context.packageName)
    view.setImageDrawable(view.context.getDrawable(icon))

}
