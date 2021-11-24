package uz.star.mardex.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.utils.helpers.NIGHT_MODE

/**
 * Created by Farhod Tohirov on 08-Jan-21
 **/

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        MapKitFactory.setApiKey("8533b7ec-ffbc-4132-b951-99530aeb1882")
        LocalStorage.init(this)
        if (LocalStorage.instance.nightMode == NIGHT_MODE) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        lateinit var instance: App
    }

}