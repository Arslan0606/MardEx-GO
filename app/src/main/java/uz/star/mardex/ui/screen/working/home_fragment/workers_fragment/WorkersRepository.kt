package uz.star.mardex.ui.screen.working.home_fragment.workers_fragment

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.worker.WorkerData

/**
 * Created by Farhod Tohirov on 23-Mar-21
 **/

interface WorkersRepository {
    fun getWorkers(jobId: String): LiveData<ResultData<List<WorkerData>>>
}