package com.example.minisofascoreapp.utils

sealed interface Result<out T> {

    class Success<T>(val data: T) : Result<T>

    class Error(val e: Throwable) : Result<Nothing>
}
