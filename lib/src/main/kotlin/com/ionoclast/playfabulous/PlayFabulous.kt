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
import com.ionoclast.kotlin.delegate.clearableLazyInstance
import com.ionoclast.kotlin.net.AbstractRestClient
import com.ionoclast.playfabulous.api.AuthApi
import com.ionoclast.playfabulous.api.PlayerAccountApi


/**
 * A Kotlin-first PlayFab API Client.
 */
class PlayFabulous : AbstractRestClient() {
	override val baseUri = "https://abcd.playfabapi.com/" // FIXME
	override val fallbackDateFormats = arrayOf(
			"yyyy-MM-dd'T'HH:mm:ss'Z'",
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
	)

	val authApi by clearableLazyInstance { restClient.create(AuthApi::class.java)!! }

	val playerAccountApi by clearableLazyInstance { restClient.create(PlayerAccountApi::class.java)!! }

	override fun cleanup() {
		super.cleanup()

		::authApi.clear()
		::playerAccountApi.clear()
	}
}

fun playFabApiBaseUrl(titleId: String) = "https://$titleId.playfabapi.com/"

fun endpointUrl(titleId: String, path: String) = "${playFabApiBaseUrl(titleId)}$path"
