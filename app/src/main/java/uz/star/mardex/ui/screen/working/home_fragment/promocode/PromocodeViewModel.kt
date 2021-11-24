package uz.star.mardex.ui.screen.working.home_fragment.promocode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.promocode.PromocodeReequestData
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 27-Jun-21
 **/
@HiltViewModel
class PromocodeViewModel @Inject constructor(private val repository: PromocodeRepository, private val storage: LocalStorage) : ViewModel() {

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _successData = MutableLiveData<Boolean>()
    val successData: LiveData<Boolean> get() = _successData


    fun sendPromocode(promoCode: String) {
        _message.addSourceDisposable(repository.sendPromocode(PromocodeReequestData(promoCode.toInt(), "client", storage.id))) {
            it.onData { status ->
                _successData.value = status
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { message ->
                _message.value = message
            }
        }
    }

}