package uz.star.mardex.ui.screen.working.profile.editpassword_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.requests.updateuser.UpdatePasswordData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

class EditPasswordRepositoryImpl @Inject constructor(
    private val profileDataApi: ProfileDataApi
) : EditPasswordRepository, SafeApiRequest() {

    override fun updatePassword(userId: String, updatePasswordData: UpdatePasswordData): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.updatePassword(userId, updatePasswordData) } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(it.data)
                    else
                        resultLiveData.value = ResultData.message("list bo'sh")
                }
                data?.onMessage {
                    resultLiveData.value = ResultData.message(it)
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                    }
                }
            }
        )

        return resultLiveData
    }
}