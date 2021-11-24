package uz.star.mardex.model.results.server.location


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationResponse(
    @SerializedName("coordinates")
    val coordinates: List<Double>, // lat, long
    @SerializedName("type")
    val type: String
): Serializable