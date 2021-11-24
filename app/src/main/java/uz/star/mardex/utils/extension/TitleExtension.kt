package uz.star.mardex.utils.extension

import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.server.title.Title
import uz.star.mardex.utils.helpers.LANG_KRILL
import uz.star.mardex.utils.helpers.LANG_UZ

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

fun Title.title(): String? {
    val localStorage = LocalStorage.instance
    return if (localStorage.lang == LANG_UZ) this.uz else if (localStorage.lang == LANG_KRILL) this.uz_kr else this.ru
}