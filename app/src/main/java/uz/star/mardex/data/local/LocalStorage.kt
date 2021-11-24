package uz.star.mardex.data.local

import android.content.Context
import android.content.SharedPreferences
import uz.star.mardex.utils.helpers.*

class LocalStorage private constructor(context: Context) {
    companion object {
        @Volatile
        lateinit var instance: LocalStorage
            private set

        fun init(context: Context) {
            synchronized(this) {
                instance = LocalStorage(context)
            }
        }
    }

    private val pref: SharedPreferences =
        context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var registrated by BooleanPreference(pref, false)

    var lang by StringPreference(pref, LANG_UZ)
    var langLocal by StringPreference(pref, LANG_UZ)

    var phone by StringPreference(pref, "")
    var name by StringPreference(pref, "")

    var id by StringPreference(pref, "")

    var currentLat by DoublePreference(pref, 0.0)
    var currentLong by DoublePreference(pref, 0.0)

    var avatarPath by StringPreference(pref, "")
    var notificationToken by StringPreference(pref, "")

    var nightMode by IntPreference(pref, LIGHT_MODE)
}