package uz.star.mardex.ui.screen.entry.login

import android.annotation.SuppressLint
import android.content.Intent
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
import uz.star.mardex.databinding.FragmentLoginBinding
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.utils.extension.hideEntryLoader
import uz.star.mardex.utils.extension.hideKeyboard
import uz.star.mardex.utils.extension.showEntryLoader
import uz.star.mardex.utils.helpers.showAlertDialog

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.btnEnter.setOnClickListener {
            showEntryLoader()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.login(phone, password)
        }

        binding.btnGoSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnResetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_restorePasswordFragment)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, messageObserver)
        viewModel.login.observe(this, loginDataObserver)
    }

    private val loginDataObserver = Observer<UserData> {
        showAlertDialog(it.name)
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private val messageObserver = Observer<MessageData> {
        hideEntryLoader()
        it.onResource {
            showAlertDialog(it)
        }.onMessage {
            showAlertDialog(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}