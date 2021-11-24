package uz.star.mardex.model.response.promocode


import com.google.gson.annotations.SerializedName

data class PromocodeReequestData(
    @SerializedName("promocode")
    val promocode: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("user")
    val user: String
)