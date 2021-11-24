package uz.star.mardex.model.data_exchange

import java.io.Serializable

/**
 * Created by Farhod Tohirov on 16-May-21
 **/

data class PushNotificationData(
    var user_id: String,
    var body: String,
    var text: String
): Serializable
