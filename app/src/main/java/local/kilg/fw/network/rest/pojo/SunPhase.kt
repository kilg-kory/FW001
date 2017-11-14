package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class SunPhase(

	@field:SerializedName("sunrise")
	val sunrise: Sunrise? = null,

	@field:SerializedName("sunset")
	val sunset: Sunset? = null
)