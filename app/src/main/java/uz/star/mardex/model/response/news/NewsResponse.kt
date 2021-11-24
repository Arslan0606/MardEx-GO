package uz.star.mardex.model.response.news

import com.google.gson.annotations.SerializedName
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.utils.extension.toLocalString
import java.io.Serializable

data class NewsResponse(

    @field:SerializedName("data")
    val data: List<NewsData>,

    @field:SerializedName("success")
    val success: Boolean? = null
) : Serializable

data class Title(

    @field:SerializedName("ru")
    val ru: String? = null,

    @field:SerializedName("uz")
    val uz: String? = null,

    @field:SerializedName("uz_kr")
    val uzKr: String? = null,

    @field:SerializedName("en")
    val en: String? = null
) : Serializable

fun Title.toLocalString(): String? {
    return when (LocalStorage.instance.langLocal) {
        "uz" -> {
            this.uz
        }
        "ru" -> {
            this.ru
        }
        else -> {
            this.uzKr
        }
    }
}

data class Description(

    @field:SerializedName("ru")
    val ru: String? = null,

    @field:SerializedName("uz")
    val uz: String? = null,

    @field:SerializedName("uz_kr")
    val uzKr: String? = null,

    @field:SerializedName("en")
    val en: String? = null
) : Serializable

fun Description.toLocalString(): String? {
    return when (LocalStorage.instance.langLocal) {
        "uz" -> {
            this.uz
        }
        "ru" -> {
            this.ru
        }
        else -> {
            this.uzKr
        }
    }
}

data class NewsData(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("__v")
    val V: Int? = null,

    @field:SerializedName("description")
    val description: Description? = null,

    @field:SerializedName("created_at")
    val createdAt: Long? = null,

    @field:SerializedName("_id")
    val id: String? = null,

    @field:SerializedName("title")
    val title: Title? = null
) : Serializable
