package uz.star.mardex.utils.extension

/**
 * Created by Farhod Tohirov on 13-Feb-21
 **/

val String.removePlusAndSpace: String
    get() =
        this.replace(" ", "")
            .replace("+", "")
            .replace("(", "")
            .replace(")", "")