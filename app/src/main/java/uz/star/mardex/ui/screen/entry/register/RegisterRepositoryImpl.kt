package uz.star.mardex.ui.screen.entry.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.registration.CheckExistData
import uz.star.mardex.model.requests.registration.PhoneData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.regions.CityResponse
import uz.star.mardex.model.response.regions.RegionResponse
import uz.star.mardex.model.response.server.ResponseServer
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

class RegisterRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi,
) : RegisterRepository, SafeApiRequest() {

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

    override fun checkExist(checkExistData: CheckExistData): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()

        Coroutines.ioThenMain(
            { apiRequest { api.checkExist(checkExistData) } },
            { response ->
                response?.onData { data ->
                    if (data.success)
                        resultLiveData.value = ResultData.data(true)
                    else
                        resultLiveData.value = ResultData.data(false)
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }

    override fun getCities(): LiveData<ResultData<ResponseServer<List<CityResponse>>>> {
        val resultLiveData = MutableLiveData<ResultData<ResponseServer<List<CityResponse>>>>()

        Coroutines.ioThenMain(
            { apiRequest { api.getCities() } },
            { response ->
                response?.onData { data ->
                    resultLiveData.value = ResultData.data(data)
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }

    override fun getRegions(): LiveData<ResultData<ResponseServer<List<RegionResponse>>>> {
        val resultLiveData = MutableLiveData<ResultData<ResponseServer<List<RegionResponse>>>>()

        Coroutines.ioThenMain(
            { apiRequest { api.getRegions() } },
            { response ->
                response?.onData { data ->
                    resultLiveData.value = ResultData.data(data)
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }
}