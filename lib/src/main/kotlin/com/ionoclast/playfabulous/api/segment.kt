// segment.kt
// PlayFab Player Segment API interface and related declarations.
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.playfabulous.api

import com.ionoclast.playfabulous.api.SegmentApi.Companion.allSegmentsEndpoint
import com.ionoclast.playfabulous.api.SegmentApi.Companion.playersEndpoint
import com.ionoclast.playfabulous.endpointUrl
import com.ionoclast.playfabulous.model.PlayersInSegmentRequest
import com.ionoclast.playfabulous.model.PlayersInSegmentResponse
import com.ionoclast.playfabulous.model.SecretKey
import com.ionoclast.playfabulous.model.SegmentListResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url


interface SegmentApi {
	companion object {
		fun allSegmentsEndpoint(titleId: String) = endpointUrl(titleId, "Server/GetAllSegments")
		fun playersEndpoint(titleId: String) = endpointUrl(titleId, "Server/GetPlayersInSegment")
	}

	/**
	 * Gets a list of all segments (async) for the given title ID.
	 *
	 * You should probably use `getAllSegmentsAsync` or `getAllSegments` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param key PlayFab title's admin/server secret key.
	 * @param dummy required for a well-formed POST request, but not used.
	 *
	 * @see getAllSegmentsAsync
	 * @see getAllSegments
	 */
	@POST
	fun getAllSegmentsUrlAsync(@Url url: String,
	                           @Header("X-SecretKey") key: SecretKey,
	                           @Body dummy: Unit = Unit): SegmentListResponse

	/**
	 * Gets a list of all players (async) for the given request params.
	 *
	 * You should probably use `getPlayersAsync` or `getPlayers` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param key PlayFab title's admin/server secret key.
	 * @param body request parameters.
	 *
	 * @see getPlayersAsync
	 * @see getPlayers
	 */
	@POST
	fun getPlayersUrlAsync(@Url url: String,
	                       @Header("X-SecretKey") key: SecretKey,
	                       @Body body: PlayersInSegmentRequest): PlayersInSegmentResponse
}

/**
 * Gets a list of all segments (async) for the given title ID.
 * @param titleId title to pull segments for.
 * @param key PlayFab title's admin/server secret key.
 * @see getAllSegments
 */
fun SegmentApi.getAllSegmentsAsync(titleId: String, key: SecretKey)
		= getAllSegmentsUrlAsync(allSegmentsEndpoint(titleId), key)

/**
 * Gets a list of all segments (suspending) for the given title ID.
 * @param titleId title to pull segments for.
 * @param key PlayFab title's admin/server secret key.
 * @see getAllSegmentsAsync
 */
suspend fun SegmentApi.getAllSegments(titleId: String, key: SecretKey)
		= getAllSegmentsAsync(titleId, key).await()

/**
 * Gets a list of all players (async) for the given request params.
 * @param key PlayFab title's admin/server secret key.
 * @param body request parameters.
 * @see getPlayers
 */
fun SegmentApi.getPlayersAsync(key: SecretKey, body: PlayersInSegmentRequest)
		= getPlayersUrlAsync(playersEndpoint(body.TitleId), key, body)

/**
 * Gets a list of all players (suspending) for the given request params.
 * @param key PlayFab title's admin/server secret key.
 * @param body request parameters.
 * @see getPlayersAsync
 */
suspend fun SegmentApi.getPlayers(key: SecretKey, body: PlayersInSegmentRequest)
		= getPlayersAsync(key, body).await()
