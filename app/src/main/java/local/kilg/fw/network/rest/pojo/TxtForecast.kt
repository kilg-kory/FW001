package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class TxtForecast(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("forecastday")
	val forecastday: List<ForecastdayItem?>? = null
)