package uz.star.mardex.ui.screen.working.profile.editpersonaldata_fragment

import androidx.lifecycle.LiveData
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData

interface EditPersonalDataRepository {
    fun getUserData(userId: String): LiveData<ResultData<UserData>>
    fun updateUserData(userId: String, editedPersonalData: EditedPersonalData): LiveData<ResultData<UserData>>
}