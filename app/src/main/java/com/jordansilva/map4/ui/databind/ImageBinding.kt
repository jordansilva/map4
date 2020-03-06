package com.jordansilva.map4.ui.databind

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["imageUrl", "placeholder"], requireAll = false)
fun setImageUrl(imageView: ImageView, url: String?, placeholder: Drawable?) {
    url?.let {
        Picasso.get().load(url)
            .apply {
                if (placeholder != null) placeholder(placeholder)
                into(imageView)
            }

    } ?: imageView.setImageDrawable(placeholder)
}