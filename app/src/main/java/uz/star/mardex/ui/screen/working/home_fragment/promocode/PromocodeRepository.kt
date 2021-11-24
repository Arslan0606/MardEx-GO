package uz.star.mardex.ui.screen.working.home_fragment.promocode

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.promocode.PromocodeReequestData

/**
 * Created by Farhod Tohirov on 27-Jun-21
 **/

interface PromocodeRepository {
    fun sendPromocode(promocodeRequestData: PromocodeReequestData): LiveData<ResultData<Boolean>>
}