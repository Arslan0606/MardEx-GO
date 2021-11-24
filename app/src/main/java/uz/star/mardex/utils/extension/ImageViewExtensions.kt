package uz.star.mardex.utils.extension

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import uz.star.mardex.R
import uz.star.mardex.app.App
import uz.star.mardex.utils.helpers.BASE_URL
import java.io.File

/**
 * Created by Farhod Tohirov on 21-Feb-21
 **/

fun ImageView.loadImageFile(file: File) {
    Glide.with(this).load(file).centerCrop().into(this)
}

fun ImageView.loadImageUri(uri: Uri) {
    Glide.with(this).load(uri).centerCrop().into(this)
}

fun ImageView.loadImageUrl(url: String, isProfile: Boolean = true, isWorker: Boolean = false) {
    if (url.endsWith(".svg", true)) {
        GlideToVectorYou.init()
            .with(App.instance)
            .setPlaceHolder(if (isProfile)
                if (isWorker) R.drawable.pro_logo else R.drawable.ic_logo
            else R.drawable.ic_logo,
                if (isWorker) R.drawable.pro_logo else R.drawable.ic_logo)
            .load(Uri.parse(BASE_URL + url), this)
    } else
        Glide.with(this).load(BASE_URL + url).placeholder(if (isProfile && isWorker) R.drawable.pro_logo else  R.drawable.ic_logo).into(this)
}

fun ImageView.loadImageUrl(url: String, placeHolder: Int) {
    Glide.with(this).load(url).centerCrop().placeholder(placeHolder).into(this)
}