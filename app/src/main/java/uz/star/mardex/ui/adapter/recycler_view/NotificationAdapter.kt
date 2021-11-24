package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.star.mardex.databinding.NotificationItem1Binding
import uz.star.mardex.model.response.NotificationResponse
import uz.star.mardex.utils.helpers.SingleBlock

/**
 * Created by Farhod Tohirov on 12-Jan-21
 **/

class NotificationAdapter : ListAdapter<NotificationResponse, NotificationAdapter.ViewHolder>(DIFF_JOB_CALLBACK) {

    private var clickListener: SingleBlock<NotificationResponse>? = null

    inner class ViewHolder(private val binding: NotificationItem1Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val notificationData = getItem(adapterPosition)
            binding.apply {
                txtTitle.text = notificationData.title
                txtDate.text = notificationData.date
                txtDescription.text = notificationData.description
            }
        }
    }

    fun setOnClickListener(f: SingleBlock<NotificationResponse>) {
        clickListener = f
    }

    companion object {
        var DIFF_JOB_CALLBACK = object : DiffUtil.ItemCallback<NotificationResponse>() {
            override fun areItemsTheSame(oldItem: NotificationResponse, newItem: NotificationResponse) =
                newItem.id == oldItem.id

            override fun areContentsTheSame(oldItem: NotificationResponse, newItem: NotificationResponse): Boolean {
                return newItem.title == oldItem.title && newItem.description == oldItem.description && newItem.date == oldItem.date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        NotificationItem1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()
}