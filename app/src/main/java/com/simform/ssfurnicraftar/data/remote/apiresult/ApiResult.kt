package com.simform.ssfurnicraftar.data.remote.apiresult

import com.simform.ssfurnicraftar.data.remote.NetworkDataSource

/**
 * Interface to wrap API results from a Retrofit API call
 * Use this class as the return type for every function placed in [NetworkDataSource]
 *
 * Usage:
 * ```
 *     /* Function getUserData returns an instance of ApiResult */
 *     apiRepository.getUserData(id = 101)
 *         .onSuccess {
 *             // Called when api call succeeds
 *         }
 *         .onError {
 *             // Called when error occurs while making an API call
 *         }
 *         .onException {
 *             // Called when exception is generated while making an API call
 *         }
 * ```
 */
sealed interface ApiResult<T : Any>

/**
 * Denotes API success
 */
data class ApiSuccess<T : Any>(val data: T) : ApiResult<T>

/**
 * Denotes error while making an API call
 */
data class ApiError<T : Any>(val code: Int, val message: String?) : ApiResult<T>

/**
 * Denotes exception generated while making an API call
 */
data class ApiException<T : Any>(val exception: Throwable) : ApiResult<T>
