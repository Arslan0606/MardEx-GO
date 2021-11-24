package uz.star.mardex.utils.extension

import android.view.View

/**
 * Created by Farhod Tohirov on 13-Feb-21
 **/

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}