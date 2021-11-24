package uz.star.mardex.utils.extension

import androidx.fragment.app.Fragment
import uz.star.mardex.ui.screen.working.MainActivity

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

fun Fragment.showLoader() {
    (activity as MainActivity).showLoader()
}

fun Fragment.hideLoader() {
    (activity as MainActivity).hideLoader()
}