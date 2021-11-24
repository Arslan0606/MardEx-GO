package uz.star.mardex.ui.screen.working.home_fragment.promocode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.promocode.PromocodeReequestData
import uz.star.mardex.utils.helpers.PARTICIPATED
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 27-Jun-21
 **/

class PromocodeRepositoryImpl @Inject constructor(
    private val authApi: ProfileDataApi
) : PromocodeRepository, SafeApiRequest() {

    override fun sendPromocode(promocodeRequestData: PromocodeReequestData): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()

        Coroutines.ioThenMain(
            { apiRequest { authApi.sendPromocode(promocodeRequestData) } },
            { data ->
                data?.onData {
                    if (it.success) {
                        if (it.data == PARTICIPATED) {
                            resultLiveData.value = ResultData.resource(R.string.promocode_used)
                        } else {
                            resultLiveData.value = ResultData.data(true)
                        }
                    } else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.error_promocode))
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