package com.ftrujillo.moviedbsample.data.remote.interceptors

import com.ftrujillo.moviedbsample.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class MovieDbAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var req = chain.request()
        val url = req.url().newBuilder()
            .addQueryParameter("api_key", BuildConfig.MOVIE_DB_API_KEY).build()
        req = req.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}