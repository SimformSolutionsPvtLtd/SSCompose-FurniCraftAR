package com.simform.ssfurnicraftar.utils.result

import com.simform.ssfurnicraftar.utils.result.Result.Error
import com.simform.ssfurnicraftar.utils.result.Result.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
inline val Result<*>.succeeded
    get() = this is Success && data != null

/**
 * `true` if [Result] is of type [Error].
 */
val Result<*>.failed
    get() = this is Error

val <T> Result<T>.data: T?
    get() = (this as? Success)?.data

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Success<T>)?.data ?: fallback
}
