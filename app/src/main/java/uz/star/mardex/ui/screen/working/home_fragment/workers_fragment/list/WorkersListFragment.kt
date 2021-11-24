package uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import uz.star.mardex.databinding.WorkersListItemBinding
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.WorkersListRVAdapter
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.show

/**
 * Created by Farhod Tohirov on 21-Mar-21
 **/

class WorkersListFragment : Fragment() {

    private var _binding: WorkersListItemBinding? = null
    private val binding: WorkersListItemBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val adapter = WorkersListRVAdapter.instance
    var selectAllListener :CompoundButton.OnCheckedChangeListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = WorkersListItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
    }

    private fun loadViews() {

        if (adapter.currentList.size == 0) {
            binding.checked.hide()
            binding.txtAll.hide()
        } else {
            binding.checked.show()
            binding.txtAll.show()
        }

        binding.list.adapter = adapter
        selectAllListener = CompoundButton.OnCheckedChangeListener { _, state ->
            adapter.selectAllWorkers(state)
        }
        binding.checked.setOnCheckedChangeListener(selectAllListener)
    }

    fun changeSelectedSwitch(state: Boolean){
        binding.checked.setOnCheckedChangeListener(null)
        binding.checked.isChecked = state
        binding.checked.setOnCheckedChangeListener(selectAllListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}