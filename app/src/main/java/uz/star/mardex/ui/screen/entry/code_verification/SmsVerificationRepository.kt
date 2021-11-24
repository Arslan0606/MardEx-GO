package uz.star.mardex.ui.screen.entry.code_verification

import androidx.lifecycle.LiveData
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.requests.registration.RegistrationData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

interface SmsVerificationRepository {
    fun verifyPhone(phone: String, code: String): LiveData<ResultData<Boolean>>
    fun register(registrationData: RegistrationData): LiveData<ResultData<UserData>>
    fun sendVerificationCode(phone: String): LiveData<ResultData<Boolean>>
    fun restorePassword(login: LoginData): LiveData<ResultData<UserData>>
    fun sendNotificationTokenToServer(): LiveData<ResultData<UserData>>
}