package uz.star.mardex.model.response.server


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.response.server.title.Title
import java.io.Serializable

data class RateData(
    @SerializedName("for_whom")
    val forWhom: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("related_star")
    val relatedStar: Int,
    @SerializedName("title")
    val title: Title,
    @SerializedName("__v")
    val v: Int,

    @Expose
    var isChecked: Boolean = false
): Serializable