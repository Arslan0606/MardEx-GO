package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.PhotosItemRecyclerBinding
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.loadImageUrl
import uz.star.mardex.utils.extension.show
import uz.star.mardex.utils.helpers.EmptyBlock
import uz.star.mardex.utils.helpers.SingleBlock

/**
 * Created by Botirali Kozimov on 10-03-21
 **/

class ImagesRVAdapter : ListAdapter<String, ImagesRVAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {
    private var listenerAdd: EmptyBlock? = null
    private var listenerImageClick: SingleBlock<String>? = null

    companion object {
        var DIFF_JOB_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String) =
                newItem.hashCode() == oldItem.hashCode()

            override fun areContentsTheSame(oldItem: String, newItem: String) =
                newItem == oldItem
        }
    }

    fun setOnAddListener(block: EmptyBlock?) {
        listenerAdd = block
    }

    fun setOnListenerImageClick(block: SingleBlock<String>?) {
        listenerImageClick = block
    }

    override fun getItemCount(): Int = currentList.size

    inner class ViewHolder(private val binding: PhotosItemRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnAdd.setOnClickListener {
                if (adapterPosition == 0) {
                    listenerAdd?.invoke()
                } else listenerImageClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind() {
            if (adapterPosition != 0) {
                binding.layoutAdd.hide()
                binding.imagePhoto.show()
                val path = getItem(adapterPosition)

                binding.imagePhoto.loadImageUrl(path)
            } else {
                binding.imagePhoto.hide()
                binding.layoutAdd.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        PhotosItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}