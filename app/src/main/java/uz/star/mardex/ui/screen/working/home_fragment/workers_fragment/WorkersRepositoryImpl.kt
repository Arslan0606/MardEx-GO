package uz.star.mardex.ui.screen.working.home_fragment.workers_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.GetDataApi
import uz.star.mardex.model.requests.worker.GetWorkerBody
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 23-Mar-21
 **/

class WorkersRepositoryImpl @Inject constructor(
    private val getDataApi: GetDataApi,
    private val localStorage: LocalStorage
) : WorkersRepository, SafeApiRequest() {


    override fun getWorkers(jobId: String): LiveData<ResultData<List<WorkerData>>> {
        var radius = 10
        val resultLiveData = MutableLiveData<ResultData<List<WorkerData>>>()
        Coroutines.ioThenMain({
            apiRequest {
                getDataApi.getWorkers(
                    "${localStorage.currentLat},${localStorage.currentLong}",
                    GetWorkerBody(radius.toString(), jobId)
                )
            }
        },
            { response ->
                response?.onData {
                    if (it.success) {
                        if (it.data.isEmpty() && radius < 30) {
                            radius += 5
                            Coroutines.ioThenMain({
                                apiRequest {
                                    getDataApi.getWorkers(
                                        "${localStorage.currentLat},${localStorage.currentLong}",
                                        GetWorkerBody(radius.toString(), jobId)
                                    )
                                }
                            },
                                { response ->
                                    response?.onData {
                                        if (it.success) {
                                            if (it.data.isEmpty() && radius < 30) {
                                                radius += 5
                                                Coroutines.ioThenMain({
                                                    apiRequest {
                                                        getDataApi.getWorkers(
                                                            "${localStorage.currentLat},${localStorage.currentLong}",
                                                            GetWorkerBody(radius.toString(), jobId)
                                                        )
                                                    }
                                                },
                                                    { response ->
                                                        response?.onData {
                                                            if (it.success) {
                                                                if (it.data.isEmpty() && radius < 30) {
                                                                    radius += 5
                                                                    Coroutines.ioThenMain({
                                                                        apiRequest {
                                                                            getDataApi.getWorkers(
                                                                                "${localStorage.currentLat},${localStorage.currentLong}",
                                                                                GetWorkerBody(radius.toString(), jobId)
                                                                            )
                                                                        }
                                                                    },
                                                                        { response ->
                                                                            response?.onData {
                                                                                if (it.success) {
                                                                                    if (it.data.isEmpty() && radius < 30) {
                                                                                        radius += 5
                                                                                        Coroutines.ioThenMain({
                                                                                            apiRequest {
                                                                                                getDataApi.getWorkers(
                                                                                                    "${localStorage.currentLat},${localStorage.currentLong}",
                                                                                                    GetWorkerBody(radius.toString(), jobId)
                                                                                                )
                                                                                            }
                                                                                        },
                                                                                            { response ->
                                                                                                response?.onData {
                                                                                                    if (it.success) {
                                                                                                        if (it.data.isEmpty() && radius < 30) {
                                                                                                            radius += 5
                                                                                                            resultLiveData.value =
                                                                                                                ResultData.data(emptyList())
                                                                                                        } else {
                                                                                                            resultLiveData.value =
                                                                                                                ResultData.data(it.data)
                                                                                                        }
                                                                                                    } else {
                                                                                                        resultLiveData.value = ResultData.messageData(
                                                                                                            MessageData.resource(R.string.failure)
                                                                                                        )
                                                                                                    }
                                                                                                }?.onMessage { message ->
                                                                                                    resultLiveData.value = ResultData.message(message)
                                                                                                }?.onMessageData { messageData ->
                                                                                                    messageData.onResource {
                                                                                                        resultLiveData.value = ResultData.messageData(
                                                                                                            MessageData.resource(it)
                                                                                                        )
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        )
                                                                                    } else {
                                                                                        resultLiveData.value = ResultData.data(it.data)
                                                                                    }
                                                                                } else {
                                                                                    resultLiveData.value =
                                                                                        ResultData.messageData(MessageData.resource(R.string.failure))
                                                                                }
                                                                            }?.onMessage { message ->
                                                                                resultLiveData.value = ResultData.message(message)
                                                                            }?.onMessageData { messageData ->
                                                                                messageData.onResource {
                                                                                    resultLiveData.value =
                                                                                        ResultData.messageData(MessageData.resource(it))
                                                                                }
                                                                            }
                                                                        }
                                                                    )
                                                                } else {
                                                                    resultLiveData.value = ResultData.data(it.data)
                                                                }
                                                            } else {
                                                                resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                                                            }
                                                        }?.onMessage { message ->
                                                            resultLiveData.value = ResultData.message(message)
                                                        }?.onMessageData { messageData ->
                                                            messageData.onResource {
                                                                resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                                                            }
                                                        }
                                                    }
                                                )
                                            } else {
                                                resultLiveData.value = ResultData.data(it.data)
                                            }
                                        } else {
                                            resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                                        }
                                    }?.onMessage { message ->
                                        resultLiveData.value = ResultData.message(message)
                                    }?.onMessageData { messageData ->
                                        messageData.onResource {
                                            resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                                        }
                                    }
                                }
                            )
                        } else {
                            resultLiveData.value = ResultData.data(it.data)
                        }
                    } else {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(R.string.failure))
                    }
                }?.onMessage { message ->
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

}