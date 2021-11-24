package uz.star.mardex.ui.screen.entry.reset_passpord

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData

/**
 * Created by Kurganbaev Jasurbek on 28.04.2021 11:09
 **/
interface RestorePasswordRepository {
    fun sendVerificationCode(phone: String): LiveData<ResultData<Boolean>>
}