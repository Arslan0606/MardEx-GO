package uz.star.mardex.utils.extension

import uz.star.mardex.model.response.server.worker.SumMark

/**
 * Created by Farhod Tohirov on 27-Mar-21
 **/

fun SumMark?.rating(): Float {
    return if (this != null) {
        if (sumAll == 0f)
            5f
        else
            sumAll / sumClients
    } else
        0f
}