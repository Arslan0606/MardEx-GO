package uz.star.mardex.utils.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.yandex.mapkit.mapview.MapView;

public class ScrollViewWithMap extends ScrollView {
    public MapView mapView;

    public ScrollViewWithMap(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mapView == null)
            return super.onInterceptTouchEvent(ev);

        if (inRegion(ev.getRawX(), ev.getRawY(), mapView))
            return false;

        return super.onInterceptTouchEvent(ev);
    }

    private boolean inRegion(float x, float y, View v) {
        int[] mCoordBuffer = new int[]
                {0, 0};

        v.getLocationOnScreen(mCoordBuffer);

        return mCoordBuffer[0] + v.getWidth() > x && // right edge
                mCoordBuffer[1] + v.getHeight() > y && // bottom edge
                mCoordBuffer[0] < x && // left edge
                mCoordBuffer[1] < y; // top edge
    }
}