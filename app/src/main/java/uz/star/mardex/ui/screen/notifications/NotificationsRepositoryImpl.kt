package uz.star.mardex.ui.screen.notifications

import androidx.lifecycle.LiveData
import uz.star.mardex.data.remote.ProfileDataApi
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Davronbek Raximjanov on 27.06.2021 15:32
 **/

class NotificationsRepositoryImpl @Inject constructor(
    private val api: ProfileDataApi,
) : NotificationsRepository, SafeApiRequest() {
    override fun getNews(): LiveData<ResultData<String>> {
        TODO("Not yet implemented")
    }
}