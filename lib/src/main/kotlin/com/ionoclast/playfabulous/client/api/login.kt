// login.kt
// PlayFab Login interface and related declarations.
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.playfabulous.client.api

import com.ionoclast.playfabulous.client.model.EmailLoginRequest
import com.ionoclast.playfabulous.client.model.LoginResponse
import com.ionoclast.playfabulous.playFabApiBaseUrl
import retrofit2.http.*


/**
 * PlayFab Login REST client interface for use with Retrofit2.
 * @author btoskin &lt;brigham@ionoclast.com&gt;
 */
interface LoginApi {
	companion object {
		fun emailLoginEndpoint(titleId: String) = "${playFabApiBaseUrl(titleId)}Client/LoginWithEmailAddress"
	}

	/**
	 * Logs into PlayFab title (async) with given credentials.
	 *
	 * You should probably use `loginWithEmailAsync` or `loginWithEmail`.
	 *
	 * @param url required because any call could go to a different subdomain.
	 * @param request login request parameters.
	 */
	@POST
	fun loginWithEmailUrlAsync(@Url url: String, @Body request: EmailLoginRequest): LoginResponse

}


/**
 * Logs into PlayFab title (async) with given credentials.
 * @param request login request parameters.
 */
fun LoginApi.loginWithEmailAsync(request: EmailLoginRequest)
		= loginWithEmailUrlAsync(LoginApi.emailLoginEndpoint(request.TitleId), request)

/**
 * Logs into PlayFab title (suspending) with given credentials.
 * @param request login request parameters.
 */
suspend fun LoginApi.loginWithEmail(request: EmailLoginRequest)
		= loginWithEmailAsync(request).await()
