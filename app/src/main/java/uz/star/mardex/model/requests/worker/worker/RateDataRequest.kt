package uz.star.mardex.model.requests.worker.worker

import com.google.gson.annotations.SerializedName

/**
 * Created by Farhod Tohirov on 09-Feb-21
 **/

data class RateDataRequest(
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("client_id")
    var clientId: String,
    var mark: Float
)