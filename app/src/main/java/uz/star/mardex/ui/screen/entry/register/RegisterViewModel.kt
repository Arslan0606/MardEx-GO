package uz.star.mardex.ui.screen.entry.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.R
import uz.star.mardex.model.requests.registration.CheckExistData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.regions.CityResponse
import uz.star.mardex.model.response.regions.RegionResponse
import uz.star.mardex.utils.extension.addSourceDisposable
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: RegisterRepository) : ViewModel() {

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _sendDone = MutableLiveData<Boolean>()
    val sendDone: LiveData<Boolean> get() = _sendDone

    private val _responseRegions = MediatorLiveData<List<RegionResponse>>()
    val responseRegions: LiveData<List<RegionResponse>> get() = _responseRegions

    private val _responseCities = MediatorLiveData<List<CityResponse>>()
    val responseCities: LiveData<List<CityResponse>> get() = _responseCities

    private val _loader = MutableLiveData<Boolean>()
    val loader: LiveData<Boolean> get() = _loader

    init {
        _loader.value = true
        _responseRegions.addSourceDisposable(repository.getRegions()) { it ->
            _loader.value = false
            it.onData {
                if (it.success) {
                    _responseRegions.value = it.data!!
                }
                getCities()
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { message ->
                _message.value = message
            }
        }
    }

    private fun getCities() {
        _loader.value = true
        _responseCities.addSourceDisposable(repository.getCities()) { it ->
            _loader.value = false
            it.onData {
                if (it.success) {
                    _responseCities.value = it.data!!
                }
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { message ->
                _message.value = message
            }
        }
    }

    fun sendSms(phone: String) {
        _message.addSourceDisposable(repository.checkExist(CheckExistData("client", phone))) {
            it.onData { success ->
                if (success)
                    _message.value = MessageData.resource(R.string.please_reset_password)
                else
                    sendSmsVerification(phone)
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { stringSource ->
                    _message.value = MessageData.resource(stringSource)
                }
            }
        }


    }

    private fun sendSmsVerification(phone: String) {
        _message.addSourceDisposable(repository.sendVerificationCode(phone)) {
            it.onData { success ->
                _sendDone.value = success
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { stringSource ->
                    _message.value = MessageData.resource(stringSource)
                }
            }
        }
    }

}