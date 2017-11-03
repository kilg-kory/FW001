package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Date(

	@field:SerializedName("tz_short")
	val tzShort: String? = null,

	@field:SerializedName("pretty")
	val pretty: String? = null,

	@field:SerializedName("ampm")
	val ampm: String? = null,

	@field:SerializedName("year")
	val year: Int? = null,

	@field:SerializedName("isdst")
	val isdst: String? = null,

	@field:SerializedName("weekday")
	val weekday: String? = null,

	@field:SerializedName("weekday_short")
	val weekdayShort: String? = null,

	@field:SerializedName("epoch")
	val epoch: String? = null,

	@field:SerializedName("sec")
	val sec: Int? = null,

	@field:SerializedName("min")
	val min: String? = null,

	@field:SerializedName("month")
	val month: Int? = null,

	@field:SerializedName("hour")
	val hour: Int? = null,

	@field:SerializedName("monthname")
	val monthname: String? = null,

	@field:SerializedName("tz_long")
	val tzLong: String? = null,

	@field:SerializedName("yday")
	val yday: Int? = null,

	@field:SerializedName("day")
	val day: Int? = null
)