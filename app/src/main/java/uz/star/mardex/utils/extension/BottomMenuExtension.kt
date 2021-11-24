package uz.star.mardex.utils.extension

import androidx.fragment.app.Fragment
import uz.star.mardex.ui.screen.working.MainActivity

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

fun Fragment.showBottomMenu() {
    try {
        (activity as MainActivity).showBottomMenu()
    } catch (e: Exception) {

    }
}

fun Fragment.hideBottomMenu() {
    try {
        (activity as MainActivity).hideBottomMenu()
    } catch (e: Exception) {

    }
}