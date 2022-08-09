package com.ftrujillo.moviedbsample.core

import android.app.Application
import com.ftrujillo.moviedbsample.BuildConfig
import com.ftrujillo.moviedbsample.di.dataModule
import com.ftrujillo.moviedbsample.di.moviesModule
import com.ftrujillo.moviedbsample.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieSampleApplication : Application() {
    override fun onCreate() {

        startKoin {
            androidContext(this@MovieSampleApplication)
            listOf(
                modules(
                    moviesModule,
                    dataModule,
                    networkModule
                )
            )
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}