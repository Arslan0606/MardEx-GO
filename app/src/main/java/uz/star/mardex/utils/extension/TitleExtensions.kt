package uz.star.mardex.utils.extension

import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.regions.Title

/**
 * Created by Botirali Kozimov on 27/06/2021
 */

fun Title.toLocalString(): String? {
    return when (LocalStorage.instance.langLocal) {
        "uz" -> {
            this.uz
        }
        "ru" -> {
            this.ru
        }
        else -> {
            this.uzKr
        }
    }
}