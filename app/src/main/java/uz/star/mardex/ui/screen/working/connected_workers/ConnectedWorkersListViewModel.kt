package uz.star.mardex.ui.screen.working.connected_workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.R
import uz.star.mardex.model.requests.rate.RateDataComment
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.RateData
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 02-Feb-21
 **/

@HiltViewModel
class ConnectedWorkersListViewModel @Inject constructor(
    private val repository: ConnectedWorkersListRepository
) : ViewModel() {

    private val _workers = MediatorLiveData<List<WorkerData>>()
    val workers: LiveData<List<WorkerData>> get() = _workers

    private val _message = MutableLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _commentsData = MutableLiveData<List<RateData>>()
    val commentsData: LiveData<List<RateData>> get() = _commentsData

    private val _cancellationComments = MutableLiveData<Any>()
    val cancellationComments: LiveData<Any> get() = _cancellationComments

    private val _removeWorker = MutableLiveData<WorkerData>()
    val removeWorker: LiveData<WorkerData> get() = _removeWorker

    fun getConnectedWorkersList() {
        _workers.addSourceDisposable(repository.getConnectedWorkers()) {
            it.onData { list ->
                if (list.isNotEmpty())
                    _workers.value = list
                else
                    _workers.value = emptyList()
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { message ->
                    _message.value = MessageData.resource(message)
                }
            }
        }
    }

    fun getCommentsData() {
        _workers.addSourceDisposable(repository.getCommentsData()) {
            it.onData { list ->
                _commentsData.value = list
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { message ->
                    _message.value = MessageData.resource(message)
                }
            }
        }
    }

    fun rateWorkerEndTask(rateDataComment: RateDataComment, workerData: WorkerData) {
        _workers.addSourceDisposable(repository.rateUser(workerData.id, rateDataComment)) {
            it.onData { rated ->
                if (rated)
                    _workers.addSourceDisposable(repository.rateComment(rateDataComment)) { result ->
                        result.onData {
                            _workers.addSourceDisposable(repository.removeWorkerFromClient(workerData, "success")) { result ->
                                result.onData { _ ->
                                    _removeWorker.value = workerData
                                }.onMessageData { messageData ->
                                    _message.value = messageData
                                }
                            }
                        }.onMessageData { message ->
                            _message.value = message
                        }
                    }

            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }

    fun cancelWorkerData(workerData: WorkerData) {
        _workers.addSourceDisposable(repository.removeWorkerFromClient(workerData, "cancel_client")) { result ->
            result.onData { _ ->
                _removeWorker.value = workerData
            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }

    fun getCancellationComments(){
        _cancellationComments.value = Any()
    }

}