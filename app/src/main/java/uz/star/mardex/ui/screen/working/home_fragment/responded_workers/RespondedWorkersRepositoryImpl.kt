package uz.star.mardex.ui.screen.working.home_fragment.responded_workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.GetDataApi
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.rate.RateDataComment
import uz.star.mardex.model.requests.worker.MakeFreeModel
import uz.star.mardex.model.requests.worker.worker.AddOrRemoveWorker
import uz.star.mardex.model.requests.worker.worker.RateDataRequest
import uz.star.mardex.model.response.FinishLastOrder
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.RateData
import uz.star.mardex.model.response.server.ResponseServer
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 22-Apr-21
 **/

class RespondedWorkersRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi,
    private val localStorage: LocalStorage,
    private val getDataApi: GetDataApi
) : RespondedWorkersRepository, SafeApiRequest() {

    override fun addWorkerToClientData(workerData: WorkerData): LiveData<ResultData<ResponseServer<Any>>> {
        val resultLiveData = MutableLiveData<ResultData<ResponseServer<Any>>>()

        val request = AddOrRemoveWorker(localStorage.id, "add", workerData.id)
        Coroutines.ioThenMain(
            { apiRequest { api.addOrRemoveWorker(request) } },
            { response ->
                response?.onData { data ->
                    if (data.success)
                        resultLiveData.value = ResultData.data(data)
                    else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )

        return resultLiveData
    }

    override fun getCommentsData(): LiveData<ResultData<List<RateData>>> {
        val resultLiveData = MutableLiveData<ResultData<List<RateData>>>()
        Coroutines.ioThenMain(
            { apiRequest { getDataApi.getComments() } },
            { response ->
                response?.onData { responseServer ->
                    if (responseServer.success) {
                        resultLiveData.value = ResultData.data(responseServer.data)
                    } else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                    }
                }
            }
        )

        return resultLiveData
    }

    override fun removeWorkerFromClient(workerData: WorkerData, status: String): LiveData<ResultData<ResponseServer<Any>>> {
        val resultLiveData = MutableLiveData<ResultData<ResponseServer<Any>>>()

        val request = AddOrRemoveWorker(localStorage.id, "remove", workerData.id)
        Coroutines.ioThenMain(
            { apiRequest { api.addOrRemoveWorker(request) } },
            { response ->
                response?.onData { data ->
                    Coroutines.ioThenMain(
                        { apiRequest { api.makeWorkerFree(MakeFreeModel(workerData.id)) } },
                        { isDeleted ->
                            isDeleted?.onData {
                                if (it.success) {
                                    if (data.success) {
                                        resultLiveData.value = ResultData.data(data)
                                        getWorkerOrders(workerData.id, status)
                                    } else
                                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                                }
                            }?.onMessageData { messageData ->
                                resultLiveData.value = ResultData.messageData(messageData)
                            }
                        }
                    )
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )

        return resultLiveData
    }

    override fun cancelWorker(workerData: WorkerData) {

    }

    private fun getWorkerOrders(workerId: String, status: String) {
        Coroutines.ioThenMain(
            { apiRequest { api.getWorkersOrders(workerId) } },
            { response ->
                response?.onData { data ->
                    if (data.success) {
                        val order = data.data.last()
                        finishLastOrder(orderId = order.id ?: "", status)
                    }
                }
            }
        )
    }

    private fun finishLastOrder(orderId: String, status: String) {
        Coroutines.ioThenMain(
            { apiRequest { api.removeLastOrder(orderId, FinishLastOrder(true, status)) } },
            { response ->
                response?.onData {

                }
            }
        )
    }

    override fun rateUser(userId: String, rateDataComment: RateDataComment): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()
        val rateData = RateDataRequest(userId, localStorage.id, rateDataComment.rate.mark.toFloat())
        Coroutines.ioThenMain(
            { apiRequest { api.rateUser(rateData) } },
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

    override fun rateComment(rateDataComment: RateDataComment): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()
        Coroutines.ioThenMain(
            { apiRequest { api.rateWorkerWithComment(rateDataComment) } },
            { response ->
                response?.onData {
                    resultLiveData.value = ResultData.data(true)
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }
}