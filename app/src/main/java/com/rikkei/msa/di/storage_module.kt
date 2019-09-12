package com.rikkei.msa.di

import android.os.storage.StorageManager
import com.rikkei.msa.storage.SharedPrefsManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val storageModule = module {
    factory { SharedPrefsManager(androidContext()) }
}
