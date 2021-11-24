package uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy

import androidx.lifecycle.LiveData
import okhttp3.MultipartBody
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.ImagePath

/**
 * Created by Farhod Tohirov on 08-Apr-21
 **/

interface FillingVacancyRepository {
    fun uploadImage(part: MultipartBody.Part): LiveData<ResultData<ImagePath>>
    fun sendLanguage()
}