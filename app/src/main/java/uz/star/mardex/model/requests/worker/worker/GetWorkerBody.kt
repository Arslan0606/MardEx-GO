package uz.star.mardex.model.requests.worker


import com.google.gson.annotations.SerializedName

data class GetWorkerBody(
    @SerializedName("radius")
    val radius: String,
    @SerializedName("job_id")
    val jobId: String
)