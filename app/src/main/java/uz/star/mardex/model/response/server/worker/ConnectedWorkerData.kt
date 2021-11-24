package uz.star.mardex.model.response.server.worker


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import uz.star.mardex.model.response.server.jobs.Job
import uz.star.mardex.model.results.server.location.LocationResponse
import java.io.Serializable

data class ConnectedWorkerData(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("balance")
    val balance: Int,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("_id")
    val id: String,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("isFree")
    val isFree: Boolean,
    @SerializedName("jobs")
    val jobs: List<String>,
    @SerializedName("location")
    val location: LocationResponse,
    @SerializedName("packet")
    val packet: Int,
    @SerializedName("password")
    val password: Any?,
    @SerializedName("payment_id")
    val paymentId: String,
    @SerializedName("payments")
    val payments: List<Any>,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("__v")
    val v: Int,

    @SerializedName("sum_mark")
    var sumMark: SumMark?,

    @SerializedName("allJobs")
    var allJobs: List<Job>? = null,

    @Expose
    var isSelected: Boolean = false
) : Serializable