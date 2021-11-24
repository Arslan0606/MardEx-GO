package uz.star.mardex.utils.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import uz.star.mardex.R
import uz.star.mardex.data.local.LocalStorage
import uz.star.mardex.model.response.local.MessageData
import uz.star.mardex.ui.screen.entry.EntryActivity
import uz.star.mardex.utils.extension.hideLoader
import uz.star.mardex.utils.liveData.EventObserver

/**
 * Created by Farhod Tohirov on 18-Mar-21
 **/

fun Fragment.showMessage() = Observer<MessageData> {
    hideLoader()
    it.onMessage {
        val dialog = AlertDialog.Builder(requireContext()).setTitle(it).setPositiveButton("OK", null).create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
        }
        dialog.show()
    }.onResource {
        val dialog = AlertDialog.Builder(requireContext()).setTitle(it).setPositiveButton("OK", null).create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
        }
        dialog.show()
    }
}

fun Fragment.showMessageEvent() = EventObserver<MessageData> {
    hideLoader()
    it.onMessage {
        val dialog = AlertDialog.Builder(requireContext()).setTitle(it).setPositiveButton("OK", null).create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
        }
        dialog.show()
    }.onResource { message ->
        val dialog = AlertDialog.Builder(requireContext()).setTitle(message).setPositiveButton(
            "OK"
        ) { dialog, _ ->
            if (message == R.string.failure) {
                LocalStorage.instance.registrated = false
                requireActivity().finish()
                startActivity(Intent(requireActivity(), EntryActivity::class.java))
            } else {
                dialog?.dismiss()
            }
        }.create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
        }
        dialog.show()
    }
}

fun Fragment.showMessage(@StringRes text: Int) {
    val dialog = AlertDialog.Builder(requireContext()).setTitle(R.string.alert).setMessage(text).setPositiveButton("OK", null).create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
    }
    dialog.show()
}


fun Fragment.showAlertDialog(text: String, dismissListener: EmptyBlock? = null) {
    val dialog = AlertDialog.Builder(requireContext()).setTitle(R.string.alert).setMessage(text).setOnDismissListener { dismissListener?.invoke() }
        .setPositiveButton("OK", null).create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
    }
    dialog.show()
}

fun Fragment.showAlertDialog(text: Int, dismissListener: EmptyBlock? = null) {
    val dialog = AlertDialog.Builder(requireContext()).setTitle(R.string.alert).setMessage(text).setOnDismissListener { dismissListener?.invoke() }
        .setPositiveButton("OK", null).create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
    }
    dialog.show()
}


fun Fragment.showAlertDialog(title: Int, text: Int, dismissListener: EmptyBlock? = null) {
    val dialog = AlertDialog.Builder(requireContext()).setTitle(title).setMessage(text).setOnDismissListener { dismissListener?.invoke() }
        .setPositiveButton("OK", null).create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.new_green))
    }
    dialog.show()
}

fun Activity.showAlertDialog(title: Int, text: Int, dismissListener: EmptyBlock? = null) {
    val dialog = AlertDialog.Builder(this).setTitle(title).setMessage(text).setOnDismissListener { dismissListener?.invoke() }
        .setPositiveButton("OK", null).create()
    dialog.setOnShowListener {
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.new_green))
    }
    dialog.show()
}


