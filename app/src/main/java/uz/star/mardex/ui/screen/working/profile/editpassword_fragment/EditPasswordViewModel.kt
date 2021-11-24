package uz.star.mardex.ui.screen.working.profile.editpassword_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.requests.updateuser.EditedPersonalData
import uz.star.mardex.model.requests.updateuser.UpdatePasswordData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.addSourceDisposable
import uz.star.mardex.utils.liveData.Event
import javax.inject.Inject

@HiltViewModel
class EditPasswordViewModel @Inject constructor(
    private val repository: EditPasswordRepository,
    private val storage: LocalStorage
) : ViewModel() {

    private val _updateUserPasswordData = MediatorLiveData<UserData>()
    val updateUserPasswordData: LiveData<UserData> get() = _updateUserPasswordData

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    private val _backToProfileFragmentData = MutableLiveData<Event<Unit>>()
    val backToProfileFragmentData: LiveData<Event<Unit>> get() = _backToProfileFragmentData

    private val _toastMessageData = MutableLiveData<String>()
    val toastMessageData: LiveData<String> get() = _toastMessageData

    fun backToProfileFragment() {
        _backToProfileFragmentData.value = Event(Unit)
    }


    fun updateUserPassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        if (oldPassword == "" || newPassword == "" || confirmPassword == "") {
            _toastMessageData.value = "The information is incomplete. Please fill in all the information"
        } else if (newPassword != confirmPassword) {
            _toastMessageData.value = "Error confirm password. Please enter correctly"
        } else {
            val updatePasswordData = UpdatePasswordData(
                oldPassword = oldPassword,
                newPassword = newPassword
            )

            _updateUserPasswordData.addSourceDisposable(
                repository.updatePassword(
                    userId = storage.id,
                    updatePasswordData = updatePasswordData
                )
            ) {
                it.onData { responseUpdateUserData ->
                    _updateUserPasswordData.value = responseUpdateUserData
                    backToProfileFragment()
                }.onMessage { message ->
                    _message.value = MessageData.message(message)
                }.onMessageData { messageData ->
                    messageData.onResource { stringResources ->
                        _message.value = MessageData.resource(stringResources)
                    }
                }
            }
        }
    }


}