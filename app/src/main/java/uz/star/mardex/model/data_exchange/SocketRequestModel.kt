package uz.star.mardex.model.data_exchange

import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.response.server.title.Title
import java.io.Serializable

/**
 * Created by Farhod Tohirov on 28-Jan-21
 **/

data class SocketRequestModel(
    var workerId: String,
    var clientId: String,
    var jobName: Title,
    var jobDescription: String,
    var jobPrice: String,
    var jobDescriptionFull: String,
    var workerCount: Int,
    var location: LocationRequest,
    var userName: String,
    var userPhone: String,
    var images: List<String>,
    var jobId: String
) : Serializable
