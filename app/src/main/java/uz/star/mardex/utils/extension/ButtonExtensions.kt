package uz.star.mardex.utils.extension

import android.widget.Button
import uz.star.mardex.utils.helpers.EmptyBlock

/**
 * Created by Farhod Tohirov on 17-Feb-21
 **/

fun Button.whenItIsAvailable(f: EmptyBlock) {
    if (this.alpha == 1f) f()
}