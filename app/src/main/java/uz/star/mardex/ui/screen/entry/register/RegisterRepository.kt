package uz.star.mardex.ui.screen.entry.register

import androidx.lifecycle.LiveData
import uz.star.mardex.model.requests.registration.CheckExistData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.regions.CityResponse
import uz.star.mardex.model.response.regions.RegionResponse
import uz.star.mardex.model.response.server.ResponseServer

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

interface RegisterRepository {
    fun sendVerificationCode(phone: String): LiveData<ResultData<Boolean>>
    fun checkExist(checkExistData: CheckExistData): LiveData<ResultData<Boolean>>
    fun getCities(): LiveData<ResultData<ResponseServer<List<CityResponse>>>>
    fun getRegions(): LiveData<ResultData<ResponseServer<List<RegionResponse>>>>
}