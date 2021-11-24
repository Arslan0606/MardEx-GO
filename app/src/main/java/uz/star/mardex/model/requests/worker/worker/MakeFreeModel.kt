package uz.star.mardex.model.requests.worker

import com.google.gson.annotations.SerializedName

/**
 * Created by Farhod Tohirov on 05-Feb-21
 **/

data class MakeFreeModel(
    @SerializedName("user_id")
    var userId: String
)