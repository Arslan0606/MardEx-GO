package uz.star.mardex.utils.extension

import uz.star.mardex.utils.helpers.EmptyBlock

/**
 * Created by Farhod Tohirov on 23-Mar-21
 **/

fun Any?.isNotNull(block: EmptyBlock, itNull: EmptyBlock? = null){
    if (this != null) block() else itNull?.invoke()
}