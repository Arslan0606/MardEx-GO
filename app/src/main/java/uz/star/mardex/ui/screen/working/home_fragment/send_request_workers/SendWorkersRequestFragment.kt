package uz.star.mardex.ui.screen.working.home_fragment.send_request_workers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentSendWorkersRequestBinding
import uz.star.mardex.model.data_exchange.SocketResponseData
import uz.star.mardex.model.data_exchange.WorkersList
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.WorkersShowOnlyRVAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.showAlertDialog
import uz.star.mardex.utils.helpers.showMessage


@AndroidEntryPoint
class SendWorkersRequestFragment : Fragment() {

    private var _binding: FragmentSendWorkersRequestBinding? = null
    private val binding: FragmentSendWorkersRequestBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val args: SendWorkersRequestFragmentArgs by navArgs()
    private val viewModel: SendWorkersRequestViewModel by viewModels()
    private val workers = ArrayList<WorkerData>()
    private val adapter = WorkersShowOnlyRVAdapter()
    private var barSection = -1.0
    private var progress = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSendWorkersRequestBinding.inflate(layoutInflater)
        hideBottomMenu()
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.callAnotherWorkers.setOnClickListener {
            viewModel.cancelSocketRequest()
            findNavController().popBackStack()
        }
        binding.back.setOnClickListener {
            viewModel.cancelSocketRequest()
            findNavController().popBackStack()
        }

        binding.workerCountText.text = getString(R.string.send_to_n_workers, args.workers.list.size)
        binding.list.adapter = adapter
        barSection = 100.0 / args.workers.list.size / 25.0
        viewModel.sendVerification(args.workers.list, args.vacanyData)

        loadBackPress()
    }

    private fun loadBackPress() {
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                viewModel.cancelSocketRequest()
            }
            false;
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, showMessage())
        viewModel.workerResponse.observe(this, workerResponseObserver)
        viewModel.sendingPosition.observe(this, sendPositionObserver)
        viewModel.sendingSeconds.observe(this, sendSecondsObserver)
        viewModel.finishSending.observe(this, finishSendingObserver)
    }

    private val workerResponseObserver = Observer<SocketResponseData> {
        if (it.userId == LocalStorage.instance.id) {
            args.workers.list.forEach { workerData ->
                if (it.workerId == workerData.id) {
                    workers.add(workerData)
                    return@forEach
                }
            }
            adapter.submitList(workers)
        }
    }

    @SuppressLint("SetTextI18n")
    private val sendPositionObserver = Observer<Int> { pos ->
        progress = if (pos == 0) 0.0 else ((pos).toFloat() / args.workers.list.size.toFloat() * 100).toDouble()
        setProgress(progress)
        binding.workerCount.text = "${pos + 1}/${args.workers.list.size}"
        setWorkerOnRequestData(pos)
    }

    private fun setWorkerOnRequestData(pos: Int) {
        val workerOnRequest = args.workers.list[pos]
        binding.workerOnRequest.name.text = workerOnRequest.fullName
        binding.workerOnRequest.avatar.loadImageUrl(workerOnRequest.avatar ?: "", isProfile = true, isWorker = true)
        workerOnRequest.sumMark.isNotNull({
            binding.workerOnRequest.rating.show()
            binding.workerOnRequest.ratingText.show()
            binding.workerOnRequest.rating.rating = workerOnRequest.sumMark.rating()
            binding.workerOnRequest.ratingText.text = workerOnRequest.sumMark.rating().toString().substring(0, 3)
        }, {
            binding.workerOnRequest.rating.hide()
            binding.workerOnRequest.ratingText.hide()
        })
        binding.workerOnRequest.root.startAnimation(inFromRightAnimation())
    }

    private fun inFromRightAnimation(): Animation {
        val inFromRight: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        inFromRight.duration = 500
        inFromRight.interpolator = AccelerateInterpolator()
        return inFromRight
    }

    private val finishSendingObserver = Observer<Int> {
        try {
            binding.progressBar.setProgressDisplayAndInvalidate(100)
            showAlertDialog(getString(R.string.accepted_n_workers, it)) {
                if (it == 0) {
                    try {
                        findNavController().popBackStack()
                    } catch (e: Exception) {

                    }
                } else {
                    try {
                        val action =
                            SendWorkersRequestFragmentDirections.actionSendWorkersRequestFragmentToRespondedWorkersFragment(WorkersList(workers))
                        findNavController().navigate(action)
                    } catch (e: Exception) {

                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    private val sendSecondsObserver = Observer<Int> {
        progress += barSection
        setProgress(progress)
    }

    private fun setProgress(progress: Double) {
        binding.progressBar.setProgressDisplayAndInvalidate(progress.toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.disconnectSocket()
    }

}