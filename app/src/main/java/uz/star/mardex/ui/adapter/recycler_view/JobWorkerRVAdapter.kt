package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.WorkerJobsItemRecyclerBinding
import uz.star.mardex.model.response.server.jobs.Job
import uz.star.mardex.utils.extension.loadImageUrl
import uz.star.mardex.utils.extension.title

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

class JobWorkerRVAdapter : ListAdapter<Job, JobWorkerRVAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    inner class ViewHolder(private val binding: WorkerJobsItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val data = getItem(adapterPosition)
            binding.jobName.text = data.title.title()
            binding.jobImageItem.loadImageUrl(data.pic)
            binding.root.setOnClickListener {

            }
        }
    }

    companion object {
        var DIFF_JOB_CALLBACK = object : DiffUtil.ItemCallback<Job>() {
            override fun areItemsTheSame(oldItem: Job, newItem: Job) =
                newItem.id == oldItem.id

            override fun areContentsTheSame(oldItem: Job, newItem: Job) =
                newItem.pic == oldItem.pic && newItem.createdAt == oldItem.createdAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        WorkerJobsItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}