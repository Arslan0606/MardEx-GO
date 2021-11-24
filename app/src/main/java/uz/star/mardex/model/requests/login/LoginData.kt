package uz.star.mardex.model.requests.login


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LoginData(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("password")
    val password: String
): Serializable