package uz.star.mardex.utils.extension

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Created by Farhod Tohirov on 27-Mar-21
 **/

fun BottomSheetBehavior<View>.hide(){
    this.state = BottomSheetBehavior.STATE_HIDDEN
}

fun BottomSheetBehavior<View>.show(){
    this.state = BottomSheetBehavior.STATE_EXPANDED
}