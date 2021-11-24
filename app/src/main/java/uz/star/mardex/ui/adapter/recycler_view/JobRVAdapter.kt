package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.CategoryItemRecyclerBinding
import uz.star.mardex.databinding.JobsItemRecyclerBinding
import uz.star.mardex.model.response.server.jobs.Job
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.loadImageUrl
import uz.star.mardex.utils.extension.show
import uz.star.mardex.utils.extension.title
import uz.star.mardex.utils.helpers.DoubleBlock

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

class JobRVAdapter : ListAdapter<Job, JobRVAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    private var listenerCategoryClick: DoubleBlock<Job, Int>? = null

    inner class ViewHolder(private val binding: JobsItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val data = getItem(adapterPosition)
            binding.jobName.text = data.title.title()
            binding.jobImage.loadImageUrl(data.pic)
            binding.root.setOnClickListener {
                listenerCategoryClick?.invoke(data, adapterPosition)
            }
        }
    }

    fun setOnJobSelectedListener(f: DoubleBlock<Job, Int>) {
        listenerCategoryClick = f
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
        JobsItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}