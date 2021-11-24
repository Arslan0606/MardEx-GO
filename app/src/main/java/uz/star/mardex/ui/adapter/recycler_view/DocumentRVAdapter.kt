package uz.star.mardex.ui.adapter.recycler_view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.LayoutDocumentItemBinding
import uz.star.mardex.utils.extension.hide
import uz.star.mardex.utils.extension.loadImageFile
import uz.star.mardex.utils.extension.show
import uz.star.mardex.utils.helpers.EmptyBlock
import uz.star.mardex.utils.helpers.SingleBlock
import java.io.File

/**
 * Created by Farhod Tohirov on 19-Feb-21
 **/

class DocumentRVAdapter : ListAdapter<File, DocumentRVAdapter.ViewHolder>(ITEM_DOCUMENT_DIFF_CALLBACK) {

    private var listenerDocumentDeleteClick: SingleBlock<File>? = null
    private var listenerAddDocumentClick: EmptyBlock? = null

    inner class ViewHolder(private val binding: LayoutDocumentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            /**
             * Below if used for create addImage Button in recycler View
             * */
            if (currentList.size - 1 != adapterPosition) {
                binding.addPhoto.hide()
                binding.image.show()
                binding.deleteImage.show()
                binding.image.loadImageFile(getItem(adapterPosition))
                binding.root.setOnClickListener(null)
                binding.deleteImage.setOnClickListener {
                    listenerDocumentDeleteClick?.invoke(getItem(adapterPosition))
                }
            } else {
                binding.root.setCardBackgroundColor(Color.parseColor("#F6F6F8"))
                binding.root.setOnClickListener {
                    listenerAddDocumentClick?.invoke()
                }
                binding.addPhoto.show()
                binding.deleteImage.hide()
                binding.image.hide()
            }
        }
    }

    fun setOnDeleteDocumentClickListener(f: SingleBlock<File>) {
        listenerDocumentDeleteClick = f
    }

    fun setOnAddDocumentClickListener(f: EmptyBlock) {
        listenerAddDocumentClick = f
    }

    companion object {
        val ITEM_DOCUMENT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<File>() {
            override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem.hashCode() == newItem.hashCode()

            override fun areContentsTheSame(oldItem: File, newItem: File) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutDocumentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}