package com.ftrujillo.moviedbsample.data.data_source.interceptors

import com.ftrujillo.moviedbsample.core.utils.NetworkInfoProvider
import com.ftrujillo.moviedbsample.core.utils.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(private val networkInfoProvider: NetworkInfoProvider):Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if(!networkInfoProvider.isNetworkAvailable()) throw NoConnectivityException()
        return chain.proceed(chain.request())
    }
}