package com.ftrujillo.moviedbsample.core.utils


sealed class RequestDataWrapper<T> {
    data class Success<T>(val result: T) : RequestDataWrapper<T>()
    data class Error<T>(val errorMessage: String, val result: T? = null) : RequestDataWrapper<T>()
    data class Loading<T>(val result: T? = null) : RequestDataWrapper<T>()
}

sealed class RemoteRequestWrapper<T> {
    data class Success<T>(val result: T) : RemoteRequestWrapper<T>()
    data class Error<T>(val errorMessage: String, val result: T? = null) : RemoteRequestWrapper<T>()
}

fun <T> RemoteRequestWrapper<T>.toLoadingResultWrapper(): RequestDataWrapper<T> {
    return when (this) {
        is RemoteRequestWrapper.Success<T> -> RequestDataWrapper.Success(this.result)
        is RemoteRequestWrapper.Error<T> -> RequestDataWrapper.Error(this.errorMessage)
    }
}