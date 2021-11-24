package uz.star.mardex.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.star.mardex.data.local.LocalStorage
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 11-Jan-21
 **/

@Module
@InstallIn(SingletonComponent::class)
class LocalStorageModule {
    @Provides
    @Singleton
    fun getLocalStorage(): LocalStorage = LocalStorage.instance
}