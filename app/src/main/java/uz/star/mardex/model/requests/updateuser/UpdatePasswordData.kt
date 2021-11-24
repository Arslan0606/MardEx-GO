package uz.star.mardex.model.requests.updateuser

import com.google.gson.annotations.SerializedName

data class UpdatePasswordData(
    @SerializedName("oldPassword")
    val oldPassword: String,
    @SerializedName("newPassword")
    val newPassword: String
)
