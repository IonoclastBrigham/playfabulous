// ClearableLazyInstanceTest.kt
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.kotlin.delegate

import kotlin.test.Test
import kotlin.test.assertEquals


class ClearableLazyInstanceTest {
	companion object {
		const val MESSAGE = "Hello, Ionoclast!"
		const val MESSAGE_LEN = MESSAGE.length
	}


	private var initRunCount = 0
	private val clearableField by clearableLazy {
		++initRunCount
		MESSAGE
	}

	@Test
	fun testIsLazy() {
		assertEquals(0, initRunCount, "Initialization should be lazy")
		clearableField.length
		assertEquals(1, initRunCount, "Init should run on demand")
	}

	@Test
	fun testValueInit() {
		assertEquals(0, initRunCount, "Initialization should be lazy")
		assertEquals(MESSAGE, clearableField,
				"Field should be set with result of init block")
		assertEquals(1, initRunCount, "Init should be run on first usage")
	}

	@Test
	fun testMemoizedLazyValue() {
		assertEquals(0, initRunCount, "Initialization should be lazy")
		clearableField.length
		assertEquals(1, initRunCount, "Init should be run on first usage")
		clearableField.length
		assertEquals(1, initRunCount, "Init should not run on subsequent accesses")
	}

	@Test
	fun testClearField() {
		assertEquals(0, initRunCount, "Initialization should be lazy")
		clearableField.length
		assertEquals(1, initRunCount, "Init should be run on first usage")
		::clearableField.clear()
		assertEquals(1, initRunCount, "Init should not run after clear unless accessed")
	}

	@Test
	fun testLazyReInit() {
		assertEquals(0, initRunCount, "Initialization should be lazy")
		clearableField.length
		assertEquals(1, initRunCount, "Init should be run on first usage")
		::clearableField.clear()
		assertEquals(1, initRunCount, "Init should not run after clear unless accessed")
		clearableField.length
		assertEquals(2, initRunCount, "Init should be re-run on first usage after clearing")
	}

	@Test
	fun testLocalProperty() {
		val local by clearableLazy { MESSAGE }
		assertEquals(MESSAGE_LEN, local.length)
		assertEquals(MESSAGE, local)

		// TODO: test clearing (references not supported by Kotlin yet)
		//::local.clear()
	}
}
