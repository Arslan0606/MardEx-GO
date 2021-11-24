package uz.star.mardex.ui.screen.working.news

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.ActivityNewsBinding
import uz.star.mardex.model.response.news.NewsData
import uz.star.mardex.ui.adapter.recycler_view.NewsRVAdapter
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.show
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    private var _binding: ActivityNewsBinding? = null
    private val binding: ActivityNewsBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var storage: LocalStorage

    private lateinit var newsAdapter: NewsRVAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_white)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.getNews()
        newsAdapter = NewsRVAdapter(storage.lang)
        binding.newsRecyclerView.adapter = newsAdapter

        newsAdapter.setOnItemClickListener {
            val intent = Intent(this, NewsInfoActivity::class.java)
            val bundle = Bundle().apply { putSerializable("news", it) }
            intent.putExtra("news", bundle)
            startActivity(intent)
        }
    }

    private val newsObserver = Observer<List<NewsData>> { newsList ->
        binding.textSoon.hide()
        binding.newsRecyclerView.show()
        newsAdapter.submitList(newsList)
    }

    private fun loadObservers() {
        viewModel.news.observe(this, newsObserver)
    }

}