package com.jordansilva.map4.util.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

fun Activity.checkPermission(vararg permissions: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        permissions.all { permission -> ActivityCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED }
    } else {
        true
    }
}

fun Fragment.checkPermission(vararg permissions: String): Boolean = requireActivity().checkPermission(*permissions)

fun Activity.askPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) =
    ActivityCompat.requestPermissions(this, permissions, requestCode)

fun Fragment.askPermission(vararg permissions: String, @IntRange(from = 0) requestCode: Int) = requestPermissions(permissions, requestCode)
