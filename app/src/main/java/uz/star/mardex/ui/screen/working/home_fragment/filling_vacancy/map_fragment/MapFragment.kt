package uz.star.mardex.ui.screen.working.home_fragment.filling_vacancy.map_fragment

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error
import dagger.hilt.android.AndroidEntryPoint
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.FragmentMapBinding
import uz.star.mardex.model.response.PlaceModel
import uz.star.mardex.ui.adapter.recycler_view.map_search_results.ResultAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.NIGHT_MODE
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var isNight = LocalStorage.instance.nightMode == NIGHT_MODE

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")


    @Inject
    lateinit var storage: LocalStorage

    private var clickedPlaceModel: PlaceModel? = null
    private val currentPoint: Point by lazy { Point(storage.currentLat, storage.currentLong) }
    private var tappedPoint: Point? = null

    private lateinit var adapter: ResultAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(layoutInflater)
        hideBottomMenu()
        hideKeyboard(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
        loadMapListeners()
    }

    private fun loadViews() {
        binding.mapView.map.isNightModeEnabled = isNight
        binding.btnNight.setOnClickListener {
            if (isNight) {
                binding.btnNight.setImageResource(R.drawable.ic_night_map)
                binding.mapView.map.isNightModeEnabled = false
                isNight = false
            } else {
                binding.btnNight.setImageResource(R.drawable.ic_day_map)
                binding.mapView.map.isNightModeEnabled = true
                isNight = true
            }
        }

        binding.rvResults.layoutManager = LinearLayoutManager(requireContext())
        adapter = ResultAdapter(arrayListOf())
        binding.rvResults.adapter = adapter

        adapter.setOnclickItemListener { placeModel ->

            clickedPlaceModel = placeModel
            val point = Point(placeModel.latitude, placeModel.longitude)
            moveCameraPosition(point)
            hideKeyboard(binding.root)
            binding.etSearch.setText("")
            clickedPlaceModel?.let { clickedPlaceModel ->
                setBottomSheet(clickedPlaceModel)
            }
        }
    }

    private fun loadMapListeners() {
        moveCameraPosition(currentPoint)
        binding.mapView.map.apply {
            addCameraListener(cameraPositionListener)
            addTapListener(geoObjectTapListener)
        }
        loadSuggestListener()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val geoObjectTapListener = GeoObjectTapListener { geoObjectTapEvent ->

        val selectionMetadata = geoObjectTapEvent.geoObject.metadataContainer.getItem(
            GeoObjectSelectionMetadata::class.java
        )

        val businessData =
            geoObjectTapEvent.geoObject.metadataContainer.getItem(BusinessRating1xObjectMetadata::class.java)

        val currentLocation = Location("current")
        val foundLocation = Location("found")
        var distance = 0F

        val title = geoObjectTapEvent.geoObject.name
        val subTitle = geoObjectTapEvent.geoObject.descriptionText
        val latitude = geoObjectTapEvent.geoObject.geometry[0].point?.latitude
        val longitude = geoObjectTapEvent.geoObject.geometry[0].point?.longitude
        if (latitude != 0.0) {
            foundLocation.latitude = latitude!!
            foundLocation.longitude = longitude!!
            distance = currentLocation.distanceTo(foundLocation)
        }

        if (latitude != null && longitude != null) {
            tappedPoint = Point(latitude, longitude)
            hideKeyboard(binding.root)
            moveCameraPosition(tappedPoint!!)

            val placeModel = PlaceModel(
                title = title ?: " Title Not found",
                subtitle = subTitle ?: "Description Not found",
                distance = if (distance == 0.0F) "Nan" else distance.metrToKM(),
                allReview = null,
                score = null,
                longitude = longitude,
                latitude = latitude
            )

            setBottomSheet(placeModel)
        }
        selectionMetadata != null
    }


    private val cameraPositionListener =
        CameraListener { p0, cameraPosition, cameraUpdateReason, isStopped ->

            binding.btnPin.changeFormer()
            binding.shade.changeFormerShade()
            var lat = cameraPosition.target.latitude
            var long = cameraPosition.target.longitude

            if (cameraPosition.target.latitude != 0.0) {
                if (isStopped) {
                    binding.mapView.map.deselectGeoObject()
                    binding.btnPin.resumeFormer()
                    binding.shade.resumeFormerShade()
                }
            }
        }


    private fun loadSuggestListener() {
        val searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(text: Editable?) {

                    Log.d("tto", "dddddd tto ttto ttto ttto ttto ttto tttto ttttto ")
                }

                override fun beforeTextChanged(
                    text: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onTextChanged(
                    text: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    val searchOption = SearchOptions()
                    searchOption.snippets = Snippet.BUSINESS_RATING1X.value
                    searchManager.submit(
                        text.toString(),
                        VisibleRegionUtils.toPolygon(binding.mapView.map.visibleRegion),
                        searchOption,
                        searchListener
                    )

                    var drawable = requireActivity().resources.getDrawable(R.drawable.ic_close)

                    if (text.toString().trim() == "") {
                        binding.rvResults.hide()
                    } else {
                        binding.rvResults.show()
                        hideBottomSheet()
                    }
                }
            })
    }


    private val searchListener = object : Session.SearchListener {
        override fun onSearchError(p0: Error) {
        }

        override fun onSearchResponse(response: Response) {
            val resultList = ArrayList<PlaceModel>()

            for (suggest in response.collection.children) {

                val currentLocation = Location("currentLocation")
                currentLocation.latitude = currentPoint.latitude
                currentLocation.longitude = currentPoint.longitude

                val foundPoint = suggest.obj?.geometry?.get(0)?.point
                val foundLocation = Location("foundLocation")
                var distanceOf = 0F

                if (foundPoint != null) {
                    foundLocation.latitude = foundPoint.latitude
                    foundLocation.latitude = foundPoint.longitude
                    distanceOf = currentLocation.distanceTo(foundLocation)
                }

                val placeMoreData =
                    suggest.obj?.metadataContainer?.getItem(BusinessRating1xObjectMetadata::class.java)

                Log.d("TUU", "$placeMoreData")

                val placeM = PlaceModel(
                    title = suggest.obj?.name.toString(),
                    subtitle = suggest.obj?.descriptionText.toString(),
                    distance = distanceOf.metrToKM(),
                    allReview = placeMoreData?.ratings,
                    score = placeMoreData?.score,
                    latitude = foundPoint!!.latitude,
                    longitude = foundPoint!!.longitude
                )
                Log.d("1997O", "placeM : ")
                resultList.add(placeM)
            }
            adapter?.submitList(resultList)
        }
    }


    private fun moveCameraPosition(point: Point) {
        binding.mapView.map.move(
            CameraPosition(point, 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.7f),
            null
        )
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    private fun setBottomSheet(selectedPlaceM: PlaceModel) {
        showBottomSheet()
        binding.bottom.apply {
            txtAddressName.text = selectedPlaceM.title

            btnAcceptAddress.setOnClickListener {
                storage.currentLat = selectedPlaceM.latitude
                storage.currentLong = selectedPlaceM.longitude
                findNavController().popBackStack()
            }
        }
    }


    private fun hideBottomSheet() {
        binding.bottom.root.hide()
    }

    private fun showBottomSheet() {
        binding.bottom.root.show()
    }
}