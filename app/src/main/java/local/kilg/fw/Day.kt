package local.kilg.fw

import java.util.*

/**
 * Created by kilg on 02.11.17.
 */
data class Day(val date:Date, val high:Double, val low: Double) {
    constructor(date:Long, high: Double, low: Double) : this(Date(date), high, low)
    constructor(date:String, high: Double, low: Double) : this(Date(date.toLong()), high, low)

    val strHigh = high.toString()
    val strLow = low.toString()
}