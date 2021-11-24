package uz.star.mardex.ui.screen.entry.reset_passpord

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentRestorePasswordBinding
import uz.star.mardex.model.requests.login.LoginData
import uz.star.mardex.model.requests.registration.RegistrationData
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.utils.extension.hideEntryLoader
import uz.star.mardex.utils.extension.hideKeyboard
import uz.star.mardex.utils.extension.showEntryLoader
import uz.star.mardex.utils.helpers.showAlertDialog
import javax.inject.Inject

@AndroidEntryPoint
class RestorePasswordFragment : Fragment() {

    private var _binding: FragmentRestorePasswordBinding? = null
    private val binding: FragmentRestorePasswordBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: RestorePasswordViewModel by viewModels()

    @Inject
    lateinit var storage: LocalStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestorePasswordBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadObservers()
        loadViews()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.responseRestorePassword.observe(this, responseRestorePassword)
        viewModel.message.observe(this, messageObserver)
        viewModel.loader.observe(this, loaderObserver)

    }

    private val responseRestorePassword = Observer<LoginData> { loginData ->
        findNavController().navigate(RestorePasswordFragmentDirections.actionRestorePasswordFragmentToSmsVerificationFragment(
            RegistrationData(null, "restore", loginData.password, "+998${binding.etPhone.rawText}", -1)
        ))
    }

    private fun loadViews() {
        binding.etPhone.setOnEditorActionListener { v, actionId, event ->
            actionId == EditorInfo.IME_ACTION_DONE
        }

        binding.apply {
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            binding.btnNext.setOnClickListener {
                viewModel.openCodeVerifyScreen(
                    "+998" + binding.etPhone.rawText,
                    binding.etPassword.text.toString(),
                    binding.etConfirmPassword.text.toString()
                )
            }
        }
    }


    private val loaderObserver = Observer<Boolean> { b ->
        if (b) showEntryLoader() else hideEntryLoader()
    }

    private val messageObserver = Observer<MessageData> {
        hideEntryLoader()
        it.onResource {
            showAlertDialog(it)
        }.onMessage {
            showAlertDialog(it)
        }
    }
}