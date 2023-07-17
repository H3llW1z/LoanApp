package com.panassevich.panassevich.util.loans.ui

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(text: String) {
    this.let {
        Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show()
    }
}