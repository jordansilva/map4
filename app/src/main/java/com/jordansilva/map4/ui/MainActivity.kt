package com.jordansilva.map4.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jordansilva.map4.R
import com.jordansilva.map4.ui.map.MapsFragment
import com.jordansilva.map4.util.extensions.makeStatusBarTransparent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeStatusBarTransparent()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapsFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else
            super.onBackPressed()
    }
}