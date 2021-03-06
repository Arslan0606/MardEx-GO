package uz.star.mardex.model.response.server.title


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Title(
    @SerializedName("uz")
    val uz: String? = null,
    @SerializedName("ru")
    val ru: String? = null,
    @SerializedName("uz_kr")
    val uz_kr: String? = null,
    @SerializedName("en")
    val eng: String? = null
): Serializable