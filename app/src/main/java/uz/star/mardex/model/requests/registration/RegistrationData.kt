package uz.star.mardex.model.requests.registration


import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.requests.location.LocationRequest
import java.io.Serializable

data class RegistrationData(
    @SerializedName("location")
    val locationRequest: LocationRequest?,
    @SerializedName("name")
    val name: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("promocode")
    val promocode: Int

): Serializable