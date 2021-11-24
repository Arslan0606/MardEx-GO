package uz.star.mardex.ui.adapter.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.smarteist.autoimageslider.SliderViewAdapter
import uz.star.mardex.databinding.ImageSliderItemBinding
import uz.star.mardex.utils.extension.loadImageUrl

/**
 * Created by Farhod Tohirov on 14-Jan-21
 **/

class ImageSliderAdapter(private val list: List<String>) : SliderViewAdapter<ImageSliderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ImageSliderItemBinding) : SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.ivAutoImageSlider.loadImageUrl(list[position])
        }
    }

    override fun getCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder = ViewHolder(
        ImageSliderItemBinding.inflate(LayoutInflater.from(parent?.context))
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = viewHolder.bind(position)
}