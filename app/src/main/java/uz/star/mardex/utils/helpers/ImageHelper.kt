package uz.star.mardex.utils.helpers

import android.content.Context
import id.zelory.compressor.Compressor
import uz.star.mardex.utils.network.Coroutines
import java.io.File

/**
 * Created by Botirali Kozimov on 11-03-21
 **/

object ImageHelper {
    fun compressImage(context: Context, file: File, granted: (File?) -> Unit) {
        Coroutines.dispatcherThenMain(
            {
                Compressor.compress(context, file)
            }, {
                granted(it)
            }
        )
    }
}
