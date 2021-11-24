package uz.star.mardex.ui.screen.entry

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.ActivityEntryBinding
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.show
import uz.star.mardex.utils.helpers.MyContextWrapper

@AndroidEntryPoint
class EntryActivity : AppCompatActivity() {

    val localStorage = LocalStorage.instance

    private var _binding: ActivityEntryBinding? = null
    private val binding: ActivityEntryBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("T12T", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            LocalStorage.instance.notificationToken = token ?: ""
        })
        changeLang()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, LocalStorage.instance.langLocal))
    }

    @Suppress("DEPRECATION")
    fun changeLang() {
        val context: Context = MyContextWrapper.wrap(this, LocalStorage.instance.langLocal)
        resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
    }

    fun showLoader() {
        binding.loader.root.show()
    }

    fun hideLoader() {
        binding.loader.root.hide()
    }
}