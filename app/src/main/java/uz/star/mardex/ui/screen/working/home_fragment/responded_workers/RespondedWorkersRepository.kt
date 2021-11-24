package uz.star.mardex.ui.screen.working.home_fragment.responded_workers

import androidx.lifecycle.LiveData
import uz.star.mardex.model.requests.rate.RateDataComment
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.RateData
import uz.star.mardex.model.response.server.ResponseServer
import uz.star.mardex.model.response.server.worker.WorkerData

/**
 * Created by Farhod Tohirov on 22-Apr-21
 **/

interface RespondedWorkersRepository {
    fun addWorkerToClientData(workerData: WorkerData): LiveData<ResultData<ResponseServer<Any>>>
    fun getCommentsData(): LiveData<ResultData<List<RateData>>>

    fun removeWorkerFromClient(workerData: WorkerData, status: String): LiveData<ResultData<ResponseServer<Any>>>
    fun cancelWorker(workerData: WorkerData)
    fun rateUser(userId: String, rateDataComment: RateDataComment): LiveData<ResultData<Boolean>>
    fun rateComment(rateDataComment: RateDataComment): LiveData<ResultData<Boolean>>
}