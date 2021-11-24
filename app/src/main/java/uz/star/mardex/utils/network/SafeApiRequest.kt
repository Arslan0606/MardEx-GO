package uz.star.mardex.utils.network

import android.util.Log
import retrofit2.Response
import uz.star.mardex.R
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData


/**
 * Created by Farhod Tohirov on 13-Feb-21
 **/

abstract class SafeApiRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): ResultData<T> {
        return try {
            val response = call.invoke()

            if (response.code() == 401) return ResultData.messageData(MessageData.resource(R.string.incorrent_passwort))

            if (response.code() in 400..500) {
                return ResultData.messageData(MessageData.resource(R.string.failure))
            }

            if (response.code() == 501) {
                return ResultData.messageData(MessageData.resource(R.string.code_incorrect))
            }

            if (response.code() == 502) {
                return ResultData.messageData(MessageData.resource(R.string.sms_is_not_working))
            }

            if (response.code() >= 500) {
                return ResultData.messageData(MessageData.resource(R.string.try_after_a_while))
            }

            if (response.body() == null)
                ResultData.messageData(MessageData.resource(R.string.failure))
            else ResultData.data(response.body()!!)


        } catch (e: Exception) {
            when (e) {
                is java.net.ConnectException -> ResultData.messageData(MessageData.resource(R.string.turn_on_internet))
                is java.net.SocketTimeoutException -> ResultData.messageData(MessageData.resource(R.string.internet_slow))
                is java.net.UnknownHostException -> ResultData.messageData(MessageData.resource(R.string.turn_on_internet))
                else -> ResultData.messageData(MessageData.resource(R.string.failure))
            }
        }
    }
}