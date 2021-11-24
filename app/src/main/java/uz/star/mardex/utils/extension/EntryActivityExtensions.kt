package uz.star.mardex.utils.extension

import androidx.fragment.app.Fragment
import uz.star.mardex.ui.screen.entry.EntryActivity

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/

fun Fragment.showEntryLoader() {
    try {
        (activity as EntryActivity).showLoader()
    } catch (e: Exception) {

    }
}

fun Fragment.hideEntryLoader() {
    try {
        (activity as EntryActivity).hideLoader()
    } catch (e: Exception) {

    }
}