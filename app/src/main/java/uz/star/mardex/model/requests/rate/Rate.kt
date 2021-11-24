package uz.star.mardex.model.requests.rate


import com.google.gson.annotations.SerializedName

data class Rate(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("comments_id")
    val commentsId: List<String>,
    @SerializedName("mark")
    val mark: Int,
    @SerializedName("text")
    val text: String
)