package com.ftrujillo.moviedbsample

import android.app.Application
import com.ftrujillo.moviedbsample.di.moviesModule
import com.ftrujillo.moviedbsample.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import timber.log.Timber

class MovieSampleApplication : Application() {
    override fun onCreate() {

        startKoin {
            androidContext(this@MovieSampleApplication)
            listOf(
                modules(
                    moviesModule,
                    networkModule
                )
            )

        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}