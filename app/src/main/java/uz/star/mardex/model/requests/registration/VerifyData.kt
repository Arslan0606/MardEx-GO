package uz.star.mardex.model.requests.registration


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VerifyData(
    @SerializedName("check_string")
    val checkString: String,
    @SerializedName("phone")
    val phone: String
) : Serializable