package uz.star.mardex.ui.screen.working.home_fragment.responded_workers

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentRespondedWorkersBinding
import uz.star.mardex.databinding.RateUsBinding
import uz.star.mardex.model.requests.rate.Rate
import uz.star.mardex.model.requests.rate.RateDataComment
import uz.star.mardex.model.response.server.RateData
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.ConnectedWorkersListRVAdapter
import uz.star.mardex.ui.adapter.recycler_view.ImageSliderAdapter
import uz.star.mardex.ui.adapter.recycler_view.JobWorkerRVAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showMessage

@AndroidEntryPoint
class RespondedWorkersFragment : Fragment() {

    private var _binding: FragmentRespondedWorkersBinding? = null
    private val binding: FragmentRespondedWorkersBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val args: RespondedWorkersFragmentArgs by navArgs()
    private val viewModel: RespondedWorkersViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val adapter = ConnectedWorkersListRVAdapter()
    private var selectedWorkerData: WorkerData? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRespondedWorkersBinding.inflate(layoutInflater)
        hideBottomMenu()
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.connectWorker.observe(this, connectedWorkerObserver)
        viewModel.message.observe(this, showMessage())
        viewModel.commentsData.observe(this, commentsDataObserver)
        viewModel.removeWorker.observe(this, removeWorkerObserver)
        viewModel.cancellationComments.observe(this, cancellationCommentsObserver)
    }

    private val connectedWorkerObserver = Observer<Unit> {

    }

    private val removeWorkerObserver = Observer<WorkerData> {
        hideLoader()
        val workers = adapter.currentList.toMutableList()
        workers.remove(it)
        adapter.submitList(workers.toMutableList())
    }

    private val commentsDataObserver = Observer<List<RateData>> {
        hideLoader()
        val dialog = AlertDialog.Builder(requireActivity()).create()
        val binding = RateUsBinding.inflate(layoutInflater)
        binding.buttonCancel.setOnClickListener { dialog.dismiss() }
        val comments = ArrayList<ArrayList<RateData>>().apply {
            add(ArrayList())
            add(ArrayList())
            add(ArrayList())
            add(ArrayList())
            add(ArrayList())
        }
        for (rate in it) {
            comments[rate.relatedStar - 1].add(rate)
        }
        var r = 0
        binding.rateRatingBar.setOnRatingChangeListener { _, rating, _ ->
            r = rating.toInt()
            binding.reasonCheckbox1.isChecked = false
            binding.reasonCheckbox2.isChecked = false
            binding.reasonCheckbox3.isChecked = false
            if (r <= 3) {
                binding.commentText.show()
            } else {
                binding.commentText.hide()
            }
            try {
                binding.reasonText1.show()
                binding.reasonCheckbox1.show()
                binding.reasonText1.text = comments[r - 1][0].title.title()
            } catch (e: Exception) {
                binding.reasonText1.hide()
                binding.reasonCheckbox1.hide()
            }

            try {
                binding.reasonText2.show()
                binding.reasonCheckbox2.show()
                binding.reasonText2.text = comments[r - 1][1].title.title()
            } catch (e: Exception) {
                binding.reasonText2.hide()
                binding.reasonCheckbox2.hide()
            }

            try {
                binding.reasonText3.show()
                binding.reasonCheckbox3.show()
                binding.reasonText3.text = comments[r - 1][2].title.title()
            } catch (e: Exception) {
                binding.reasonText3.hide()
                binding.reasonCheckbox3.hide()
            }
        }

        binding.buttonSave.setOnClickListener {
            val selectedComments = ArrayList<String>()
            if (binding.reasonCheckbox1.isChecked) selectedComments.add(comments[r - 1][0].id)
            try {
                if (binding.reasonCheckbox2.isChecked) selectedComments.add(comments[r - 1][1].id)
            } catch (e: Exception) {
            }
            try {
                if (binding.reasonCheckbox3.isChecked) selectedComments.add(comments[r - 1][2].id)
            } catch (e: Exception) {

            }
            val rateDataComment = RateDataComment(
                Rate(LocalStorage.instance.id, selectedComments, r, binding.commentText.text.toString()),
                selectedWorkerData?.id ?: ""
            )
            viewModel.rateWorkerEndTask(rateDataComment, selectedWorkerData ?: return@setOnClickListener)
            dialog.dismiss()
            showLoader()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(binding.root)
        dialog.show()
    }

    private val cancellationCommentsObserver = Observer<Any> {
        hideLoader()
        val dialog = AlertDialog.Builder(requireActivity()).create()
        /*val binding = ReasonCancellationBinding.inflate(layoutInflater)
        binding.buttonCancel.setOnClickListener { dialog.dismiss() }
        binding.buttonSave.setOnClickListener {
            viewModel.cancelWorkerData(selectedWorkerData ?: return@setOnClickListener)
            dialog.dismiss()
        }*/
        dialog.setTitle(R.string.alert)
        dialog.setMessage(getText(R.string.cancel_worker_text))
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes)) { _, _ ->
            viewModel.cancelWorkerData(selectedWorkerData ?: return@setButton)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no)) { _, _ ->
            hideLoader()
            dialog.dismiss()
        }
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        }
        dialog.show()
    }

    private fun loadViews() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.workerInfo.bottomSheetWorker)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        binding.callAnotherWorkers.setOnClickListener { findNavController().popBackStack() }
        binding.workerCountText.text = getString(R.string.accepted_n_workers, args.workers.list.size)
        connectWorkersToClient()
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.isDraggable = true
        bottomSheetBehavior.hide()
        binding.list.adapter = adapter
        adapter.submitList(args.workers.list)

        adapter.setOnWorkerCallListener {
            callToWorker(it.phone)
        }
        adapter.setOnWorkerClickListener { setWorkerData(it) }
        binding.seeOnMapButton.setOnClickListener {
            findNavController().navigate(R.id.connectedWorkersFragment)
        }
    }

    private fun connectWorkersToClient() {
        for (worker in args.workers.list) {
            viewModel.connectWorkerToClient(worker)
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

        binding.workerInfo.callToWorker.setOnClickListener {
            callToWorker(workerData.phone)
        }

        binding.workerInfo.buttonFinish.setOnClickListener {
            bottomSheetBehavior.hide()
            selectedWorkerData = workerData
            viewModel.getCommentsData()
            showLoader()
        }
        binding.workerInfo.buttonCancel.setOnClickListener {
            bottomSheetBehavior.hide()
            selectedWorkerData = workerData
            viewModel.getCancellationComments()
            showLoader()
        }
    }

    private fun callToWorker(phone: String) {
        val callIntent = Intent(Intent.ACTION_DIAL)
        callIntent.data = Uri.fromParts("tel", phone, null)
        Toast.makeText(requireContext(), phone, Toast.LENGTH_SHORT).show()
        startActivity(callIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}