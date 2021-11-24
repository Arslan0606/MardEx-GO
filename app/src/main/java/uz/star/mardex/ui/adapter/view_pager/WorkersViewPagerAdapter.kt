package uz.star.mardex.ui.adapter.view_pager

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.list.ConnectedWorkersListItemFragment
import uz.star.mardex.ui.screen.working.connected_workers.map.ConnectedWorkersMapFragment
import uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.list.WorkersListFragment
import uz.star.mardex.ui.screen.working.home_fragment.workers_fragment.map.WorkersMapFragment

/**
 * Created by Farhod Tohirov on 08-Jan-21
 **/

class WorkersViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val workersListFragment = WorkersListFragment()
    private val workersMapFragment = WorkersMapFragment()


    override fun getItemCount() = 2
    override fun createFragment(position: Int) = when (position) {
        1 -> workersListFragment
        else -> workersMapFragment
    }

    fun submitList(list: List<WorkerData>) {
        workersMapFragment.submitList(list)
    }

    fun allWorkersSelected(state: Boolean){
        workersListFragment.changeSelectedSwitch(state)
    }
}