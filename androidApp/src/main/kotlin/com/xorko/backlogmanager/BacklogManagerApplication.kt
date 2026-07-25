package com.xorko.backlogmanager

import android.app.Application
import com.xorko.backlogmanager.di.initKoin
import org.koin.android.ext.koin.androidContext

class BacklogManagerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@BacklogManagerApplication)
        }
    }
}
