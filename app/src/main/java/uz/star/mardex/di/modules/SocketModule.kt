package uz.star.mardex.di.modules

import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.star.mardex.utils.helpers.BASE_URL
import java.net.URISyntaxException
import javax.inject.Singleton

/**
 * Created by Farhod Tohirov on 25-Jan-21
 **/

@Module
@InstallIn(SingletonComponent::class)
class SocketModule {
    @Provides
    @Singleton
    fun getSocket(): Socket = try {
        IO.socket(BASE_URL)
    } catch (e: URISyntaxException) {
        throw Exception("SOCKET IS NOT CONNECTED BRO :)")
    }
}