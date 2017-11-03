package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class WeatherResponse(

	@field:SerializedName("response")
	val response: Response? = null,

	@field:SerializedName("forecast")
	val forecast: Forecast? = null
)