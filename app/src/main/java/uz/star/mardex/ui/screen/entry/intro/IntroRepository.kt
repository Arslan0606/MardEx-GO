package uz.star.mardex.ui.screen.entry.intro

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.server.intro.IntroData

/**
 * Created by Farhod Tohirov on 11-Jan-21
 **/

interface IntroRepository {
    fun getIntroData(): LiveData<ResultData<List<IntroData>>>
}