package com.simform.ssfurnicraftar.data.remote.apiresult

import com.simform.ssfurnicraftar.data.remote.apiresult.ApiError
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiSuccess
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Custom Retrofit call class to delegate response
 *
 * @param call  The retrofit [Call]
 */
class ApiResultCall<T : Any>(private val call: Call<T>) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResult = getApiResult(response)
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = ApiException<T>(t)
                callback.onResponse(this@ApiResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResult<T>> = ApiResultCall(call.clone())
    override fun request(): Request = call.request()
    override fun timeout(): Timeout = call.timeout()
    override fun isExecuted(): Boolean = call.isExecuted
    override fun isCanceled(): Boolean = call.isCanceled
    override fun cancel() { call.cancel() }

    /**
     * Get the [ApiResult] from the [response]
     *
     * @param response  The [Response] received from API call
     *
     * @return [ApiResult.ApiSuccess] if [response] is successful and received body is non-null,
     * Otherwise [ApiResult.ApiError]
     */
    private fun <T : Any> getApiResult(response: Response<T>): ApiResult<T> {
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            ApiSuccess(body)
        } else {
            ApiError(response.code(), response.message())
        }
    }
}
