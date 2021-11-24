package uz.star.mardex.ui.screen.entry.code_verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.requests.registration.RegistrationData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

@HiltViewModel
class SmsVerificationViewModel @Inject constructor(private val repository: SmsVerificationRepository) : ViewModel() {

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _registerResponse = MediatorLiveData<UserData>()
    val registerResponse: LiveData<UserData> get() = _registerResponse

    fun verifyPhone(registrationData: RegistrationData, userSmsCode: String) {
        _message.addSourceDisposable(repository.verifyPhone(registrationData.phone, userSmsCode)) {
            it.onData { success ->
                if (registrationData.name == "restore")
                    restorePassword(LoginData(registrationData.phone, registrationData.password))
                else
                    registerClient(registrationData.phone, registrationData.name, registrationData.password, registrationData.promocode)
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { stringSource ->
                    _message.value = MessageData.resource(stringSource)
                }
            }
        }
    }

    private fun registerClient(phone: String, name: String, password: String, promocode: Int) {
        val registrationData = RegistrationData(LocationRequest(listOf(0.0, 0.0)), name, password, phone, promocode)
        _message.addSourceDisposable(repository.register(registrationData)) {
            it.onData { userData ->
                sendNotificationToken(userData)
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource {
                    _message.value = MessageData.resource(it)
                }
            }
        }
    }

    private fun restorePassword(loginData: LoginData) {
        _message.addSourceDisposable(repository.restorePassword(loginData)) {
            it.onData { userData ->
                _registerResponse.value = userData
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource {
                    _message.value = MessageData.resource(it)
                }
            }
        }
    }

    fun resendSms(phone: String) {
        _message.addSourceDisposable(repository.sendVerificationCode(phone)) {
            it.onData { userData ->

            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource {
                    _message.value = MessageData.resource(it)
                }
            }
        }
    }

    private fun sendNotificationToken(userData: UserData) {
        _message.addSourceDisposable(repository.sendNotificationTokenToServer()) {
            it.onData {
                _registerResponse.value = userData
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }

}