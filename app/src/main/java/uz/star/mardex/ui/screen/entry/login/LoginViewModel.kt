package uz.star.mardex.ui.screen.entry.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.R
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 12-Jan-21
 **/

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _login = MediatorLiveData<UserData>()
    val login: LiveData<UserData> get() = _login

    private val _message = MutableLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    fun login(phoneRaw: String, password: String) {
        var phone = phoneRaw
        phone = phone.replace("(", "").replace(")", "").replace("-", "")
        if (phone.trim().length == 13)
            _login.addSourceDisposable(repository.login(LoginData(phone, password))) {
                it.onData { status ->
                    sendNotificationToken(status)
                }.onMessage { message ->
                    _message.value = MessageData.message(message)
                }.onMessageData { messageData ->
                    _message.value = messageData
                }
            }
        else
            _message.value = MessageData.resource(R.string.please_enter_phone)
    }

    private fun sendNotificationToken(userData: UserData) {
        _login.addSourceDisposable(repository.sendNotificationTokenToServer()) {
            it.onData {
                _login.value = userData
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }
}