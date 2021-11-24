package uz.star.mardex.ui.screen.working.connected_workers.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yandex.mapkit.geometry.Point
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.ConnectedWorkersMapItemBinding
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.ConnectedWorkersListRVAdapter
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.NIGHT_MODE

/**
 * Created by Farhod Tohirov on 21-Mar-21
 **/

class ConnectedWorkersMapFragment : Fragment() {

    private var _binding: ConnectedWorkersMapItemBinding? = null
    private val binding: ConnectedWorkersMapItemBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val adapter = ConnectedWorkersListRVAdapter.instance
    private val localStorage = LocalStorage.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ConnectedWorkersMapItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
    }

    private fun loadViews() {
        binding.workerList.adapter = adapter
        navigateCurrent()
        if (localStorage.nightMode != NIGHT_MODE) {
            binding.map.map.isNightModeEnabled = false
            binding.buttonDayNight.setImageResource(R.drawable.ic_day_night)
        } else {
            binding.map.map.isNightModeEnabled = true
            binding.buttonDayNight.setImageResource(R.drawable.ic_night)
        }

        binding.layoutDayNight.setOnClickListener {
            binding.map.map.isNightModeEnabled = !binding.map.map.isNightModeEnabled
            if (binding.map.map.isNightModeEnabled) {
                binding.buttonDayNight.setImageResource(R.drawable.ic_night)
            } else {
                binding.buttonDayNight.setImageResource(R.drawable.ic_day_night)
            }
        }


        binding.layoutNavigate.setOnClickListener { navigateCurrent() }
        binding.layoutZoomIn.setOnClickListener { binding.map.zoomIn() }
        binding.layoutZoomOut.setOnClickListener { binding.map.zoomOut() }
    }

    private fun navigateCurrent() {
        getLocation { state, point ->
            if (state && point != null) {
                binding.map.navigateToPoint(point)
                addRedPlaceMark(binding.map, point)
            } else {
                val point_1 = Point(localStorage.currentLat, localStorage.currentLong)
                binding.map.navigateToPoint(point_1)
                addRedPlaceMark(binding.map, point_1)
            }
        }
    }

    fun submitList(list: List<WorkerData>) {
        adapter.submitList(list)
        for (worker in list) {
            val point = Point(worker.location.coordinates[0], worker.location.coordinates[1])
            addWorkerPlaceMark(binding.map, point, worker.avatar.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}