package uz.star.mardex.ui.screen.entry.reset_passpord

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.registration.PhoneData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Kurganbaev Jasurbek on 28.04.2021 11:08
 **/
class RestorePasswordRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi,
) : RestorePasswordRepository, SafeApiRequest() {


    override fun sendVerificationCode(phone: String): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()

        Coroutines.ioThenMain(
            { apiRequest { api.sendSmsCode(PhoneData(phone)) } },
            { response ->
                response?.onData { data ->
                    if (data.success)
                        resultLiveData.value = ResultData.data(true)
                    else
                        resultLiveData.value =
                            ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }


}