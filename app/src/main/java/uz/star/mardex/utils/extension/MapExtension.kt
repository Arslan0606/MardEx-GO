package uz.star.mardex.utils.extension

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import de.hdodenhof.circleimageview.CircleImageView
import uz.star.mardex.R
import uz.star.mardex.utils.helpers.BASE_URL

/**
 * Created by Farhod Tohirov on 21-Mar-21
 **/

fun MapView.navigateToPoint(point: Point, zoom: Float = 17f) {
    this.map.move(CameraPosition(point, zoom, 0f, 0f))
}

fun Fragment.addRedPlaceMark(map: MapView, point: Point, imageUrl: String = "") {
    val avatarIcon = CircleImageView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val picasso = Picasso.get().load(BASE_URL + imageUrl)
    picasso.error(R.drawable.ic_gps_worker)
    picasso.resize(125, 125)
    picasso.placeholder(R.drawable.ic_gps_worker)
    picasso.into(avatarIcon)

    val viewProvider = ViewProvider(avatarIcon)
    map.map.mapObjects.addPlacemark(point, viewProvider)
}


fun Fragment.addWorkerPlaceMark(map: MapView, point: Point, imageUrl: String = "") {
    val avatarIcon = CircleImageView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    val picasso = Picasso.get().load(BASE_URL + imageUrl)
    picasso.error(R.drawable.ic_baseline_account_circle_48)
    picasso.resize(125, 125)
    picasso.placeholder(R.drawable.ic_baseline_account_circle_48)
    picasso.into(avatarIcon)

    val viewProvider = ViewProvider(avatarIcon)
    map.map.mapObjects.addPlacemark(point, viewProvider)
}

fun MapView.zoomIn() {
    this.map.move(CameraPosition(this.map.cameraPosition.target, this.map.cameraPosition.zoom + 2f, 0f, 0f))
}

fun MapView.zoomOut() {
    try {
        this.map.move(CameraPosition(this.map.cameraPosition.target, this.map.cameraPosition.zoom - 2f, 0f, 0f))
    } catch (e: Exception) {

    }
}