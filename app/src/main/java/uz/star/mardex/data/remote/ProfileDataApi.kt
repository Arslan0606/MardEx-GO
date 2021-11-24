package uz.star.mardex.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import uz.star.mardex.model.data_exchange.PushNotificationData
import uz.star.mardex.model.data_exchange.SocketRequestModel
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.requests.rate.RateDataComment
import uz.star.mardex.model.requests.registration.CheckExistData
import uz.star.mardex.model.requests.registration.PhoneData
import uz.star.mardex.model.requests.registration.RegistrationData
import uz.star.mardex.model.requests.registration.VerifyData
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.requests.updateuser.Pic
import uz.star.mardex.model.requests.updateuser.UpdatePasswordData
import uz.star.mardex.model.requests.worker.MakeFreeModel
import uz.star.mardex.model.requests.worker.worker.AddOrRemoveWorker
import uz.star.mardex.model.requests.worker.worker.RateDataRequest
import uz.star.mardex.model.response.FinishLastOrder
import uz.star.mardex.model.response.GetOrdersResponse
import uz.star.mardex.model.response.notification_for_user.IsRead
import uz.star.mardex.model.response.promocode.PromocodeReequestData
import uz.star.mardex.model.response.regions.CityResponse
import uz.star.mardex.model.response.regions.RegionResponse
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.model.response.server.ResponseServer
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardexworker.model.response.notification_for_user.OwnNotificationResponse

/**
 * Created by Farhod Tohirov on 08-Apr-21
 **/

interface ProfileDataApi {
    @Multipart
    @POST("users/upload")
    suspend fun uploadImage(@Part part: MultipartBody.Part): Response<ImagePath>

    @POST("clients/login")
    suspend fun login(@Body loginData: LoginData): Response<ResponseServer<UserData>>

    @POST("smsverify/receive")
    suspend fun sendSmsCode(
        @Body phoneData: PhoneData
    ): Response<ResponseServer<Any>>

    @POST("smsverify/check")
    suspend fun checkSmsCode(
        @Body verifyData: VerifyData
    ): Response<ResponseServer<Any>>

    @POST("clients")
    suspend fun registerClient(@Body registrationData: RegistrationData): Response<ResponseServer<UserData>>

    @GET("clients/{id}")
    suspend fun getUserData(@Path("id") clientId: String): Response<ResponseServer<UserData>>

    @PATCH("clients/users")
    suspend fun addOrRemoveWorker(
        @Body addOrRemoveWorker: AddOrRemoveWorker
    ): Response<ResponseServer<Any>>

    @POST("clients/userFree")
    suspend fun makeWorkerFree(
        @Body workerData: MakeFreeModel
    ): Response<ResponseServer<Any>>

    @POST("clients/rateUser")
    suspend fun rateUser(
        @Body rateDataRequest: RateDataRequest
    ): Response<ResponseServer<Any>>

    @PATCH("clients/{id}")
    suspend fun updatePersonalData(
        @Path("id") id: String,
        @Body() editedPersonalData: EditedPersonalData
    ): Response<ResponseServer<UserData>>

    @PATCH("clients/{id}")
    suspend fun updatePicImage(
        @Path("id") id: String,
        @Body pic: Pic
    ): Response<ResponseServer<UserData>>

    @POST("clients/clientUpdatePassword/{id}")
    suspend fun updatePassword(
        @Path("id") id: String,
        @Body() updatePasswordData: UpdatePasswordData
    ): Response<ResponseServer<UserData>>

    @POST("rate")
    suspend fun rateWorkerWithComment(@Body rateDataComment: RateDataComment): Response<Any>

    @POST("clients/resetClientPassword")
    suspend fun resetUserPassword(
        @Body loginData: LoginData
    ): Response<ResponseServer<UserData>>

    @POST("admin/checkExsist")
    suspend fun checkExist(@Body checkExistData: CheckExistData): Response<ResponseServer<Any>>

    @POST("pushnotification")
    suspend fun pushNotification(@Body pushNotificationData: SocketRequestModel): Response<ResponseServer<Any>>

    @POST("pushnotification/send")
    suspend fun pushNotificationToPhone(@Body pushNotificationData: PushNotificationData): Response<Any>

    @GET("order/{id}")
    suspend fun getWorkersOrders(@Path("id") id: String): Response<ResponseServer<List<GetOrdersResponse>>>

    @PATCH("order/{id}")
    suspend fun removeLastOrder(@Path("id") id: String, @Body finishLastOrder: FinishLastOrder): Response<Any>

    @GET("city")
    suspend fun getRegions(): Response<ResponseServer<List<RegionResponse>>>

    @GET("region")
    suspend fun getCities(): Response<ResponseServer<List<CityResponse>>>

    @POST("action/participateAction")
    suspend fun sendPromocode(@Body promocodeRequestData: PromocodeReequestData): Response<ResponseServer<Int>>

    @GET("ownnotification/getAll/{id}")
    suspend fun getAllOwnNotifications(
        @Path("id") id: String
    ): Response<ResponseServer<List<OwnNotificationResponse>>>

    @PATCH("ownnotification/{id}")
    suspend fun changeStatusToRead(
        @Path("id") notificationId: String,
        @Body isRead: IsRead
    ): Response<ResponseServer<OwnNotificationResponse>>
}