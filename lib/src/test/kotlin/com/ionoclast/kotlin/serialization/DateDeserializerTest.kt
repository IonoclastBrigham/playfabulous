// DateDeserializerTest.kt
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.kotlin.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


private data class Timestamp(val timestamp: Date)

class DateDeserializerTest {
	companion object {
		const val TIME_LONG = 1540840680000L
		const val FALLBACK_FORMAT = "yyyy.MM.dd HH:mm:ss ZZ"
		const val TIME_LONG_JSON = "{\"timestamp\":$TIME_LONG}"
		const val TIME_STRING_JSON = "{\"timestamp\":\"2018.10.29 19:18:00 GMT-00:00\"}"
	}


	private lateinit var gson: Gson

	@BeforeTest
	fun initBefore() {
		gson = GsonBuilder()
				.registerTypeAdapter(Date::class.java, DateDeserializer(FALLBACK_FORMAT))
				.create()
	}

	@Test
	fun testDeserializeLong() {
		val timestamp = gson.fromJson(TIME_LONG_JSON, Timestamp::class.java)
		assertEquals(TIME_LONG, timestamp.timestamp.time, "Parsed long should match")
	}

	@Test
	fun testDeserializeString() {
		val timestamp = gson.fromJson(TIME_STRING_JSON, Timestamp::class.java)
		assertEquals(TIME_LONG, timestamp.timestamp.time, "Parsed long should match")
	}
}
