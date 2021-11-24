package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.R
import uz.star.mardex.databinding.WorkerItemSelectableBinding
import uz.star.mardex.databinding.WorkerItemShowOnlyBinding
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.SingleBlock

/**
 * Created by Farhod Tohirov on 12-Jan-21
 **/

class WorkersShowOnlyRVAdapter : ListAdapter<WorkerData, WorkersShowOnlyRVAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    inner class ViewHolder(private val binding: WorkerItemShowOnlyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val workerData = getItem(adapterPosition)
            binding.name.text = workerData.fullName
            workerData.avatar.isNotNull({
                binding.avatar.loadImageUrl(workerData.avatar!!, isProfile = true, isWorker = true)
            }, {
                binding.avatar.loadImageUrl("", isProfile = true, isWorker = true)
            })
            workerData.sumMark.isNotNull({
                binding.rating.show()
                binding.ratingText.show()
                binding.rating.rating = workerData.sumMark.rating()
                binding.ratingText.text = workerData.sumMark.rating().toString().substring(0, 3)
            }, {
                binding.rating.hide()
                binding.ratingText.hide()
            })
        }
    }

    companion object {
        var DIFF_JOB_CALLBACK = object : DiffUtil.ItemCallback<WorkerData>() {
            override fun areItemsTheSame(oldItem: WorkerData, newItem: WorkerData) =
                newItem.id == oldItem.id

            override fun areContentsTheSame(oldItem: WorkerData, newItem: WorkerData): Boolean {
                return newItem.fullName == oldItem.fullName && newItem.balance == oldItem.balance &&
                        newItem.createdAt == oldItem.createdAt && newItem.isSelected == oldItem.isSelected
                        && newItem.avatar == oldItem.avatar

            }
        }

        lateinit var instance: WorkersShowOnlyRVAdapter; private set

        fun init() {
            instance = WorkersShowOnlyRVAdapter()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        WorkerItemShowOnlyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}