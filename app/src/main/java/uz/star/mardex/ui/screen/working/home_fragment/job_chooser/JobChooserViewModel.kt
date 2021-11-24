package uz.star.mardex.ui.screen.working.home_fragment.job_chooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.jobs.JobCategory
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

@HiltViewModel
class JobChooserViewModel @Inject constructor(private val repository: JobChooserRepository) : ViewModel() {

    private val _jobs = MutableLiveData<List<JobCategory>>()
    val jobs: LiveData<List<JobCategory>> get() = _jobs

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    fun getJobs() {
        _message.addSourceDisposable(repository.getJobs()) {
            it.onData { list ->
                _jobs.value = list
            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }
}