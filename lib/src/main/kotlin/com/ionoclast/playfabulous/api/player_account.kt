// player_account.kt
// PlayFab Player Account interface and related declarations.
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

import com.ionoclast.playfabulous.endpointUrl
import com.ionoclast.playfabulous.model.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url


/**
 * PlayFab Player Account REST client interface for use with Retrofit2.
 * @author btoskin &lt;brigham@ionoclast.com&gt;
 */
interface PlayerAccountApi {
	companion object {
		fun registerAccountEndpoint(titleId: String) = endpointUrl(titleId, "Client/RegisterPlayFabUser")
		fun playerInfoEndpoint(titleId: String) = endpointUrl(titleId, "Server/GetPlayerCombinedInfo")
		fun userDataEndpoint(titleId: String) = endpointUrl(titleId, "Server/UpdateUserData")
	}


	/**
	 * Registers a new PlayFab account (async) with given credentials.
	 *
	 * You should probably use `registerAsync` or `register` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param request player account registration request parameters.
	 *
	 * @see registerAsync
	 * @see register
	 */
	@POST
	fun registerUrlAsync(@Url url: String, @Body request: RegisterPlayerRequest): RegisterUserResponse

	/**
	 * Gets requested player data (async) using given secret key.
	 *
	 * You should probably use `getPlayerInfoAsync` or `getPlayerInfo` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param key PlayFab title's admin/server secret key.
	 * @param request player data request params.
	 *
	 * @see getPlayerInfoAsync
	 * @see getPlayerInfo
	 */
	@POST
	fun getPlayerInfoUrlAsync(@Url url: String,
	                          @Header("X-SecretKey") key: SecretKey,
	                          @Body request: PlayerInfoRequest): PlayerInfoResponse

	/**
	 * Sets/updates requested player data (async) using given secret key.
	 *
	 * You should probably use `updateUserDataAsync` or `updateUserData` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param key PlayFab title's admin/server secret key.
	 * @param request player data update request params.
	 *
	 * @see updateUserDataAsync
	 * @see updateUserData
	 */
	@POST
	fun updateUserDataUrlAsync(@Url url: String,
	                           @Header("X-SecretKey") key: SecretKey,
	                           @Body request: UpdateUserDataRequest): UpdateUserDataResponse
}

/**
 * Registers a new PlayFab account (async) with given credentials.
 * @param request player account registration request parameters.
 * @see register
 */
fun PlayerAccountApi.registerAsync(request: RegisterPlayerRequest)
		= registerUrlAsync(PlayerAccountApi.registerAccountEndpoint(request.TitleId), request)

/**
 * Registers a new PlayFab account (suspending) with given credentials.
 * @param request player account registration request parameters.
 * @see registerAsync
 */
suspend fun PlayerAccountApi.register(request: RegisterPlayerRequest)
		= registerAsync(request).await()

/**
 * Gets requested player data (async) using given secret key.
 * @param key PlayFab title's admin/server secret key.
 * @param request player data request params.
 * @see getPlayerInfo
 */
fun PlayerAccountApi.getPlayerInfoAsync(key: SecretKey, request: PlayerInfoRequest)
		= getPlayerInfoUrlAsync(PlayerAccountApi.playerInfoEndpoint(request.TitleId), key, request)

/**
 * Gets requested player data (suspending) using given secret key.
 * @param key PlayFab title's admin/server secret key.
 * @param request player data request params.
 * @see getPlayerInfoAsync
 */
suspend fun PlayerAccountApi.getPlayerInfo(key: SecretKey, request: PlayerInfoRequest)
		= getPlayerInfoAsync(key, request).await()

/**
 * Sets/updates requested player data (async) using given secret key.
 * @param key PlayFab title's admin/server secret key.
 * @param request player data update request params.
 * @see updateUserData
 */
fun PlayerAccountApi.updateUserDataAsync(key: SecretKey, request: UpdateUserDataRequest)
		= updateUserDataUrlAsync(PlayerAccountApi.userDataEndpoint(request.TitleId), key, request)

/**
 * Sets/updates requested player data (suspending) using given secret key.
 * @param key PlayFab title's admin/server secret key.
 * @param request player data update request params.
 * @see updateUserDataAsync
 */
suspend fun PlayerAccountApi.updateUserData(key: SecretKey, request: UpdateUserDataRequest)
		= updateUserDataAsync(key, request).await()
