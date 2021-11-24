package uz.star.mardex.ui.screen.notifications

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData

/**
 * Created by Davronbek Raximjanov on 27.06.2021 15:32
 **/
interface NotificationsRepository {
    fun getNews(): LiveData<ResultData<String>>
}