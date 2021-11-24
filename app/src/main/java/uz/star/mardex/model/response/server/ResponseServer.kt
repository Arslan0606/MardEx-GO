package uz.star.mardex.model.response.server

import com.google.gson.annotations.SerializedName

/**
 * Created by Farhod Tohirov on 09-Jan-21
 **/

data class ResponseServer<T>(
    @SerializedName("data")
    val data: T,
    @SerializedName("success")
    val success: Boolean,

    var token: String? = null
)