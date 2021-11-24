package uz.star.mardex.model.response.server.user

import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.results.server.location.LocationResponse
import java.io.Serializable

data class UserData(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("pic")
    val pic: String?,
    @SerializedName("location")
    val locationResponse: LocationResponse,
    @SerializedName("users_id")
    val workersList: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("__v")
    val v: Int
) : Serializable