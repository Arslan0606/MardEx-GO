package uz.star.mardex.utils.extension

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.star.mardex.R

/**
 * Created by Farhod Tohirov on 21-Mar-21
 **/

fun Fragment.changeStatusColorWhite() {
    requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.light_white)
}


fun Fragment.changeStatusColorMainColor() {
    requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.main_color)
}