// auth.kt
// PlayFab Authentication interface and related declarations.
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
import com.ionoclast.playfabulous.model.EmailLoginRequest
import com.ionoclast.playfabulous.model.LoginResponse
import com.ionoclast.playfabulous.model.SessionValidationRequest
import com.ionoclast.playfabulous.model.SessionValidationResponse
import retrofit2.http.*


/**
 * PlayFab Authentication REST client interface for use with Retrofit2.
 * @author btoskin &lt;brigham@ionoclast.com&gt;
 */
interface AuthApi {
	companion object {
		fun emailLoginEndpoint(titleId: String) = endpointUrl(titleId, "Client/LoginWithEmailAddress")
		fun sessionValidateEndpoint(titleId: String) = endpointUrl(titleId, "Server/AuthenticateSessionTicket")
	}


	/**
	 * Logs into PlayFab title (async) with given credentials.
	 *
	 * You should probably use `loginWithEmailAsync` or `loginWithEmail` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param request login request parameters.
	 *
	 * @see loginWithEmailAsync
	 * @see loginWithEmail
	 */
	@POST
	fun loginWithEmailUrlAsync(@Url url: String, @Body request: EmailLoginRequest): LoginResponse

	/**
	 * Validates a PlayFab session ticket (async).
	 *
	 * You should probably use `validateSessionAsync` or `validateSession` instead.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param key Server/Admin API secret key.
	 * @param request validation request parameters.
	 *
	 * @see validateSessionAsync
	 * @see validateSession
	 */
	@POST
	fun validateSessionUrlAsync(@Url url: String, @Header("X-SecretKey") key: String, @Body request: SessionValidationRequest): SessionValidationResponse
}


/**
 * Logs into PlayFab title (async) with given credentials.
 * @param request login request parameters.
 * @see loginWithEmail
 */
fun AuthApi.loginWithEmailAsync(request: EmailLoginRequest)
		= loginWithEmailUrlAsync(AuthApi.emailLoginEndpoint(request.TitleId), request)

/**
 * Logs into PlayFab title (suspending) with given credentials.
 * @param request login request parameters.
 * @see loginWithEmailAsync
 */
suspend fun AuthApi.loginWithEmail(request: EmailLoginRequest)
		= loginWithEmailAsync(request).await()

/**
 * Validates a PlayFab session ticket (async).
 * @param titleId title ID that this session is associated with.
 * @param key Server/Admin API secret key.
 * @param request validation request parameters.
 * @see validateSession
 */
fun AuthApi.validateSessionAsync(titleId: String, key: String, request: SessionValidationRequest)
		= validateSessionUrlAsync(AuthApi.sessionValidateEndpoint(titleId), key, request)

/**
 * Validates a PlayFab session ticket (suspending).
 * @param titleId title ID that this session is associated with.
 * @param key Server/Admin API secret key.
 * @param request validation request parameters.
 * @see validateSessionAsync
 */
suspend fun AuthApi.validateSession(titleId: String, key: String, request: SessionValidationRequest)
		= validateSessionAsync(titleId, key, request).await()
