package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class MoonPhase(

	@field:SerializedName("moonset")
	val moonset: Moonset? = null,

	@field:SerializedName("sunrise")
	val sunrise: Sunrise? = null,

	@field:SerializedName("sunset")
	val sunset: Sunset? = null,

	@field:SerializedName("phaseofMoon")
	val phaseofMoon: String? = null,

	@field:SerializedName("ageOfMoon")
	val ageOfMoon: String? = null,

	@field:SerializedName("hemisphere")
	val hemisphere: String? = null,

	@field:SerializedName("moonrise")
	val moonrise: Moonrise? = null,

	@field:SerializedName("percentIlluminated")
	val percentIlluminated: String? = null,

	@field:SerializedName("current_time")
	val currentTime: CurrentTime? = null
)