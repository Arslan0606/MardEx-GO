package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.databinding.ItemIntroBinding
import uz.star.mardex.model.response.server.intro.IntroData
import uz.star.mardex.utils.extension.loadImageUrl
import uz.star.mardex.utils.extension.title

/**
 * Created by Farhod Tohirov on 06-Jan-21
 **/

class IntroAdapter : ListAdapter<IntroData, IntroAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    inner class ViewHolder(private val binding: ItemIntroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            val title = item.title.title()
            val subTitle = item.subTitle.title()
            binding.title.text = title
            binding.subTitle.text = subTitle

            binding.image.loadImageUrl(item.image, item.imageLocal)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()


    companion object {
        var DIFF_JOB_CALLBACK = object : DiffUtil.ItemCallback<IntroData>() {
            override fun areItemsTheSame(oldItem: IntroData, newItem: IntroData) =
                newItem.hashCode() == oldItem.hashCode()

            override fun areContentsTheSame(oldItem: IntroData, newItem: IntroData) =
                newItem.title == oldItem.title && newItem.subTitle == oldItem.subTitle
        }
    }
}