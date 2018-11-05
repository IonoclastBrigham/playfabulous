// AbstractRestClient.kt
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2017-2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.kotlin.net

import com.google.gson.GsonBuilder
import com.ionoclast.kotlin.delegate.clear
import com.ionoclast.kotlin.delegate.clearableLazyInstance
import com.ionoclast.kotlin.serialization.DateDeserializer
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.internal.platform.Platform.INFO
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Default config values mixin.
 */
interface ClientConfig {
    val baseUri: String
    val timeoutSecs get() = 0L
    val logLevel get() = HttpLoggingInterceptor.Level.HEADERS
    val fallbackDateFormats: Array<String>? get() = null
}

/**
 * *Abstract base class for REST API clients in Kotlin 1.3+/Coroutines 1.0+.*
 *
 * To implement an API client using this class, create your endpoint interfaces as usual,
 * and allocate an implementation with the `restClient` property. You may wish to make them
 * lazy properties of your derived class.
 *
 * To use these APIs, create a coroutine block that will continue on some thread.
 * If you need to make sequential calls, such as pulling a list of IDs and
 * querying one or more with a second call, or pre-fetching a token to use with
 * another call, you can do that within the coroutine block; they will be
 * executed in order, with results returned on another thread as if it were all
 * synchronously executed. If you want multiple concurrent calls, they can be
 * spawned from within multiple coroutine blocks.
 *
 * You should pre-allocate a Job instance and pass that to your API calls, so
 * they can all be cancelled appropriately on user request, after a timeout, etc.
 *
 * @see Retrofit
 *
 * @author btoskin &lt;brigham@ionoclast.com&gt;
 */
abstract class AbstractRestClient : ClientConfig, CoroutineScope {
    companion object {
        private val TAG = AbstractRestClient::class.java.simpleName
    }


    data class RestResponse<out T>(val result: T? = null, val err: Throwable? = null)
    class ApiError(msg: String, val errBody: String? = null, cause: Throwable? = null) : IOException(msg, cause) {
        override fun toString() = message!!
    }


    private val job by clearableLazyInstance { Job() }
    override val coroutineContext get() = Dispatchers.IO + job

    protected val restClient by clearableLazyInstance(::buildClientAdapter)

    /**
     * *Builds a reasonable default API adapter.*
     *
     * Initializes the following networking components and features:
     * * Uses OkHttp for the underlying HTTP client implementation
     * * Registers a basic logging interceptor
     * * Sets connection and read timeouts as specified by the class implementation
     * * Registers a GSON decoder to inflate your models, with a `Date` helper class
     * * Binds the adapter to the base URI as specified by the class implementation
     *
     * You may override this method, if you need to add more hooks, or more fine-grained
     * control over how the components are configured, but it MUST NOT depend on internal
     * class state, either directly or indirectly.
     *
     * @see timeoutSecs
     * @see baseUri
     * @see DateDeserializer
     */
    protected open fun buildClientAdapter(): Retrofit {
        val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .connectTimeout(timeoutSecs, TimeUnit.SECONDS)
                .readTimeout(timeoutSecs , TimeUnit.SECONDS)
                .build()

        val gson = GsonBuilder().apply {
            fallbackDateFormats?.let { registerTypeAdapter(Date::class.java, DateDeserializer(fallbackDateFormats)) }
        }.create()

        return Retrofit.Builder()
                .baseUrl(baseUri)
                .client(client)
                .addCallAdapterFactory(DeferredRestResponseAdapterFactory()) // MUST COME BEFORE COROUTINE FACTORY
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    open fun cleanup() {
        job.cancel()
        ::job.clear()
        ::restClient.clear()
    }

    /**
     * Ensures that messages are logged atomically in order received.
     */
    protected open fun loggingInterceptor(): HttpLoggingInterceptor {
        val chan = Channel<String>(Channel.UNLIMITED)
        launch {
            for (msg in chan) {
                Platform.get().log(INFO, msg, null)
            }
        }

        return HttpLoggingInterceptor { msg ->
            chan.sendBlocking(msg)
        }.apply {
            level = logLevel
        }
    }
}
