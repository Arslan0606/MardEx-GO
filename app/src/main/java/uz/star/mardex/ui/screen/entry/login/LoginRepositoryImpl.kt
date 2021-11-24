package uz.star.mardex.ui.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.ui.screen.entry.login.LoginRepository
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 12-Jan-21
 **/

class LoginRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi,
    private val localStorage: LocalStorage
) : LoginRepository, SafeApiRequest() {

    override fun login(loginData: LoginData): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()

        Coroutines.ioThenMain(
            { apiRequest { api.login(loginData) } },
            { response ->
                response?.onData {
                    if (it.success) {
                        localStorage.id = it.data.id
                        localStorage.name = it.data.name
                        localStorage.phone = it.data.phone
                        localStorage.registrated = true
                        resultLiveData.value = ResultData.data(it.data)
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