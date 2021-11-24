package uz.star.mardex.ui.screen.working.home_fragment.workers_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.R
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 23-Mar-21
 **/

@HiltViewModel
class WorkersViewModel @Inject constructor(private val repository: WorkersRepository) : ViewModel() {
    private val _workers = MutableLiveData<List<WorkerData>>()
    val workers: LiveData<List<WorkerData>> get() = _workers

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    fun getWorkers(jobId: String) {
        _message.addSourceDisposable(repository.getWorkers(jobId)) {
            it.onData { list ->
                Log.d("T12T", "list = " + list)
                if (list.isNotEmpty())
                    _workers.value = list
                else
                    _message.value = MessageData.resource(R.string.no_workers)
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { message ->
                    _message.value = MessageData.resource(message)
                }
            }
        }
    }
}