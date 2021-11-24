package uz.star.mardex.ui.screen.working.home_fragment.responded_workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.model.requests.rate.RateDataComment
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.RateData
import uz.star.mardex.model.response.server.ResponseServer
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 22-Apr-21
 **/

@HiltViewModel
class RespondedWorkersViewModel @Inject constructor(private val repository: RespondedWorkersRepository) : ViewModel() {

    private val _connectWorker = MediatorLiveData<Unit>()
    val connectWorker: LiveData<Unit> get() = _connectWorker

    private val _commentsData = MutableLiveData<List<RateData>>()
    val commentsData: LiveData<List<RateData>> get() = _commentsData

    private val _cancellationComments = MutableLiveData<Any>()
    val cancellationComments: LiveData<Any> get() = _cancellationComments

    private val _removeWorker = MutableLiveData<WorkerData>()
    val removeWorker: LiveData<WorkerData> get() = _removeWorker

    private val _message = MutableLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    fun connectWorkerToClient(workerData: WorkerData) {
        _connectWorker.addSourceDisposable(repository.addWorkerToClientData(workerData)) {
            it.onData { _ ->
                _connectWorker.value = Unit
            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }

    fun getCommentsData() {
        _connectWorker.addSourceDisposable(repository.getCommentsData()) {
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
        _connectWorker.addSourceDisposable(repository.rateUser(workerData.id, rateDataComment)) {
            it.onData { rated ->
                if (rated)
                    _connectWorker.addSourceDisposable(repository.rateComment(rateDataComment)) { result ->
                        result.onData {
                            _connectWorker.addSourceDisposable(repository.removeWorkerFromClient(workerData, "success")) { result ->
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
        _connectWorker.addSourceDisposable(repository.removeWorkerFromClient(workerData, "cancel_client")) { result ->
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