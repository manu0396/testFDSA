package com.example.test.utils

sealed class WrapperResponse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : WrapperResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : WrapperResponse<T>(data, message)
    //class Loading<T>(data: T?= null): WrapperResponse<T>(data)
}
