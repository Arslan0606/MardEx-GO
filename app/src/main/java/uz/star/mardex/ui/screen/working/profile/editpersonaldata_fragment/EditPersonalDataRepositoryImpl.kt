package uz.star.mardex.ui.screen.working.profile.editpersonaldata_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

class EditPersonalDataRepositoryImpl @Inject constructor(
    private val profileDataApi: ProfileDataApi
) : EditPersonalDataRepository, SafeApiRequest() {

    override fun getUserData(userId: String): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()

        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.getUserData(userId) } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(it.data)
                    else
                        resultLiveData.value = ResultData.message("list bosh")
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

    override fun updateUserData(userId: String, editedPersonalData: EditedPersonalData): LiveData<ResultData<UserData>> {
        val resultLiveData = MutableLiveData<ResultData<UserData>>()
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.updatePersonalData(userId, editedPersonalData) } },
            { data ->
                data?.onData {
                    if (it.success)
                        resultLiveData.value = ResultData.data(it.data)
                    else
                        resultLiveData.value = ResultData.message("list bosh")
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