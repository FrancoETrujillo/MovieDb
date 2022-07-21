package com.ftrujillo.moviedbsample.data.remote.interceptors

import android.content.Context
import android.net.ConnectivityManager
import com.ftrujillo.moviedbsample.utils.NetworkInfoProvider
import com.ftrujillo.moviedbsample.utils.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val networkInfoProvider: NetworkInfoProvider):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!networkInfoProvider.isNetworkAvailable()) throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

}