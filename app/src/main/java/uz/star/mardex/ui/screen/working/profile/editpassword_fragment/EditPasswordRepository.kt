package uz.star.mardex.ui.screen.working.profile.editpassword_fragment

import androidx.lifecycle.LiveData
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.requests.updateuser.UpdatePasswordData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData

interface EditPasswordRepository {
    fun updatePassword(userId: String, updatePasswordData: UpdatePasswordData): LiveData<ResultData<UserData>>
}