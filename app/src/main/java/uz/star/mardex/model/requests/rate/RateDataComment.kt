package uz.star.mardex.model.requests.rate


import com.google.gson.annotations.SerializedName

data class RateDataComment(
    @SerializedName("rate")
    val rate: Rate,
    @SerializedName("user_id")
    val userId: String
)