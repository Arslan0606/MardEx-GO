package uz.star.mardex.ui.screen.working.home_fragment.selected_workers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import uz.star.mardex.R
import uz.star.mardex.databinding.FragmentSelectedWorkersBinding
import uz.star.mardex.model.data_exchange.WorkersList
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.ImageSliderAdapter
import uz.star.mardex.ui.adapter.recycler_view.JobWorkerRVAdapter
import uz.star.mardex.ui.adapter.recycler_view.WorkersListRVAdapter
import uz.star.mardex.utils.extension.*

class SelectedWorkersFragment : Fragment() {

    private var _binding: FragmentSelectedWorkersBinding? = null
    private val binding: FragmentSelectedWorkersBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val args: SelectedWorkersFragmentArgs by navArgs()
    private val adapter = WorkersListRVAdapter()
    private lateinit var workers: ArrayList<WorkerData>
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedWorkersBinding.inflate(layoutInflater)
        changeStatusColorMainColor()
        hideBottomMenu()
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
    }

    private fun loadViews() {
        bottomSheetBehavior =  BottomSheetBehavior.from(binding.workerInfo.bottomSheetWorker)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.hide()
        binding.continueButton.text = getString(R.string.send_n_request, 0)
        binding.list.adapter = adapter
        workers = ArrayList(args.workers.list)
        adapter.submitList(workers.toMutableList())

        if (workers.size == 1) binding.checked.isChecked = true

        val selectAllListener = CompoundButton.OnCheckedChangeListener { _, state ->
            adapter.selectAllWorkers(state)
        }
        binding.checked.setOnCheckedChangeListener(selectAllListener)

        adapter.setOnWorkerClickListener {
            setWorkerData(it)
        }

        adapter.setOnAllWorkersSelectListener {
            if (it) {
                workers.clear()
                workers.addAll(args.workers.list)
            } else {
                workers.clear()
            }
            binding.continueButton.text = getString(R.string.send_n_request, workers.size)
        }

        binding.continueButton.text = getString(R.string.send_n_request, workers.size)
        adapter.setOnWorkerCheckListener {
            if (it.isSelected) {
                workers.add(it)
            } else {
                workers.remove(it)
                binding.checked.setOnCheckedChangeListener(null)
                binding.checked.isChecked = false
                binding.checked.setOnCheckedChangeListener(selectAllListener)
            }
            binding.continueButton.alpha = if (workers.isEmpty()) 0.5f else 1f

            if (workers.size == args.workers.list.size) {
                binding.checked.setOnCheckedChangeListener(null)
                binding.checked.isChecked = true
                binding.checked.setOnCheckedChangeListener(selectAllListener)
            }

            binding.continueButton.text = getString(R.string.send_n_request, workers.size)
        }

        binding.continueButton.setOnClickListener {
            if (workers.isEmpty()) return@setOnClickListener
            val action =
                SelectedWorkersFragmentDirections.actionSelectedWorkersFragmentToSendWorkersRequestFragment(WorkersList(workers), args.vacanyData)
            findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}