// AbstractRestClientTeset.kt
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

import com.ionoclast.kotlin.delegate.clear
import com.ionoclast.kotlin.delegate.clearableLazy
import com.ionoclast.kotlin.net.AbstractRestClient.RestResponse
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import kotlin.test.*


private data class Stuff(val authorizations_url: String,
                         val code_search_url: String,
                         val current_user_url: String)
private typealias StuffResponse = Deferred<RestResponse<Stuff>>

private interface TestApi {
	@GET("/")
	fun getStuff(): StuffResponse
}

private class TestClient : AbstractRestClient() {
	override val baseUri = "https://api.github.com" // TODO: mock server on localhost
	override val logLevel = HttpLoggingInterceptor.Level.NONE
	override val maxIdleConnections = 1

	val testApi by clearableLazy { restClient.create(TestApi::class.java)!! }

	override fun cleanup() {
		super.cleanup()
		::testApi.clear()
	}
}

class AbstractRestClientTest {
	private var client: TestClient? = null
	private var result: Stuff? = null
	private var error: Throwable? = null

	@BeforeTest
	fun initBefore() = runBlocking {
		client = TestClient()
		val response = client!!.testApi.getStuff().await()
		result = response.result
		error = response.err
	}

	@Test
	fun testRestClient() {
		assertNotNull(result, "Did not receive valid result")
		assertNull(error, error?.message)
	}

	@Test
	fun testGetResults() {
		val client = client!!
		val result = result!!

		assertFalse(result.authorizations_url.isBlank(), "Should receive auth url")
		assertTrue(result.authorizations_url.startsWith(client.baseUri), "Should receive auth url")

		assertFalse(result.code_search_url.isBlank(), "Should receive code search url")
		assertTrue(result.code_search_url.startsWith(client.baseUri), "Should receive code search url")

		assertFalse(result.current_user_url.isBlank(), "Should receive user url")
		assertTrue(result.current_user_url.startsWith(client.baseUri), "Should receive user url")
	}

	@AfterTest
	fun cleanup() {
		client!!.cleanup()
		client = null
		result = null
		error = null
	}
}
