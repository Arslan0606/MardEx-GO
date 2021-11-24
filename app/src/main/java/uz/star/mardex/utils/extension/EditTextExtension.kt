package uz.star.mardex.utils.extension

import android.os.CountDownTimer
import android.widget.TextView
import androidx.annotation.StringRes

/**
 * Created by Farhod Tohirov on 14-Apr-21
 **/


fun TextView.setTextChangeableAfterTime(startText: String, timeInSec: Int, @StringRes endText: Int) {
    try {
        this.text = startText
        this.isEnabled = false
        object : CountDownTimer(timeInSec * 1_000L, 1_000) {
            override fun onTick(time: Long) {
                text = when {
                    time / 1000 >= 60 -> {
                        if (time / 1000 >= 70)
                            startText + "01:${(time) / 1000 - 60}"
                        else
                            startText + "01:0${(time) / 1000 - 60}"
                    }
                    time / 1000 >= 10 -> {
                        startText + "00:${(time) / 1000}"
                    }
                    else -> startText + "00:0${(time) / 1000}"
                }
            }

            override fun onFinish() {
                this@setTextChangeableAfterTime.isEnabled = true
                setText(endText)
            }
        }.start()
    } catch (e: Exception) {

    }
}
