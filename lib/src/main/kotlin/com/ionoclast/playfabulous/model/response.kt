// response.kt
// PlayFab REST response base models.
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.playfabulous.model

import com.ionoclast.kotlin.net.AbstractRestClient.RestResponse
import kotlinx.coroutines.Deferred


data class PlayFabResult<T>(
	val code: Int,
	val status: String,
	val data: T
) {
	val isSuccessful get() = code <= 400
}

typealias DeferredResponse<T> = Deferred<RestResponse<PlayFabResult<T>>>
