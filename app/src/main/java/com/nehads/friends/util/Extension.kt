package com.nehads.friends.util

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.nehads.friends.R
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/* Extension to load image, in case of error/broken url error image will be shown */
fun ImageView.load(url: Any, onLoadingFinished: () -> Unit = {}) {
    val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadingFinished()
            return false
        }
    }
    GlideApp.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(R.drawable.error_placeholder))
        .listener(listener)
        .dontAnimate()
        .into(this)
}

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

/* Check if email is valid */
fun CharSequence.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun ActionBar?.addActionBar(barTitle: String) {
    this?.apply {
        setHomeButtonEnabled(true)
        title = barTitle
    }
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun String.getPath(): String {
    return String.format("%s%s", Constants.IMAGE_BASE_URL, this)
}