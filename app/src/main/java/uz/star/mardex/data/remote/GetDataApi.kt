package uz.star.mardex.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uz.star.mardex.model.requests.worker.GetWorkerBody
import uz.star.mardex.model.response.server.RateData
import uz.star.mardex.model.response.server.ResponseServer
import uz.star.mardex.model.response.server.intro.IntroData
import uz.star.mardex.model.response.server.jobs.JobCategory
import uz.star.mardex.model.response.server.worker.ConnectedWorkerData
import uz.star.mardex.model.response.server.worker.WorkerData

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

interface GetDataApi {

    @GET("categoryjobs/lang/{lang}")
    suspend fun getJobs(
        @Path("lang") lang: String = "uz"
    ): Response<ResponseServer<List<JobCategory>>>

    @POST("users/neartest/{latlng}")
    suspend fun getWorkers(
        @Path("latlng") latlng: String,
        @Body getWorkerBody: GetWorkerBody
    ): Response<ResponseServer<List<WorkerData>>>

    @GET("clients/users/{id}")
    suspend fun getConnectedWorkersList(
        @Path("id") clientId: String
    ): Response<ResponseServer<List<ConnectedWorkerData>>>

    @GET("comment")
    suspend fun getComments(): Response<ResponseServer<List<RateData>>>

    @GET("intros/lang/{lang}")
    suspend fun getIntro(
        @Path("lang") lang: String
    ): Response<ResponseServer<List<IntroData>>>
}