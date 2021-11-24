package uz.star.mardex.ui.screen.working.home_fragment.send_request_workers

import androidx.lifecycle.LiveData
import uz.star.mardex.model.data_exchange.SocketRequestModel
import uz.star.mardex.model.data_exchange.SocketResponseData
import uz.star.mardex.model.response.server.worker.WorkerData

/**
 * Created by Farhod Tohirov on 15-Apr-21
 **/

interface SendWorkersRequestRepository {
    fun sendNotificationToWorker(workerList: List<WorkerData>, vacanyData: SocketRequestModel)
    fun responseFromWorkers(): LiveData<SocketResponseData>
    fun sendingPosition(): LiveData<Int>
    fun sendingSeconds(): LiveData<Int>
    fun finishSending(): LiveData<Int>
    fun cancelSearchWorker()
    fun connectSocket()
    fun disconnectSocket()
}