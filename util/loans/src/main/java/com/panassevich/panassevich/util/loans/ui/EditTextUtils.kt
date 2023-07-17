package com.panassevich.panassevich.util.loans.ui

import android.text.InputType
import android.widget.EditText
import android.widget.ImageButton

const val INPUT_TYPE_PASSWORD_SHOWN =
    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

const val INPUT_TYPE_PASSWORD_HIDDEN =
    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

/**
 * Used to link EditText with ImageButton, so click on button change password visibility.
 */
fun enableTogglePasswordVisibility(
    inputField: EditText, button: ImageButton,
    disabledImageId: Int, enabledImageId: Int
) {
    button.setOnClickListener {
        when (inputField.inputType) {
            INPUT_TYPE_PASSWORD_HIDDEN -> {
                button.setImageResource(enabledImageId)
                inputField.inputType = INPUT_TYPE_PASSWORD_SHOWN
            }

            INPUT_TYPE_PASSWORD_SHOWN -> {
                button.setImageResource(disabledImageId)
                inputField.inputType = INPUT_TYPE_PASSWORD_HIDDEN
            }

            else -> throw RuntimeException("Unexpected input type.")
        }
        inputField.setSelection(inputField.length())  //move cursor to the end of EditText
    }
}