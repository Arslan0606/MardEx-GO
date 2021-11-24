package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.CategoryItemRecyclerBinding
import uz.star.mardex.model.response.server.jobs.Job
import uz.star.mardex.model.response.server.jobs.JobCategory
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.loadImageUrl
import uz.star.mardex.utils.extension.show
import uz.star.mardex.utils.extension.title
import uz.star.mardex.utils.helpers.DoubleBlock

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

class JobCategoryRVAdapter() : ListAdapter<JobCategory, JobCategoryRVAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    private var listenerSelectCategory: DoubleBlock<List<Job>, String?>? = null
    var lastSelectedIndex = 0
    private var listBackgrounds: ArrayList<View> = arrayListOf()

    inner class ViewHolder(private val binding: CategoryItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            listBackgrounds.add(binding.selected)

            binding.category.setOnClickListener {
                if (lastSelectedIndex == adapterPosition)
                    return@setOnClickListener

                val data = getItem(adapterPosition)
                getItem(lastSelectedIndex).isCollapsed = false
                data.isCollapsed = true

                removeBgLines()
                binding.selected.show()

                lastSelectedIndex = adapterPosition
                val dataTitle = data.categoryTitle
                val title = dataTitle.title()
                listenerSelectCategory?.invoke(data.jobs, title)
            }
        }

        fun bind() {
            if (getItem(adapterPosition).isCollapsed) {
                binding.selected.show()
            } else
                binding.selected.hide()

            val data = getItem(adapterPosition)

            val dataTitle = data.categoryTitle
            val title = dataTitle.title()
            binding.name.text = title

            data.pic?.let { pic ->
                binding.image.loadImageUrl(pic)
            }
        }
    }

    private fun removeBgLines() {
        listBackgrounds.forEach {
            it.hide()
        }
    }

    companion object {
        var DIFF_JOB_CALLBACK = object : DiffUtil.ItemCallback<JobCategory>() {
            override fun areItemsTheSame(oldItem: JobCategory, newItem: JobCategory) =
                newItem == oldItem

            override fun areContentsTheSame(
                oldItem: JobCategory,
                newItem: JobCategory
            ) = newItem.categoryTitle == oldItem.categoryTitle
                    && newItem.pic == oldItem.pic
                    && newItem.hashCode() == oldItem.hashCode()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        CategoryItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    fun setOnCategoryItemClickListener(f: DoubleBlock<List<Job>, String?>) {
        this.listenerSelectCategory = f
    }
}