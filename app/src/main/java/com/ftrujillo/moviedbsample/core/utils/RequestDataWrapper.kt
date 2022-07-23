package com.ftrujillo.moviedbsample.core.utils


sealed class RequestDataWrapper<T>(val result: T? = null, val errorMessage: String? = null) {
    class Success<T>(result: T) : RequestDataWrapper<T>(result)
    class Error<T>(errorMessage: String, result: T? = null) : RequestDataWrapper<T>(result, errorMessage)
    class Loading<T>(result: T? = null) : RequestDataWrapper<T>(result)
}

sealed class RemoteRequestWrapper<T>(val result: T? = null, val errorMessage: String? = null) {
    class Success<T>(result: T) : RemoteRequestWrapper<T>(result)
    class Error<T>(errorMessage: String, result: T? = null) : RemoteRequestWrapper<T>(result, errorMessage)
}