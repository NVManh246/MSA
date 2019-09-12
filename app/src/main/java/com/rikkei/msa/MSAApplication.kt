package com.rikkei.msa

import android.app.Application
import com.rikkei.msa.di.presenterModule
import com.rikkei.msa.di.storageModule
import org.koin.android.ext.android.startKoin

class MSAApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(
            presenterModule,
            storageModule
        ))
    }
}
