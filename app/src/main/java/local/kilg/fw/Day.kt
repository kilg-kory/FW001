package local.kilg.fw

import android.view.View
import java.util.*



/**
 * Created by kilg on 02.11.17.
 */
data class Day(val date: Date?, val high: Double?, val low: Double?,val body: (date:Day) -> Unit ) {
    constructor(date: Long, high: Double, low: Double, body: (date:Day) -> Unit) : this(Date(date), high, low, body   )
    constructor(date: String, high: Double, low: Double, body: (date:Day) -> Unit) : this(Date(date.toLong()), high, low, body )
    constructor(): this(null, null, null, {})

    val strHigh = high.toString()
    val strLow = low.toString()


    fun click(view: View) = body(this)


    fun getTimestamp(): Long {

        val output = date!!.time / 1000L
        val str = java.lang.Long.toString(output)
        val timestamp = java.lang.Long.parseLong(str) * 1000

        return timestamp

    }

}