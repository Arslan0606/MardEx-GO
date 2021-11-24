package uz.star.mardex.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import uz.star.mardex.data.local.LocalStorage


/**
 * Created by Farhod Tohirov on 16-May-21
 **/

class FirebaseMessagingServiceHelper : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        LocalStorage.instance.notificationToken = newToken
    }
}