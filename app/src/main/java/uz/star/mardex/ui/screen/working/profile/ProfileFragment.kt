package uz.star.mardex.ui.screen.working.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.BuildConfig.APPLICATION_ID
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentProfileBinding
import uz.star.mardex.databinding.LayoutChooseDialogBinding
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.model.response.server.user.UserData
import uz.star.mardex.ui.screen.working.MainActivity
import uz.star.mardex.ui.screen.working.home_fragment.own_notifications_activity.OwnNotificationsActivity
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.ImageHelper
import uz.star.mardex.utils.helpers.showAlertDialog
import uz.star.mardex.utils.helpers.showMessageEvent
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    @Inject
    lateinit var storage: LocalStorage

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var uri: Uri
    private var image: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showBottomMenu()
        changeStatusColorMainColor()
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
        showBottomMenu()
    }

    private fun loadViews() {
        (activity as MainActivity).changeDataInMenu()
        binding.clientPhoto.loadImageUrl(storage.avatarPath)
        binding.changeAvatar.setOnClickListener {
            showSuggestDialog()
        }

        binding.btnPersonalData.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditPersonalDataFragment())
        }

        binding.btnClientPassword.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditPasswordFragment())
        }

        binding.privacy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mardex.uz/uz/public-offer"))
            startActivity(browserIntent)

        }

        binding.menu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }
        binding.clientName.text = storage.name
        binding.btnPhoneNumber.setOnClickListener {
            showAlertDialog(LocalStorage.instance.phone)
        }

        binding.news.setOnClickListener {
            startActivity(Intent(requireActivity(), OwnNotificationsActivity::class.java))
        }

        viewModel.getUserData()

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.imagePath.observe(this, imagePathObserver)
        viewModel.message.observe(this, showMessageEvent())
        viewModel.responseUserData.observe(this, userDataObserver)
        viewModel.updateUserData.observe(this, updateUserObserver)
    }

    private val updateUserObserver = Observer<UserData> {
        binding.clientPhoto.loadImageUrl(storage.avatarPath)
    }

    private val userDataObserver = Observer<UserData> {
        binding.clientName.text = it.name
        binding.clientPhoto.loadImageUrl(storage.avatarPath)
    }


    private val imagePathObserver = Observer<ImagePath> {
        hideLoader()
        storage.avatarPath = it.path
        viewModel.updateUserPicImage()
    }

    private fun showSuggestDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        val view = LayoutChooseDialogBinding.inflate(layoutInflater)
        dialog.setContentView(view.root)
        dialog.dismissWithAnimation = true
        view.takePhotoButton.setOnClickListener {
            getImageFromCamera()
            dialog.dismiss()
        }
        view.takeFromGalleryButton.setOnClickListener {
            getImageFromGallery()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getImageFromCamera() {
        val imageFile = createImageFile()
        uri = FileProvider.getUriForFile(
            requireActivity(),
            APPLICATION_ID,
            imageFile
        )
        takeImageFromCamera.launch(uri)
    }

    private val takeImageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            image = File(LocalStorage.instance.avatarPath)
            showLoader()
            ImageHelper.compressImage(requireContext(), image!!) {
                viewModel.uploadImage(it)
            }
        }
    }

    private fun getImageFromGallery() {
        getImageFromGallery.launch("image/*")
    }

    private val getImageFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri ?: return@registerForActivityResult
        val ins = requireActivity().contentResolver.openInputStream(uri)
        image = File.createTempFile("avatar", ".jpg", requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        val fileOutputStream = FileOutputStream(image)
        ins?.copyTo(fileOutputStream)
        ins?.close()
        fileOutputStream.close()
        LocalStorage.instance.avatarPath = image?.absolutePath ?: ""
        showLoader()
        ImageHelper.compressImage(requireContext(), image!!) {
            viewModel.uploadImage(it)
        }
    }


    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmm", Locale.US).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Avatar_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            LocalStorage.instance.avatarPath = absolutePath
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}