package com.fhanafi.cerdikia.helper

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.textfield.TextInputLayout

class KelasEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var onValidInputChanged: ((Boolean) -> Unit)? = null

    init {
        inputType = InputType.TYPE_CLASS_NUMBER
        filters = arrayOf(InputFilter.LengthFilter(1)) // max 1 digit

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = s?.matches(Regex("[1-6]")) == true
                if (!isValid) {
                    error = "Masukkan angka 1 hingga 6"
                } else {
                    error = null
                }
                onValidInputChanged?.invoke(isValid)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
