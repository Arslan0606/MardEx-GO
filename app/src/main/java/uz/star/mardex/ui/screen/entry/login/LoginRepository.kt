package uz.star.mardex.ui.screen.entry.login

import androidx.lifecycle.LiveData
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.user.UserData

/**
 * Created by Farhod Tohirov on 12-Jan-21
 **/

interface LoginRepository {
    fun login(loginData: LoginData): LiveData<ResultData<UserData>>
    fun sendNotificationTokenToServer(): LiveData<ResultData<UserData>>
}