package uz.star.mardex.model.response.server.intro


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.response.server.title.Title

data class IntroData(
    @SerializedName("_id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("subTitle")
    val subTitle: Title,
    @SerializedName("title")
    val title: Title,
    @Expose
    val imageLocal: Int
)