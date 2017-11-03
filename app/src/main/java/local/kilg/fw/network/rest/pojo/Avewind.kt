package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Avewind(

	@field:SerializedName("kph")
	val kph: Int? = null,

	@field:SerializedName("mph")
	val mph: Int? = null,

	@field:SerializedName("dir")
	val dir: String? = null,

	@field:SerializedName("degrees")
	val degrees: Int? = null
)