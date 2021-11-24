package uz.star.mardex.ui.screen.working.home_fragment.send_request_workers

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.data_exchange.PushNotificationData
import uz.star.mardex.model.data_exchange.SocketRequestModel
import uz.star.mardex.model.data_exchange.SocketResponseData
import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.extension.title
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 15-Apr-21
 **/

class SendWorkersRequestRepositoryImpl @Inject constructor(
    private val socket: Socket,
    private val localStorage: LocalStorage,
    private val profileDataApi: ProfileDataApi
) : SendWorkersRequestRepository, SafeApiRequest() {

    private var workerResponse: MutableLiveData<SocketResponseData>? = null
    private var sendingPosition: MutableLiveData<Int>? = null
    private var sendingSeconds: MutableLiveData<Int>? = null
    private var finishSending: MutableLiveData<Int>? = null
    private lateinit var workers: List<WorkerData>
    private var requestPos = 0
    private var totalRequestCount = -1
    private var workerCount = 0
    private lateinit var vacanyData: SocketRequestModel
    private var timer: CountDownTimer

    init {
        timer = object : CountDownTimer(25_000, 1_000) {
            override fun onTick(p0: Long) {
                val second = (p0 / 1000).toInt()
                sendingSeconds?.value = second
            }

            override fun onFinish() {
                requestPos++
                if (workerCount != totalRequestCount) {
                    if (requestPos != workers.size)
                        sendRequestToWorker(workers[requestPos])
                    else finishSending?.postValue(workerCount)
                } else {
                    finishSending?.postValue(workerCount)
                }
            }

        }
    }

    override fun connectSocket() {
        socket.connect()
        socket.on("cancel worker request", workerCancelResponseListener)
    }

    override fun sendNotificationToWorker(workerList: List<WorkerData>, vacanyData: SocketRequestModel) {
        workers = workerList
        totalRequestCount = vacanyData.workerCount
        this.vacanyData = vacanyData
        if (requestPos != workers.size)
            sendRequestToWorker(workers[requestPos])
    }

    private fun sendRequestToWorker(workerData: WorkerData) {
        sendingPosition?.postValue(requestPos)
        val socketRequestModel = SocketRequestModel(
            workerData.id,
            localStorage.id,
            vacanyData.jobName,
            vacanyData.jobDescription,
            vacanyData.jobPrice,
            vacanyData.jobDescriptionFull,
            vacanyData.workerCount,
            LocationRequest(listOf(localStorage.currentLat, localStorage.currentLong)),
            localStorage.name,
            localStorage.phone,
            vacanyData.images,
            vacanyData.jobId
        )
        timer.start()
        val request = Gson().toJson(socketRequestModel)
        socket.emit("client request", request)
        sendNotificationByFirebase(socketRequestModel)
    }

    private val workerResponseListener = Emitter.Listener {
        val res = Gson().fromJson(it[0].toString(), SocketResponseData::class.java)
        if (res.userId == localStorage.id) {
            timer.cancel()
            workerResponse?.postValue(res)
            requestPos++
            workerCount++
            if (workerCount != totalRequestCount) {
                if (requestPos < workers.size)
                    sendRequestToWorker(workers[requestPos])
                else finishSending?.postValue(workerCount)
            } else {
                finishSending?.postValue(workerCount)
            }
        }
    }

    override fun responseFromWorkers(): LiveData<SocketResponseData> {
        socket.on("worker response", workerResponseListener)
        if (workerResponse == null) {
            workerResponse = MutableLiveData<SocketResponseData>()
        }
        return workerResponse!!
    }

    private val workerCancelResponseListener = Emitter.Listener {
        val res = Gson().fromJson(it[0].toString(), SocketResponseData::class.java)
        if (res.userId == localStorage.id) {
            timer.cancel()
            requestPos++
            if (workerCount != totalRequestCount) {
                if (requestPos < workers.size)
                    sendRequestToWorker(workers[requestPos])
                else finishSending?.postValue(workerCount)
            } else {
                finishSending?.postValue(workerCount)
            }
        }
    }

    override fun sendingPosition(): LiveData<Int> {
        if (sendingPosition == null) {
            sendingPosition = MutableLiveData<Int>()
        }
        return sendingPosition!!
    }

    override fun sendingSeconds(): LiveData<Int> {
        if (sendingSeconds == null) {
            sendingSeconds = MutableLiveData<Int>()
        }
        return sendingSeconds!!
    }

    override fun finishSending(): LiveData<Int> {
        if (finishSending == null) {
            finishSending = MutableLiveData<Int>()
        }
        return finishSending!!
    }

    override fun cancelSearchWorker() {
        socket.emit("cancel client request", localStorage.id)
    }

    override fun disconnectSocket() {
        socket.off("worker response")
        workerResponse = null
    }

    private fun sendNotificationByFirebase(socketRequestModel: SocketRequestModel) {
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.pushNotification(socketRequestModel) } },
            { data ->
                data?.onData {
                    if (it.success) sendNotificationToPhone(socketRequestModel)
                }
                data?.onMessage {

                }?.onMessageData { messageData ->

                }
            }
        )
    }

    private fun sendNotificationToPhone(socketRequestModel: SocketRequestModel) {
        Coroutines.ioThenMain(
            {
                apiRequest {
                    profileDataApi.pushNotificationToPhone(
                        PushNotificationData(
                            socketRequestModel.workerId,
                            socketRequestModel.jobDescription,
                            socketRequestModel.jobName.title() ?: ""
                        )
                    )
                }
            },
            {

            }
        )
    }
}