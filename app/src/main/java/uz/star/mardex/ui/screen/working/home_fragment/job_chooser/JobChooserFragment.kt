package uz.star.mardex.ui.screen.working.home_fragment.job_chooser

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentJobChooserBinding
import uz.star.mardex.model.response.server.jobs.Job
import uz.star.mardex.model.response.server.jobs.JobCategory
import uz.star.mardex.ui.adapter.recycler_view.JobCategoryRVAdapter
import uz.star.mardex.ui.adapter.recycler_view.JobRVAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showMessage
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class JobChooserFragment : Fragment() {

    private var _binding: FragmentJobChooserBinding? = null
    private val binding: FragmentJobChooserBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: JobChooserViewModel by viewModels()

    var storage = LocalStorage.instance

    private lateinit var adapterCategory: JobCategoryRVAdapter
    private lateinit var adapterJobItems: JobRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        changeStatusColorWhite()
        hideBottomMenu()
        _binding = FragmentJobChooserBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.apply {
            adapterCategory = JobCategoryRVAdapter()
            adapterJobItems = JobRVAdapter()

            listJobs.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = this@JobChooserFragment.adapterJobItems
            }

            listCategory.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = this@JobChooserFragment.adapterCategory
            }

            adapterCategory.setOnCategoryItemClickListener { it, title ->
                textJobs.text = getString(R.string.choose_jobs, title)
                adapterJobItems.submitList(it)
            }

            btnShowAll.setOnClickListener {
                if (textShowAll.text == getString(R.string.show_all)) {
                    binding.textShowAll.text = getString(R.string.close_text)
                    listCategory.layoutManager = GridLayoutManager(requireContext(), 2)
                    binding.imageLine.rotation = 180f
                    imageCloseCategory.show()
                } else {
                    imageCloseCategory.callOnClick()
                }
            }

            btnBack.setOnClickListener {
                findNavController().popBackStack()
                hideKeyboard(it)
            }

            imageCloseCategory.setOnClickListener {
                it.hide()
                binding.imageLine.rotation = 0f
                binding.textShowAll.text = getString(R.string.show_all)
                listCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                listCategory.scrollToPosition(adapterCategory.lastSelectedIndex)
            }
            viewModel.getJobs()
            showLoader()
            try {
                initializeSearchView()
            } catch (e: Exception) {

            }
            hideKeyboard(root)
        }

        adapterJobItems.setOnJobSelectedListener { job, pos ->
            hideKeyboard(binding.root)
            val action = JobChooserFragmentDirections.actionJobChooserFragmentToFillingVacancyFragment(job)
            binding.search.addTextChangedListener(null)
            findNavController().navigate(action)
        }
    }

    private fun initializeSearchView() {
        binding.apply {
            layoutSearch.setEndIconOnClickListener {
                hideKeyboard(root)
                search.text?.clear()

                showListJobs()
                showLayoutDef()
                textJobs.show()
                textSearchResults.hide()
                try {
                    adapterJobItems.submitList(adapterCategory.currentList[adapterCategory.lastSelectedIndex].jobs)
                } catch (e: Exception){
                    adapterJobItems.submitList(emptyList())
                }
            }
            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(it: Editable?) {
                    val text = it.toString()
                    if (text.trim().isNotEmpty()) {
                        binding.textChooseCategory.hide()
                        textJobs.hide()
                        textSearchResults.show()
                        searchJob(adapterCategory.currentList, text)
                    } else {
                        binding.textChooseCategory.show()
                        searchJob(adapterCategory.currentList, "")
                        showLayoutDef()
                        textJobs.show()
                        textSearchResults.hide()
                        hideKeyboard(root)
                        try {
                            adapterJobItems.submitList(adapterCategory.currentList[adapterCategory.lastSelectedIndex].jobs)
                        } catch (e: java.lang.Exception) {

                        }
                    }
                }
            })
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.jobs.observe(this, jobsObserver)
        viewModel.message.observe(this, showMessage())
    }

    private val jobsObserver = Observer<List<JobCategory>> { list ->
        list?.let {
            if (list.isNotEmpty()) {
                val data = list[0]
                data.isCollapsed = true
                adapterJobItems.submitList(data.jobs)
                val dataTitle = data.categoryTitle
                val title = dataTitle.title()
                binding.textJobs.text = getString(R.string.choose_jobs, title)
            }
        }
        adapterCategory.submitList(list)
        hideLoader()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchJob(list: MutableList<JobCategory>, text: String) {
        binding.apply {
            emptyText.hide()
            showListJobs()
            if (text.trim().isEmpty()) {
                showLayoutDef()
                textJobs.show()
                textSearchResults.hide()
                return
            }
            textJobs.hide()
            textSearchResults.show()
            hideLayoutDef()
            val resultList = ArrayList<Job>()
            for (jobCategory in list) {
                for (job in jobCategory.jobs) {
                    val dataTitle = job.title
                    val title = dataTitle.title()
                    if (title?.toLowerCase(Locale.ROOT)
                            ?.contains(text.trim().toLowerCase(Locale.ROOT)) == true
                    ) {
                        resultList.add(job)
                    }
                }
            }
            adapterJobItems.submitList(resultList)
            if (resultList.isEmpty()) {
                hideListJobs()
                emptyText.show()
            }
        }
    }

    private fun showListJobs() {
        binding.apply {
            listJobs.show()
            textJobs.show()
        }
    }

    private fun hideListJobs() {
        binding.apply {
            listJobs.hide()
            textJobs.hide()
            textSearchResults.hide()
        }
    }

    private fun showLayoutDef() {
        binding.apply {
            if (listCategory.layoutManager is GridLayoutManager) {
                imageCloseCategory.show()
            } else
                imageCloseCategory.hide()
            textChooseCategory.show()
            listCategory.show()
            textShowAll.show()
            btnShowAll.show()
            imageLine.show()
        }
    }

    private fun hideLayoutDef() {
        binding.apply {
            if (listCategory.layoutManager is GridLayoutManager) {
                imageCloseCategory.hide()
            }
            textChooseCategory.hide()
            listCategory.hide()
            textShowAll.hide()
            btnShowAll.hide()
            imageLine.hide()
        }
    }

}