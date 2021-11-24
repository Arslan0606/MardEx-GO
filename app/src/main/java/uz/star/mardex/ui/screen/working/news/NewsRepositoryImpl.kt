package uz.star.mardex.ui.screen.working.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.data.remote.NewsApi
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.local.ResultData
import uz.star.mardex.model.response.news.NewsData
import uz.star.mardex.utils.network.Coroutines
import uz.star.mardex.utils.network.SafeApiRequest
import javax.inject.Inject

/**
 * Created by Jasurbek Kurganbaev on 27.06.2021 14:45
 **/
class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val storage: LocalStorage
) : NewsRepository, SafeApiRequest() {

    override fun getNews(): LiveData<ResultData<List<NewsData>>> {
        val resultLiveData = MutableLiveData<ResultData<List<NewsData>>>()

        Coroutines.ioThenMain({
            apiRequest { newsApi.getNews() }
        },
            { data ->
                data?.onData {
                    if (it.success) {
                        resultLiveData.value = ResultData.data(it.data)
                    } else
                        resultLiveData.value = ResultData.message("list bosh")
                }
                data?.onMessage {
                    resultLiveData.value = ResultData.message(it)
                }?.onMessageData { messageData ->
                    messageData.onResource {
                        resultLiveData.value = ResultData.messageData(MessageData.resource(it))
                    }
                }

            })
        return resultLiveData
    }


}