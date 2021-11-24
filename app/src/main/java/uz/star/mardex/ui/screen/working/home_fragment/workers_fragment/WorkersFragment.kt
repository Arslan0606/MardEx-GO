package uz.star.mardex.ui.screen.working.home_fragment.workers_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.databinding.FragmentWorkersBinding
import uz.star.mardex.model.data_exchange.WorkersList
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.ImageSliderAdapter
import uz.star.mardex.ui.adapter.recycler_view.JobWorkerRVAdapter
import uz.star.mardex.ui.adapter.recycler_view.WorkersListRVAdapter
import uz.star.mardex.ui.adapter.view_pager.WorkersViewPagerAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showMessage

@AndroidEntryPoint
class WorkersFragment : Fragment() {

    private var _binding: FragmentWorkersBinding? = null
    private val binding: FragmentWorkersBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val adapter: WorkersViewPagerAdapter by lazy {
        WorkersListRVAdapter.init()
        WorkersViewPagerAdapter(requireActivity())
    }

    private val workersAdapter by lazy { WorkersListRVAdapter.instance }

    private val viewModel: WorkersViewModel by viewModels()
    private val args: WorkersFragmentArgs by navArgs()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val selectedWorkersList by lazy { ArrayList<WorkerData>() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkersBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        hideBottomMenu()
        changeStatusColorMainColor()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.workerInfo.bottomSheetWorker)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        binding.pager.adapter = adapter
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.isDraggable = true



        bottomSheetBehavior.hide()
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setText(R.string.on_map)
                }
                1 -> {
                    tab.setText(R.string.list)
                }
            }
        }.attach()

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.pager.isUserInputEnabled = position == 1
                if (position == 0) binding.title.text = getString(R.string.map) else binding.title.text = getString(R.string.list)
            }
        })

        viewModel.getWorkers(args.job.id)
        showLoader()
        workersAdapter.setOnWorkerClickListener {
            setWorkerData(it)
        }
        selectedWorkersList.clear()
        binding.continueButton.text = getString(R.string.send_n_request, 0)
        workersAdapter.setOnWorkerCheckListener {
            if (it.isSelected) {
                selectedWorkersList.add(it)
            } else {
                selectedWorkersList.remove(it)
            }
            binding.continueButton.alpha = if (selectedWorkersList.isEmpty()) 0.5f else 1f

            if (binding.pager.currentItem != 0) {
                if (selectedWorkersList.size == workersAdapter.currentList.size) {
                    adapter.allWorkersSelected(true)
                } else {
                    adapter.allWorkersSelected(false)
                }
            }

            binding.continueButton.text = getString(R.string.send_n_request, selectedWorkersList.size)
        }

        workersAdapter.setOnAllWorkersSelectListener {
            if (it) {
                selectedWorkersList.clear()
                selectedWorkersList.addAll(workersAdapter.currentList)
                binding.continueButton.text = getString(R.string.send_n_request, selectedWorkersList.size)
                binding.continueButton.alpha = 1f
            } else {
                selectedWorkersList.clear()
                binding.continueButton.text = getString(R.string.send_n_request, selectedWorkersList.size)
                binding.continueButton.alpha = 0.5f
            }
        }

        binding.continueButton.setOnClickListener {
            if (selectedWorkersList.isEmpty()) return@setOnClickListener
            val action = WorkersFragmentDirections.actionWorkersFragmentToSelectedWorkersFragment(WorkersList(selectedWorkersList), args.vacanyData)
            findNavController().navigate(action)
            binding.pager.adapter = null
        }

    }

    private fun setWorkerData(workerData: WorkerData) {
        bottomSheetBehavior.show()
        binding.workerInfo.name.text = workerData.fullName
        binding.workerInfo.close.setOnClickListener {
            bottomSheetBehavior.hide()
        }
        binding.workerInfo.ratingBar.rating = workerData.sumMark.rating()
        binding.workerInfo.ratingNumber.text = workerData.sumMark.rating().toString().substring(0, 3)
        if (workerData.description.isNullOrEmpty()) {
            binding.workerInfo.description.hide()
        } else {
            binding.workerInfo.description.text = workerData.description
        }
        binding.workerInfo.profileImage.loadImageUrl(workerData.avatar ?: "", isProfile = true, isWorker = true)
        if (workerData.jobs.isNotEmpty()) {
            binding.workerInfo.categoryList.show()
            binding.workerInfo.categoryText.show()
            binding.workerInfo.categoryList.adapter = JobWorkerRVAdapter().apply { submitList(workerData.jobs) }
        } else {
            binding.workerInfo.categoryList.hide()
            binding.workerInfo.categoryText.hide()
        }
        if (workerData.images?.isNotEmpty() == true) {
            binding.workerInfo.photoList.show()
            binding.workerInfo.photoText.show()
            binding.workerInfo.photoList.visibility = View.VISIBLE
            binding.workerInfo.photoList.setSliderAdapter(ImageSliderAdapter(workerData.images))
            binding.workerInfo.photoList.setIndicatorAnimation(IndicatorAnimationType.WORM)
            binding.workerInfo.photoList.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            binding.workerInfo.photoList.startAutoCycle()
        } else {
            binding.workerInfo.photoList.hide()
            binding.workerInfo.photoText.hide()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, showMessage())
        viewModel.workers.observe(this, workersObserver)
    }

    private val workersObserver = Observer<List<WorkerData>> {
        hideLoader()
        adapter.submitList(it)
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

}