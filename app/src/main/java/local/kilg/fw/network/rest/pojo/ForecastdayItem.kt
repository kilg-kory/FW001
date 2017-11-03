package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ForecastdayItem(

	@field:SerializedName("date")
	val date: Date? = null,

	@field:SerializedName("icon_url")
	val iconUrl: String? = null,

	@field:SerializedName("period")
	val period: Int? = null,

	@field:SerializedName("maxhumidity")
	val maxhumidity: Int? = null,

	@field:SerializedName("skyicon")
	val skyicon: String? = null,

	@field:SerializedName("avewind")
	val avewind: Avewind? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("avehumidity")
	val avehumidity: Int? = null,

	@field:SerializedName("snow_allday")
	val snowAllday: SnowAllday? = null,

	@field:SerializedName("qpf_day")
	val qpfDay: QpfDay? = null,

	@field:SerializedName("maxwind")
	val maxwind: Maxwind? = null,

	@field:SerializedName("pop")
	val pop: Int? = null,

	@field:SerializedName("qpf_night")
	val qpfNight: QpfNight? = null,

	@field:SerializedName("high")
	val high: High? = null,

	@field:SerializedName("minhumidity")
	val minhumidity: Int? = null,

	@field:SerializedName("low")
	val low: Low? = null,

	@field:SerializedName("snow_night")
	val snowNight: SnowNight? = null,

	@field:SerializedName("snow_day")
	val snowDay: SnowDay? = null,

	@field:SerializedName("conditions")
	val conditions: String? = null,

	@field:SerializedName("qpf_allday")
	val qpfAllday: QpfAllday? = null
)