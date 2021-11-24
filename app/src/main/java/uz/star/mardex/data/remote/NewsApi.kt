package uz.star.mardex.data.remote

import retrofit2.Response
import retrofit2.http.GET
import uz.star.mardex.model.response.news.NewsData
import uz.star.mardex.model.response.server.ResponseServer

/**
 * Created by Jasurbek Kurganbaev on 27.06.2021 14:46
 **/
interface NewsApi {

    @GET("news/getAll")
    suspend fun getNews(): Response<ResponseServer<List<NewsData>>>
}