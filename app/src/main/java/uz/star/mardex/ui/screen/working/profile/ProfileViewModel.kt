package uz.star.mardex.ui.screen.working.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.requests.updateuser.Pic
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.addSourceDisposable
import uz.star.mardex.utils.liveData.Event
import java.io.File
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 08-Apr-21
 **/

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val storage: LocalStorage
) : ViewModel() {

    private val _imagePath = MutableLiveData<ImagePath>()
    val imagePath: LiveData<ImagePath> get() = _imagePath

    private val _message = MediatorLiveData<Event<MessageData>>()
    val message: LiveData<Event<MessageData>> get() = _message

    private val _responseUserData = MutableLiveData<UserData>()
    val responseUserData: LiveData<UserData> get() = _responseUserData

    private val _updateUserData = MutableLiveData<UserData>()
    val updateUserData: LiveData<UserData> get() = _updateUserData

    fun getUserData() {
        val userId = storage.id

        _message.addSourceDisposable(repository.getUserData(userId)) {
            it.onData { loginResponse ->
                storage.name = loginResponse.name
                storage.avatarPath = loginResponse.pic ?: ""
                _responseUserData.value = loginResponse
            }.onMessage { message ->

                _message.value = Event(MessageData.message(message))
            }.onMessageData { messageData ->
                _message.value = Event(messageData)
            }
        }

    }

    fun uploadImage(image: File?) {
        if (image == null) return
        val multiPartBody = MultipartBody.Part.createFormData(
            "image", "avatar.jpg",
            RequestBody.create(MediaType.parse("image/JPEG"), image.readBytes())
        )

        _message.addSourceDisposable(repository.uploadImage(multiPartBody)) {
            it.onData { imagePath ->
                _imagePath.value = imagePath
            }.onMessageData { messageData ->
                _message.value = Event(messageData)
            }
        }
    }

    fun updateUser() {
        val editedPersonalData = EditedPersonalData(
            phone = storage.phone,
            name = storage.name,
            pic = storage.avatarPath,
            location = LocationRequest(coordinates = arrayListOf(storage.currentLong, storage.currentLat))
        )
        _message.addSourceDisposable(
            repository.updateUserData(
                userId = storage.id,
                editedPersonalData = editedPersonalData
            )
        ) {
            it.onData { responseUpdateUserData ->
                _updateUserData.value = responseUpdateUserData
            }.onMessage { message ->
                _message.value = Event(MessageData.message(message))
            }.onMessageData { messageData ->
                _message.value = Event(messageData)
            }
        }
    }


    fun updateUserPicImage() {
        val pic = Pic(storage.avatarPath)

        _message.addSourceDisposable(
            repository.updateUserPicImage(
                userId = storage.id,
                pic
            )
        )
        {
            it.onData { responseUpdateUserData ->
                _updateUserData.value = responseUpdateUserData
            }.onMessage { message ->
                _message.value = Event(MessageData.message(message))
            }.onMessageData { messageData ->
                _message.value = Event(messageData)
            }
        }
    }

}




