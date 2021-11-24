package uz.star.mardex.ui.screen.working.home_fragment.promocode

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.databinding.FragmentPromocodeBinding
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.utils.extension.*

@AndroidEntryPoint
class PromocodeFragment : Fragment() {

    private var _binding: FragmentPromocodeBinding? = null
    private val binding: FragmentPromocodeBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: PromocodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromocodeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.btnNext.setOnClickListener {
            val promocode = binding.etPromo.rawText.toString()
            if (promocode.length != 5) return@setOnClickListener
            viewModel.sendPromocode(promocode)
            showLoader()
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, messageObserver)
        viewModel.successData.observe(this, successObserver)
    }

    private val successObserver = Observer<Boolean> {
        hideLoader()
        binding.promoError.show()
        if (it) {
            binding.promoError.setText(R.string.success_promo)
        } else {
            binding.promoError.setText(R.string.error_promocode)
        }
    }

    private val messageObserver = Observer<MessageData> {
        hideLoader()
        binding.promoError.show()
        it.onMessage {
            binding.promoError.text = it
        }.onResource {
            binding.promoError.setText(it)
        }
    }

    override fun onResume() {
        super.onResume()
        hideBottomMenu()
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(binding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideKeyboard(binding.root)
        _binding = null
    }
}