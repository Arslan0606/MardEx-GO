package uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.notification_for_user.IsRead
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import uz.star.mardexworker.model.response.notification_for_user.OwnNotificationResponse
import javax.inject.Inject


/**
 * Created by Davronbek Raximjanov on 27/06/2021
 **/

class OwnNotificationsRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi
) : OwnNotificationsRepository, SafeApiRequest() {

    override fun getAllOwnNotifications(userId: String): LiveData<ResultData<List<OwnNotificationResponse>>> {
        val resultLiveData = MutableLiveData<ResultData<List<OwnNotificationResponse>>>()

        Coroutines.ioThenMain(
            { apiRequest { api.getAllOwnNotifications(userId) } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(it.data)
                    else
                        resultLiveData.value = ResultData.message("llll bosh")
                }
                data?.onMessage {
                    resultLiveData.value = ResultData.message(it)
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                    }
                }
            }
        )
        return resultLiveData
    }

    override fun patchToReadNotification(notificationId: String): LiveData<ResultData<OwnNotificationResponse>> {
        val resultLiveData = MutableLiveData<ResultData<OwnNotificationResponse>>()

        Coroutines.ioThenMain(
            { apiRequest { api.changeStatusToRead(notificationId, IsRead(true)) } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(it.data)
                    else
                        resultLiveData.value = ResultData.message("llll bosh")
                }
                data?.onMessage {
                    resultLiveData.value = ResultData.message(it)
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                    }
                }
            }
        )

        return resultLiveData
    }

}