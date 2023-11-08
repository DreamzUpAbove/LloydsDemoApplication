package com.lloyds.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LloydsMainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}