package com.andresuryana.dicodingstories.ui.component.input

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.andresuryana.dicodingstories.R
import com.google.android.material.textfield.TextInputEditText

private const val PASSWORD_MIN_LENGTH = 8

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr), TextWatcher {

    init {
        addTextChangedListener(this)
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Do nothing
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        // Do nothing
    }

    override fun afterTextChanged(s: Editable?) {
        // Password validation
        error = if ((s?.length ?: 0) < PASSWORD_MIN_LENGTH) {
            context.getString(R.string.error_min_password_length, PASSWORD_MIN_LENGTH)
        } else {
            null
        }
    }
}