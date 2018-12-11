// PlayFabulous.kt
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.playfabulous

import com.ionoclast.kotlin.delegate.clear
import com.ionoclast.kotlin.delegate.clearableLazy
import com.ionoclast.kotlin.net.AbstractRestClient
import com.ionoclast.playfabulous.api.AuthApi
import com.ionoclast.playfabulous.api.PlayerAccountApi
import com.ionoclast.playfabulous.api.SegmentApi
import okhttp3.logging.HttpLoggingInterceptor


/**
 * A Kotlin-first PlayFab API Client.
 */
class PlayFabulous(override val maxIdleConnections: Int = 5) : AbstractRestClient() {
	/**
	 * ⚠️ INVALID: always override with a `@Url` param.
	 *
	 * PlayFabulous is meant to be able to talk to multiple title IDs, so no
	 * universally valid baseUri is possible.
	 */
	override val baseUri = "http://invalid/do/not/use/"
	override val fallbackDateFormats = arrayOf(
			"yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	)
	override val logLevel = HttpLoggingInterceptor.Level.BODY

	val authApi by clearableLazy { restClient.create(AuthApi::class.java)!! }

	val playerAccountApi by clearableLazy { restClient.create(PlayerAccountApi::class.java)!! }

	val segmentApi by clearableLazy { restClient.create(SegmentApi::class.java) }

	override fun cleanup() {
		super.cleanup()

		::authApi.clear()
		::playerAccountApi.clear()
		::segmentApi.clear()
	}
}

fun playFabApiBaseUrl(titleId: String) = "https://$titleId.playfabapi.com/"

fun endpointUrl(titleId: String, path: String) = "${playFabApiBaseUrl(titleId)}$path"
