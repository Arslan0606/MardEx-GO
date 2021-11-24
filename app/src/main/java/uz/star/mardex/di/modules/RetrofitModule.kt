package uz.star.mardex.di.modules

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.star.mardex.utils.helpers.BASE_URL
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 09-Jan-21
 **/

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun okHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ChuckInterceptor(context))//for seeing responses and requests from emulator
        .build()

    @Provides
    @Singleton
    fun getRetrofit(
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL + "api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
