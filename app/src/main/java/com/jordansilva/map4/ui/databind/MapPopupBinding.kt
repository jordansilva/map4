package com.jordansilva.map4.ui.databind

import android.view.View
import androidx.databinding.BindingAdapter
import com.jordansilva.map4.ui.model.POIViewData
import com.jordansilva.map4.util.extensions.displayCircularReveal
import com.jordansilva.map4.util.extensions.visible

@BindingAdapter("mapPopup")
fun <T> bindMapPopup(view: View, data: T?) {
    when {
        data != null -> if (!view.visible) view.displayCircularReveal()
        else -> view.visible = false
    }
}