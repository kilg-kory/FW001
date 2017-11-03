package local.kilg.fw.network.rest.pojo

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class Response(

	@field:SerializedName("features")
	val features: Features? = null,

	@field:SerializedName("version")
	val version: String? = null,

	@field:SerializedName("termsofService")
	val termsofService: String? = null
)