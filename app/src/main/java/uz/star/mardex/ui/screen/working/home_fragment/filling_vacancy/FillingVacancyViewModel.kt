package uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.utils.extension.addSourceDisposable
import java.io.File
import javax.inject.Inject

/**
 * Created by Farhod Tohirov on 08-Apr-21
 **/

@HiltViewModel
class FillingVacancyViewModel @Inject constructor(
    private val repository: FillingVacancyRepository,
    private val storage: LocalStorage
) : ViewModel() {

    private val _imagePath = MutableLiveData<ImagePath>()
    val imagePath: LiveData<ImagePath> get() = _imagePath

    private val _message = MediatorLiveData<MessageData>()
    val message: LiveData<MessageData> get() = _message

    fun uploadImage(image: File?) {
        if (image == null) return
        val multiPartBody = MultipartBody.Part.createFormData(
            "image", "doc.jpg",
            RequestBody.create(MediaType.parse("image/JPEG"), image.readBytes())
        )

        _message.addSourceDisposable(repository.uploadImage(multiPartBody)) {
            it.onData { imagePath ->
                _imagePath.value = imagePath
            }.onMessageData { messageData ->
                _message.value = messageData
            }
        }
    }

    /* Used for only HomeFragment*/
    fun sendLanguage() {
        repository.sendLanguage()
    }

}




