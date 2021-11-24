package uz.star.mardex.ui.screen.entry.code_verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.requests.registration.PhoneData
import uz.star.mardex.model.requests.registration.RegistrationData
import uz.star.mardex.model.requests.registration.VerifyData
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

class SmsVerificationRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi,
    private val localStorage: LocalStorage
) : SmsVerificationRepository, SafeApiRequest() {

    override fun verifyPhone(phone: String, code: String): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()

        Coroutines.ioThenMain(
            { apiRequest { api.checkSmsCode(VerifyData(code, phone)) } },
            { response ->
                response?.onData {
                    if (it.success) {
                        resultLiveData.value = ResultData.data(true)
                    } else
                        resultLiveData.value = ResultData.message("ERROR")
                }
                response?.onMessage { message ->
                    resultLiveData.value = ResultData.message(message)
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                    }
                }
            }
        )

        return resultLiveData
    }

    override fun register(registrationData: RegistrationData): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()

        Coroutines.ioThenMain(
            { apiRequest { api.registerClient(registrationData) } },
            { response ->
                response?.onData {
                    if (it.success) {
                        resultLiveData.value = ResultData.data(it.data)
                        localStorage.id = it.data.id
                        localStorage.name = it.data.name
                        localStorage.phone = it.data.phone
                        localStorage.registrated = true
                    } else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessage { message ->
                    resultLiveData.value = ResultData.message(message)
                }?.onMessageData { messageData ->
                    messageData.onResource { resultLiveData.value = ResultData.messageData(MessageData.resource(it)) }
                }
            }
        )
        return resultLiveData
    }

    override fun sendVerificationCode(phone: String): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()

        Coroutines.ioThenMain(
            { apiRequest { api.sendSmsCode(PhoneData(phone)) } },
            { response ->
                response?.onData { data ->
                    if (data.success)
                        resultLiveData.value = ResultData.data(true)
                    else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }

    override fun restorePassword(login: LoginData): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()

        Coroutines.ioThenMain(
            { apiRequest { api.resetUserPassword(login) } },
            { response ->
                response?.onData {
                    if (it.success) {
                        resultLiveData.value = ResultData.data(it.data)
                        localStorage.id = it.data.id
                        localStorage.name = it.data.name
                        localStorage.phone = it.data.phone
                        localStorage.registrated = true
                    } else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessage { message ->
                    resultLiveData.value = ResultData.message(message)
                }?.onMessageData { messageData ->
                    messageData.onResource { resultLiveData.value = ResultData.messageData(MessageData.resource(it)) }
                }
            }
        )
        return resultLiveData
    }

    override fun sendNotificationTokenToServer(): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()
        Coroutines.ioThenMain(
            { apiRequest { api.updatePersonalData(localStorage.id, EditedPersonalData(fcm_token = localStorage.notificationToken)) } },
            { response ->
                response?.onData {
                    if (it.success) {
                        resultLiveData.value = ResultData.data(it.data)
                    } else
                        resultLiveData.value = ResultData.message("list bosh")
                }
                response?.onMessage {
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