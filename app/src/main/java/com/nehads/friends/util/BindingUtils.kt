package com.nehads.friends.util

import android.text.format.DateUtils
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("elapsedTime")
fun TextView.setElapsedTime(value: Long) {
    val seconds = value / 1000
    text = if (seconds < 60) seconds.toString() else DateUtils.formatElapsedTime(seconds)
}

@BindingAdapter("email")
fun TextInputEditText.setEmail(value: String?) {
    setText(value ?: "")
}

@BindingAdapter("name")
fun TextInputEditText.setName(value: String?) {
    setText(value ?: "")
}
