package uz.star.mardex.ui.screen.working.profile

import androidx.lifecycle.LiveData
import okhttp3.MultipartBody
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.requests.updateuser.Pic
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.model.response.server.user.UserData


/**
 * Created by Farhod Tohirov on 08-Apr-21
 **/

interface ProfileRepository {
    fun uploadImage(part: MultipartBody.Part): LiveData<ResultData<ImagePath>>
    fun updateUserData(userId: String, editedPersonalData: EditedPersonalData): LiveData<ResultData<UserData>>
    fun getUserData(workerId: String): LiveData<ResultData<UserData>>
    fun updateUserPicImage(userId: String, pic: Pic): LiveData<ResultData<UserData>>
// AWD
}




