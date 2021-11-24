package uz.star.mardex.model.response.server.worker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by Farhod Tohirov on 02-Mar-21
 **/

data class SumMark(
    @SerializedName("sum_all")
    var sumAll: Float = 0f,
    @SerializedName("sum_clients")
    var sumClients: Int = 0
): Serializable