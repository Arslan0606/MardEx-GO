package uz.star.mardex.ui.screen.working.home_fragment.job_chooser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.GetDataApi
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.jobs.JobCategory
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

class JobChooserRepositoryImpl @Inject constructor(
    private val api: GetDataApi,
    private val localStorage: LocalStorage
) : JobChooserRepository, SafeApiRequest() {

    override fun getJobs(): LiveData<ResultData<List<JobCategory>>> {
        val resultLiveData = MutableLiveData<ResultData<List<JobCategory>>>()

        Coroutines.ioThenMain(
            { apiRequest { api.getJobs() } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(it.data)
                    else
                        resultLiveData.value = ResultData.message("list bo`sh")
                }
                data?.onMessage {
                    resultLiveData.value = ResultData.message(it)
                }?.onMessageData { messageData ->
                    messageData.onResource { resultLiveData.value = ResultData.messageData(MessageData.resource(it)) }
                }
            }
        )
        return resultLiveData
    }
}