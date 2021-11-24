package uz.star.mardex.ui.screen.entry.reset_passpord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.R
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Kurganbaev Jasurbek on 22.04.2021 15:57
 **/

@HiltViewModel
class RestorePasswordViewModel @Inject constructor(
    private val model: RestorePasswordRepository
) : ViewModel() {

    private val _responseRestorePassword = MediatorLiveData<LoginData>()
    val responseRestorePassword: LiveData<LoginData> get() = _responseRestorePassword


    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> get() = _loader

    fun openCodeVerifyScreen(etPhoneNumber: String, etPassword: String, etRepeatPassword: String) {
        when {
            etPhoneNumber.length < 13 -> {
                _message.value =
                    MessageData.resource(R.string.please_enter_phone)
            }

            etPassword.length < 4 -> {
                _message.value = MessageData.resource(R.string.password_lenth_no)
            }

            etPassword.isEmpty() -> {
                _message.value =
                    MessageData.resource(R.string.enter_password)
            }

            etRepeatPassword.isEmpty() -> {
                _message.value =
                    MessageData.resource(R.string.enter_new_password_again)

            }

            etPassword != etRepeatPassword -> {
                _message.value =
                    MessageData.resource(R.string.password_not_same)

            }

            else -> {
                _loader.value = true
                _message.addSourceDisposable(model.sendVerificationCode(etPhoneNumber)) {
                    _loader.value = false
                    it.onData {
                        val loginData = LoginData(etPhoneNumber, etPassword)
                        _responseRestorePassword.value = loginData
                    }.onMessage { message ->
                        _message.value = MessageData.message(message)
                    }.onMessageData { messageData->
                        _message.value = messageData
                    }
                }

            }
        }
    }

}
