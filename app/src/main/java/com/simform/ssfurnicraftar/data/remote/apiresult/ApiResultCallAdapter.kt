package com.simform.ssfurnicraftar.data.remote.apiresult

import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResult
import com.simform.ssfurnicraftar.data.remote.apiresult.ApiResultCall
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Custom [CallAdapter] to adapt call with [responseType] into [ApiResult]
 */
class ApiResultCallAdapter(
    private val responseType: Type
) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> = ApiResultCall(call)
}
