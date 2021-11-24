package uz.star.mardex.ui.screen.entry.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.model.response.server.intro.IntroData
import uz.star.mardex.ui.screen.entry.intro.IntroRepository
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 11-Jan-21
 **/

@HiltViewModel
class IntroViewModel @Inject constructor(private val repository: IntroRepository) : ViewModel() {
    private val _introData = MediatorLiveData<List<IntroData>>()
    val introData: LiveData<List<IntroData>> get() = _introData

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    init {
        getIntroData()
    }

    private fun getIntroData() {
        _introData.addSourceDisposable(repository.getIntroData()) {
            it.onData { list ->
                _introData.value = list
            }.onMessage { message ->
                _message.value = message
            }
        }
    }

}