// DeferredRestResponseAdapterFactory.kt
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.kotlin.net

import com.ionoclast.kotlin.net.AbstractRestClient.RestResponse
import com.ionoclast.kotlin.net.AbstractRestClient.ApiError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.SocketException


/**
 * A Retrofit2 Call adapter factory which supports async RestResponse return types.
 */
class DeferredRestResponseAdapterFactory : CallAdapter.Factory() {
	/**
	 * @param returnType return type of endpoint interface method we're adapting
	 */
	override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
		// We're wrapping up responses into kotlinx.couroutines.Deferred...
		if (getRawType(returnType) != Deferred::class.java || returnType !is ParameterizedType) {
			return null
		}

		// ...but only if they're asking for a RestResponse<T> wrapper...
		val resType = getParameterUpperBound(0, returnType)
		if (getRawType(resType) != RestResponse::class.java || resType !is ParameterizedType) {
			return null
		}

		// ...so grab the actual model type requested and create the adapter
		val modelType = getParameterUpperBound(0, resType)
		return RestResponseAdapter<Any>(modelType)
	}
}

private class RestResponseAdapter<T>(private val modelType: Type) : CallAdapter<T, Deferred<RestResponse<T>>> {
	override fun adapt(call: Call<T>): Deferred<RestResponse<T>> {
		val awaitable = CompletableDeferred<RestResponse<T>>().apply {
			invokeOnCompletion {
				if (isCancelled) call.cancel()
			}
		}

		call.enqueue(object : Callback<T> {
			override fun onResponse(call: Call<T>, response: Response<T>) {
				val restResponse = response.takeIf { it.isSuccessful }?.let { RestResponse(it.body()) }
						?: RestResponse(err = ApiError("Api Error (${response.code()})", response.errorBody()?.string(), response.code()))
				awaitable.complete(restResponse)
			}

			override fun onFailure(call: Call<T>, ex: Throwable) {
				val computedErr = if (ex is SocketException && (ex.message == "Socket closed") || ex is IOException && (ex.message == "Canceled")) {
					ApiError(ex.message!!, cause = CancellationException(ex.message))
				} else {
					val path = call.request().url().encodedPath()
					ApiError("API call failed: $path", cause = ex)
				}
				awaitable.complete(RestResponse(err = computedErr))
			}
		})

		return awaitable
	}

	override fun responseType() = modelType
}
