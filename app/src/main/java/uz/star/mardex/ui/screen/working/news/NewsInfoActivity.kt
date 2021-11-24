package uz.star.mardex.ui.screen.working.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.databinding.FragmentNewsInfoBinding
import uz.star.mardex.model.response.news.NewsData
import uz.star.mardex.model.response.news.toLocalString
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewsInfoActivity : AppCompatActivity() {

    private var _binding: FragmentNewsInfoBinding? = null
    private val binding: FragmentNewsInfoBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment
        _binding = FragmentNewsInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadViews()
    }

    private fun loadViews() {
        val data = intent.getBundleExtra("news")?.getSerializable("news") as NewsData
        binding.apply {
            textViewNewsInfoTitle.text = data.title?.toLocalString()
            newsDescription.text = data.description?.toLocalString()
            val f = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = data.createdAt!!
            newsDate.text = f.format(calendar.time)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}