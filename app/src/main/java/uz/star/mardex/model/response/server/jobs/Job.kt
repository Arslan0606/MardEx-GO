package uz.star.mardex.model.response.server.jobs


import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.response.server.title.Title
import java.io.Serializable

data class Job(
    @SerializedName("category_job")
    val categoryJob: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("pic")
    val pic: String,
    @SerializedName("title")
    val title: Title,
    @SerializedName("__v")
    val v: Int? = null,
) : Serializable