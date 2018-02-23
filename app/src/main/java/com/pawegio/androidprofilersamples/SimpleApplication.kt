package com.pawegio.androidprofilersamples

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class SimpleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
