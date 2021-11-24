package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.WorkerItemSelectableBinding
import uz.star.mardex.model.response.server.worker.WorkerData
import uz.star.mardex.utils.extension.*
import uz.star.mardex.utils.helpers.SingleBlock

/**
 * Created by Farhod Tohirov on 12-Jan-21
 **/

class WorkersListRVAdapter : ListAdapter<WorkerData, WorkersListRVAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    private var workerClickListener: SingleBlock<WorkerData>? = null
    private var workerCheckListener: SingleBlock<WorkerData>? = null
    private var allWorkersSelect: SingleBlock<Boolean>? = null

    inner class ViewHolder(private val binding: WorkerItemSelectableBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val workerData = getItem(adapterPosition)
            binding.name.text = workerData.fullName
            workerData.avatar.isNotNull({
                binding.avatar.loadImageUrl(workerData.avatar!!, isProfile = true, isWorker = true)
            }, {
                binding.avatar.loadImageUrl("", isProfile = true, isWorker = true)
            })
            binding.checked.setOnCheckedChangeListener(null)
            binding.checked.isChecked = workerData.isSelected
            binding.checked.setOnCheckedChangeListener { _, isChecked ->
                workerData.isSelected = isChecked
                notifyItemChanged(adapterPosition)
                workerCheckListener?.invoke(workerData)
            }
            workerData.sumMark.isNotNull({
                binding.rating.show()
                binding.ratingText.show()
                binding.rating.rating = workerData.sumMark.rating()
                binding.ratingText.text = workerData.sumMark.rating().toString().substring(0, 3)
            }, {
                binding.rating.hide()
                binding.ratingText.hide()
            })

            binding.moreButton.setOnClickListener { workerClickListener?.invoke(workerData) }
        }
    }

    fun selectAllWorkers(state: Boolean) {
        currentList.forEachIndexed { index, workerData ->
            workerData.isSelected = state
            notifyItemChanged(index)
        }
        allWorkersSelect?.invoke(state)
    }

    fun setOnWorkerClickListener(f: SingleBlock<WorkerData>) {
        workerClickListener = f
    }

    fun setOnAllWorkersSelectListener(f: SingleBlock<Boolean>) {
        allWorkersSelect = f
    }

    fun setOnWorkerCheckListener(f: SingleBlock<WorkerData>) {
        workerCheckListener = f
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

        val instance: WorkersListRVAdapter
            get() {
                if (_instance == null)
                    _instance = WorkersListRVAdapter()
                return _instance!!
            }

        private var _instance: WorkersListRVAdapter? = null

        fun init() {
            _instance = WorkersListRVAdapter()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        WorkerItemSelectableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}