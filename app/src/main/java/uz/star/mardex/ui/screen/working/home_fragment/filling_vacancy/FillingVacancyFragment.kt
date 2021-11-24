package uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentFillingVacancyBinding
import uz.star.mardex.databinding.LayoutChooseDialogBinding
import uz.star.mardex.model.data_exchange.SocketRequestModel
import uz.star.mardex.model.requests.location.LocationRequest
import uz.star.mardex.model.response.server.ImagePath
import uz.star.mardex.ui.adapter.recycler_view.ImagesRVAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.ImageHelper
import uz.star.mardex.utils.helpers.NIGHT_MODE
import uz.star.mardex.utils.helpers.showMessage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class FillingVacancyFragment : Fragment() {

    private var _binding: FragmentFillingVacancyBinding? = null
    private val binding: FragmentFillingVacancyBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    @Inject
    lateinit var storage: LocalStorage

    private val args: FillingVacancyFragmentArgs by navArgs()
    private var workerCount = 1
    private var adapter = ImagesRVAdapter()
    private var point: Point? = null

    private var image: File? = null
    private val imagesList = ArrayList<String>().apply { add("") }
    private val viewModel: FillingVacancyViewModel by viewModels()
    private var gotPointFromMap = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFillingVacancyBinding.inflate(layoutInflater)
        changeStatusColorWhite()
        hideBottomMenu()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadObservers()
    }

    private fun loadViews() {
        binding.job.navigateIcon.invisible()
        binding.job.jobImage.loadImageUrl(args.job.pic)
        binding.job.jobName.text = args.job.title.title()
        binding.parentLayout.mapView = binding.map
        binding.map.map.isNightModeEnabled = LocalStorage.instance.nightMode == NIGHT_MODE

        if (!gotPointFromMap)
            getLocation { success, point ->
                if (success && point != null) {
                    this.point = point
                    addRedPlaceMark(binding.map, Point(LocalStorage.instance.currentLat, LocalStorage.instance.currentLong), "")
                    binding.map.navigateToPoint(Point(LocalStorage.instance.currentLat, LocalStorage.instance.currentLong), 14f)
                }
            } else {
            this.point = Point(LocalStorage.instance.currentLat, LocalStorage.instance.currentLong)
            addRedPlaceMark(binding.map, point!!, "")
            binding.map.navigateToPoint(point!!, 14f)

        }
        gotPointFromMap = true
        binding.btnOpenMap.setOnClickListener {
            findNavController().navigate(R.id.action_fillingVacancyFragment_to_mapFragment)
        }

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.plusWorkerCount.setOnClickListener {
            workerCount++
            binding.workerCount.text = workerCount.toString()
        }

        binding.minusWorkerCount.setOnClickListener {
            if (workerCount != 1) workerCount--
            binding.workerCount.text = workerCount.toString()
        }

        binding.sendRequest.setOnClickListener {
            if (binding.description.text.isNullOrEmpty()) {
                binding.description.error = getString(R.string.description)
                binding.description.requestFocus()
                return@setOnClickListener
            }
            if (binding.price.error != null) {
                binding.price.requestFocus()
                return@setOnClickListener
            }
            val vacanyData = SocketRequestModel(
                "",
                "",
                args.job.title,
                binding.description.text.toString(),
                binding.price.formattedString + " " + binding.spnCurrency.selectedItem,
                binding.descriptionFull.text.toString(),
                workerCount,
                LocationRequest(listOf(storage.currentLat, storage.currentLong)),
                "",
                "",
                imagesList,
                args.job.id
            )
            hideKeyboard(binding.root)
            val action = FillingVacancyFragmentDirections.actionFillingVacancyFragmentToWorkersFragment(args.job, vacanyData)
            findNavController().navigate(action)
        }
        binding.listPhotos.adapter = adapter
        adapter.submitList(imagesList)
        adapter.setOnAddListener {
            showSuggestDialog()
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnCurrency.adapter = adapter
        }

        binding.workerCount.text = workerCount.toString()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun loadObservers() {
        viewModel.message.observe(this, showMessage())
        viewModel.imagePath.observe(this, imagePathObserver)
    }

    private val imagePathObserver = Observer<ImagePath> {
        imagesList.add(it.path)
        hideLoader()
        adapter.submitList(imagesList)
        adapter.notifyItemInserted(imagesList.size - 1)
    }

    private fun showSuggestDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
        val view = LayoutChooseDialogBinding.inflate(layoutInflater)
        dialog.setContentView(view.root)
        dialog.dismissWithAnimation = true
        view.takePhotoButton.hide()
        /*view.takePhotoButton.setOnClickListener {
            getImageFromCamera()
            dialog.dismiss()
            ,.,.,<><><,.,.,<></>
        }*/
        view.takeFromGalleryButton.setOnClickListener {
            getImageFromGallery()
            dialog.dismiss()
        }
        dialog.show()
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
        if (image != null)
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

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

}