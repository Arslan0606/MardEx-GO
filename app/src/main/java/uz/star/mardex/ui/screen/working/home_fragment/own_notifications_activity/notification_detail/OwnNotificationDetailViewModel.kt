package uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.notification_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.OwnNotificationsRepository
import uz.star.mardex.utils.extension.addSourceDisposable
import uz.star.mardexworker.model.response.notification_for_user.OwnNotificationResponse
import javax.inject.Inject

@HiltViewModel
class OwnNotificationDetailViewModel @Inject constructor(
    private val repository: OwnNotificationsRepository,
    private val storage: LocalStorage
) : ViewModel() {

    private val _allNotifications = MediatorLiveData<List<OwnNotificationResponse>>()
    val allNotifications: LiveData<List<OwnNotificationResponse>> get() = _allNotifications

    private val _notificationLiveData = MediatorLiveData<OwnNotificationResponse>()
    val notificationLiveData: LiveData<OwnNotificationResponse> get() = _notificationLiveData

    private var _message = MutableLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    fun getAllOwnNotifications() {

    }

    fun changeStatusToRead(id: String) {
        _notificationLiveData.addSourceDisposable(repository.patchToReadNotification(id)) {
            it.onData { data ->
                _notificationLiveData.value = data
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { stringResources ->
                    _message.value = MessageData.resource(stringResources)
                }
            }
        }
    }

}