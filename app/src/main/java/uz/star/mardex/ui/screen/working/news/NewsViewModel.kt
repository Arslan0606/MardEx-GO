package uz.star.mardex.ui.screen.working.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.news.NewsData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Jasurbek Kurganbaev on 27.06.2021 14:42
 **/

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val storage: LocalStorage
) : ViewModel() {

    private val _news = MediatorLiveData<List<NewsData>>()
    val news: LiveData<List<NewsData>> get() = _news


    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> get() = _loader

    private val _message = MutableLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message


    fun getNews() {
        _loader.value = true
        _news.addSourceDisposable(newsRepository.getNews()) {
            it.onData { list ->
                _news.value = list
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { stringResources ->
                    _message.value = MessageData.resource(stringResources)
                }
            }
            _loader.value = false
        }
    }

}