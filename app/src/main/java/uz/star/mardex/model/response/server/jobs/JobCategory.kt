package uz.star.mardex.model.response.server.jobs


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.response.server.title.Title

data class JobCategory(
    @SerializedName("category_title")
    val categoryTitle: Title,
    @SerializedName("jobs")
    val jobs: List<Job>,
    @SerializedName("pic")
    val pic: String?,
    @Expose
    var isCollapsed: Boolean = false
)