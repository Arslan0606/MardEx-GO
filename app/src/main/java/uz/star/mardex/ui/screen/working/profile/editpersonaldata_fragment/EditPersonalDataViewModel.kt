package uz.star.mardex.ui.screen.working.profile.editpersonaldata_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.addSourceDisposable
import uz.star.mardex.utils.liveData.Event
import javax.inject.Inject

@HiltViewModel
class EditPersonalDataViewModel @Inject constructor(
    private val storage: LocalStorage,
    private val repository: EditPersonalDataRepository
) : ViewModel() {

    private val _responseUserData = MediatorLiveData<UserData>()
    val responseUserData: LiveData<UserData> get() = _responseUserData

    private val _updateUserData = MediatorLiveData<UserData>()
    val updateUserData: LiveData<UserData> get() = _updateUserData

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _backToProfileFragmentData = MutableLiveData<Event<Unit>>()
    val backToProfileFragmentData: LiveData<Event<Unit>> get() = _backToProfileFragmentData


    fun getUserData() {
        val userId = storage.id
        _responseUserData.addSourceDisposable(repository.getUserData(userId)) {
            it.onData { responseUserData ->
                _responseUserData.value = responseUserData
            }.onMessage { message ->
                _message.value = MessageData.message(message)
            }.onMessageData { messageData ->
                messageData.onResource { stringResources ->
                    _message.value = MessageData.resource(stringResources)
                }
            }
        }
    }

    fun updateUser(newName: String) {
        if (newName.isNotBlank()) {
            val editedPersonalData = EditedPersonalData(
                phone = storage.phone,
                name = newName,
                pic = storage.avatarPath,
                location = LocationRequest(coordinates = arrayListOf(storage.currentLong, storage.currentLat))
            )
            _updateUserData.addSourceDisposable(
                repository.updateUserData(
                    userId = storage.id,
                    editedPersonalData = editedPersonalData
                )
            ) {
                it.onData { responseUpdateUserData ->
                    _updateUserData.value = responseUpdateUserData
                    storage.name = newName
                    backToProfileFragment()
                }.onMessage { message ->
                    _message.value = MessageData.message(message)
                }.onMessageData { messageData ->
                    messageData.onResource { stringResources ->
                        _message.value = MessageData.resource(stringResources)
                    }
                }
            }
        } else {
            return
        }
    }

    fun backToProfileFragment() {
        _backToProfileFragmentData.value = Event(Unit)
    }

}