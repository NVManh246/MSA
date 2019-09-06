package com.rikkei.msa.di

import com.rikkei.msa.ui.main.MainContract
import com.rikkei.msa.ui.main.MainPresenter
import org.koin.dsl.module.module

val presenterModule = module {
    factory<MainContract.Presenter> { (view : MainContract.View) ->
        MainPresenter(
            view
        )
    }
}
