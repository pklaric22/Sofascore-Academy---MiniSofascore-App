package com.example.minisofascoreapp.utils

import com.example.minisofascoreapp.utils.Result
import retrofit2.HttpException
import retrofit2.Response

/**
 * Wrap each call to API with this function.
 * Transform data to result so you can handle error cases in ViewModel.
 */
suspend fun <T> safeResponse(func: suspend () -> T): Result<T> {
    return try {
        val result: T = func.invoke()
        if (result is Response<*> && !result.isSuccessful) {
            Result.Error(HttpException(result))
        } else {
            Result.Success(result)
        }
    } catch (e: Throwable) {
        Result.Error(e)
    }
}
