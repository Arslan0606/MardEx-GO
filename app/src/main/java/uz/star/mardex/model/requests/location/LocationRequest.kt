package uz.star.mardex.model.requests.location


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationRequest(
    @SerializedName("coordinates")
    val coordinates: List<Double> // lat, long
): Serializable