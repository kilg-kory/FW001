package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Astronomy(

	@field:SerializedName("sun_phase")
	val sunPhase: SunPhase? = null,

	@field:SerializedName("moon_phase")
	val moonPhase: MoonPhase? = null,

	@field:SerializedName("response")
	val response: Response? = null
)