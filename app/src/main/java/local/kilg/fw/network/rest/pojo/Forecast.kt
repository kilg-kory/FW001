package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Forecast(

	@field:SerializedName("simpleforecast")
	val simpleforecast: Simpleforecast? = null,

	@field:SerializedName("txt_forecast")
	val txtForecast: TxtForecast? = null
)