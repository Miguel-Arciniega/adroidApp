package com.example.practica_firebase.util

import android.content.Context
import com.example.practica_firebase.util.ConstantsUtils.Companion.BTN_ACCEPT
import com.example.practica_firebase.util.ConstantsUtils.Companion.TITLE

class MessagesUtils {

    companion object {
        fun showMessage(context: Context, msgText: String) {
            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle(TITLE)
                .setMessage(msgText)
                .setNeutralButton(BTN_ACCEPT) { _, _ -> }
                .show()
        }
    }
}