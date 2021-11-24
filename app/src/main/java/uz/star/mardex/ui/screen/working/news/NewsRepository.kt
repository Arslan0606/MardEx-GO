package uz.star.mardex.ui.screen.working.news

import androidx.lifecycle.LiveData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.news.NewsData

/**
 * Created by Jasurbek Kurganbaev on 27.06.2021 14:45
 **/
interface NewsRepository {
    fun getNews(): LiveData<ResultData<List<NewsData>>>
}