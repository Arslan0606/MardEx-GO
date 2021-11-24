package uz.star.mardex.ui.screen.working.profile.editpersonaldata_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentEditPersonalDataBinding
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.utils.extension.changeStatusColorWhite
import uz.star.mardex.utils.extension.hideBottomMenu
import uz.star.mardex.utils.extension.hideKeyboard
import uz.star.mardex.utils.liveData.EventObserver
import javax.inject.Inject

@AndroidEntryPoint
class EditPersonalDataFragment : Fragment() {

    @Inject
    lateinit var storage: LocalStorage
    private val viewModel: EditPersonalDataViewModel by viewModels()

    private var _binding: FragmentEditPersonalDataBinding? = null
    private val binding: FragmentEditPersonalDataBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomMenu()
        changeStatusColorWhite()
        _binding = FragmentEditPersonalDataBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.btnBack.setOnClickListener {
            viewModel.backToProfileFragment()
            hideKeyboard(binding.btnBack)
        }

        binding.btnSave.setOnClickListener {
            viewModel.updateUser(
                binding.etFullName.text.toString().trim()
            )
            hideKeyboard(binding.btnSave)
        }
        binding.etFullName.setText(storage.name)
    }


    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.responseUserData.observe(this, responseUserDataObserver)
        viewModel.updateUserData.observe(this, updateUserDataObserver)
        viewModel.message.observe(this, messageObserver)
        viewModel.backToProfileFragmentData.observe(this, backDataObserver)

        viewModel.getUserData()
    }

    private val backDataObserver = EventObserver<Unit> {
        findNavController().popBackStack()
    }

    private val messageObserver = Observer<MessageData> {}
    private val updateUserDataObserver = Observer<UserData> {


    }
    private val responseUserDataObserver = Observer<UserData> {
        binding.etFullName.setText(it.name)
    }

}