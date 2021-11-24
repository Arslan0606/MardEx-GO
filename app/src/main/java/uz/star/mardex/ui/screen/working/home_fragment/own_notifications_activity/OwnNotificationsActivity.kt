package uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.databinding.ActivityOwnNotificationsBinding

@AndroidEntryPoint
class OwnNotificationsActivity : AppCompatActivity() {

    private var _binding: ActivityOwnNotificationsBinding? = null
    private val binding: ActivityOwnNotificationsBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOwnNotificationsBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_white)
        setContentView(binding.root)
        loadViews()
    }

    private fun loadViews() {
        binding.apply {
        }
    }


}