package uz.star.mardex.ui.screen.working.connected_workers

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
 * Created by Farhod Tohirov on 02-Feb-21
 **/

class ConnectedWorkersListRepositoryImpl @Inject constructor(
    private val api: GetDataApi,
    private val profileDataApi: ProfileDataApi,
    private val localStorage: LocalStorage
) : ConnectedWorkersListRepository, SafeApiRequest() {

    override fun getConnectedWorkers(): LiveData<ResultData<List<WorkerData>>> {
        val resultLiveData = MutableLiveData<ResultData<List<WorkerData>>>()

        Coroutines.ioThenMain(
            { apiRequest { api.getConnectedWorkersList(localStorage.id) } },
            { response ->
                response?.onData { responseServer ->
                    if (responseServer.success) {
                        val t = responseServer.data
                        val list = ArrayList<WorkerData>()
                        for (worker in t) {
                            list.add(
                                WorkerData(
                                    worker.avatar ?: "", worker.balance, worker.createdAt, worker.description, worker.distance,
                                    worker.fullName, worker.id, worker.images, worker.isFree, listOf(), worker.location, worker.packet,
                                    worker.password, worker.paymentId, worker.payments, worker.phone, worker.v, worker.allJobs,
                                    worker.sumMark
                                )
                            )
                        }
                        resultLiveData.value = ResultData.data(list)
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

    override fun getCommentsData(): LiveData<ResultData<List<RateData>>> {
        val resultLiveData = MutableLiveData<ResultData<List<RateData>>>()
        Coroutines.ioThenMain(
            { apiRequest { api.getComments() } },
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
            { apiRequest { profileDataApi.addOrRemoveWorker(request) } },
            { response ->
                response?.onData { data ->
                    Coroutines.ioThenMain(
                        { apiRequest { profileDataApi.makeWorkerFree(MakeFreeModel(workerData.id)) } },
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

    override fun rateUser(userId: String, rateDataComment: RateDataComment): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()
        val rateData = RateDataRequest(userId, localStorage.id, rateDataComment.rate.mark.toFloat())
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.rateUser(rateData) } },
            { response ->
                response?.onData { data ->
                    if (data.success) {
                        resultLiveData.value = ResultData.data(true)
                    } else
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                }?.onMessageData { messageData ->
                    resultLiveData.value = ResultData.messageData(messageData)
                }
            }
        )
        return resultLiveData
    }

    private fun getWorkerOrders(workerId: String, status: String) {
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.getWorkersOrders(workerId) } },
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
            { apiRequest { profileDataApi.removeLastOrder(orderId, FinishLastOrder(true, status)) } },
            { response ->
                response?.onData {

                }
            }
        )
    }

    override fun rateComment(rateDataComment: RateDataComment): LiveData<ResultData<Boolean>> {
        val resultLiveData = MutableLiveData<ResultData<Boolean>>()
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.rateWorkerWithComment(rateDataComment) } },
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