package uz.star.mardex.ui.screen.working.home_fragment.send_request_workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.model.data_exchange.SocketRequestModel
import uz.star.mardex.model.data_exchange.SocketResponseData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.worker.WorkerData
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 15-Apr-21
 **/

@HiltViewModel
class SendWorkersRequestViewModel @Inject constructor(private val repository: SendWorkersRequestRepository) : ViewModel() {

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _workerResponse = MutableLiveData<SocketResponseData>()
    val workerResponse: LiveData<SocketResponseData> get() = _workerResponse

    private val _sendingPosition = MutableLiveData<Int>()
    val sendingPosition: LiveData<Int> get() = _sendingPosition

    private val _sendingSeconds = MutableLiveData<Int>()
    val sendingSeconds: LiveData<Int> get() = _sendingSeconds

    private val _finishSending = MutableLiveData<Int>()
    val finishSending: LiveData<Int> get() = _finishSending

    init {
        loadWorkersResponses()
        connectSockets()
    }

    fun sendVerification(workersList: List<WorkerData>, vacanyData: SocketRequestModel) {
        repository.sendNotificationToWorker(workersList, vacanyData)
    }

    private fun loadWorkersResponses() {
        _message.addSource(repository.responseFromWorkers()) {
            _workerResponse.value = it
        }
        _message.addSource(repository.sendingPosition()) {
            _sendingPosition.value = it
        }
        _message.addSource(repository.sendingSeconds()) {
            _sendingSeconds.value = it
        }
        _message.addSource(repository.finishSending()){
            _finishSending.value = it
        }
    }

    fun disconnectSocket() {
        repository.disconnectSocket()
    }

    fun connectSockets() {
        repository.connectSocket()
    }

    fun cancelSocketRequest() {
        repository.cancelSearchWorker()
    }

}