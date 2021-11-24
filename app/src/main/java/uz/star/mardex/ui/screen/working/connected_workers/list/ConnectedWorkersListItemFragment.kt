package uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import uz.star.mardex.databinding.ConnectedWorkersListItemBinding
import uz.star.mardex.databinding.WorkersListItemBinding
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.adapter.recycler_view.ConnectedWorkersListRVAdapter
import uz.star.mardex.ui.adapter.recycler_view.WorkersListRVAdapter
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.show

/**
 * Created by Farhod Tohirov on 21-Mar-21
 **/

class ConnectedWorkersListItemFragment : Fragment() {

    private var _binding: ConnectedWorkersListItemBinding? = null
    private val binding: ConnectedWorkersListItemBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")

    private val adapter = ConnectedWorkersListRVAdapter.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ConnectedWorkersListItemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadViews()
    }

    private fun loadViews() {
        binding.list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}