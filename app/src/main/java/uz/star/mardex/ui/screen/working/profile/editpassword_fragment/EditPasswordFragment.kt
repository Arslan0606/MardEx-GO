package uz.star.mardex.ui.screen.working.profile.editpassword_fragment

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
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentEditPasswordBinding
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.changeStatusColorWhite
import uz.star.mardex.utils.extension.hideBottomMenu
import uz.star.mardex.utils.extension.hideKeyboard
import uz.star.mardex.utils.helpers.showAlertDialog
import uz.star.mardex.utils.liveData.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class EditPasswordFragment : Fragment() {

    private val viewModel: EditPasswordViewModel by viewModels()

    private var _binding: FragmentEditPasswordBinding? = null
    private val binding: FragmentEditPasswordBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    @Inject
    lateinit var storage: LocalStorage


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomMenu()
        changeStatusColorWhite()
        _binding = FragmentEditPasswordBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.updateUserPasswordData.observe(this, updatePasswordObserver)
        viewModel.message.observe(this, messageObserver)
        viewModel.backToProfileFragmentData.observe(this, backDataObserver)
        viewModel.toastMessageData.observe(this, toastMessageObserver)
    }

    private val backDataObserver = EventObserver<Unit> {
        findNavController().popBackStack()
    }

    private val toastMessageObserver = Observer<String> { errorToastMessage ->
        showAlertDialog("$errorToastMessage")
    }

    private val messageObserver = Observer<MessageData> {}
    private val updatePasswordObserver = Observer<UserData> {
        showAlertDialog("$it")
    }

    private fun loadViews() {
        binding.apply {
            btnBackEditPassword.setOnClickListener {
                viewModel.backToProfileFragment()
                hideKeyboard(binding.root)
            }

            btnSave.setOnClickListener {

                viewModel.updateUserPassword(
                    oldPassword = etOldPassword.text.toString().trim(),
                    newPassword = etNewPassword.text.toString().trim(),
                    confirmPassword = etConfirmPassword.text.toString().trim()
                )
            }
        }
    }
}