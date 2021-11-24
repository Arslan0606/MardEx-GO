package uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 08-Apr-21
 **/

class FillingVacancyRepositoryImpl @Inject constructor(
    private val profileDataApi: ProfileDataApi,
    private val localStorage: LocalStorage
) : FillingVacancyRepository, SafeApiRequest() {

    override fun uploadImage(part: MultipartBody.Part): LiveData<ResultData<ImagePath>> {
        val resultLiveData = MutableLiveData<ResultData<ImagePath>>()

        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.uploadImage(part) } },
            { data ->
                data?.onData {
                    resultLiveData.value = ResultData.data(it)
                }
                data?.onMessage {
                    resultLiveData.value = ResultData.message(it)
                }?.onMessageData { messageData ->
                    messageData.onResource { resultLiveData.value = ResultData.messageData(MessageData.resource(it)) }
                }
            }
        )

        return resultLiveData
    }

    override fun sendLanguage() {
        Coroutines.ioThenMain(
            { apiRequest { profileDataApi.updatePersonalData(localStorage.id, EditedPersonalData(lang = localStorage.lang)) } },
            {}

        )
    }
}