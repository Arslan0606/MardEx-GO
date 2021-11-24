package uz.star.mardex.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.star.mardex.data.remote.GetDataApi
import uz.star.mardex.data.remote.NewsApi
import uz.star.mardex.data.remote.ProfileDataApi
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 09-Jan-21
 **/

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun getDataApi(retrofit: Retrofit): GetDataApi = retrofit.create(GetDataApi::class.java)


    @Provides
    @Singleton
    fun getProfileDataApi(retrofit: Retrofit): ProfileDataApi = retrofit.create(ProfileDataApi::class.java)

    @Provides
    @Singleton
    fun getNewsApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)

}