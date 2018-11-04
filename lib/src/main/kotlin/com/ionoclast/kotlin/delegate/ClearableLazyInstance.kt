// ClearableLazyInstance.kt
// Delegate class and related declarations.
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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible


typealias Initializer<T> = (()->T)

/**
 * Standard function for creating a clearable lazy property delegate.
 *
 * @see ClearableLazyInstance
 * @see clear
 */
fun <T : Any> clearableLazyInstance(init: Initializer<T>) : ReadOnlyProperty<Any, T> = ClearableLazyInstance(init)

/**
 * Property delegate similar to default `lazy`, but the backing field is clearable and re-inita'able.
 *
 * To clear a heavyweight property after it's not needed (but might be again later):
 *
 * ```
 * class ResourceHolder(resPath: File) {
 *     val res: Resource by clearableLazyInstance { loadResource(resPath) }
 *
 *     fun loadResource() = ...
 *
 *     fun cleanup() {
 *         res.dispose()
 *         ::res.clear()
 *     }
 * }
 * ```
 *
 * @see clearableLazyInstance
 * @see clear
 */
class ClearableLazyInstance<T : Any>(private val init: Initializer<T>) : ReadOnlyProperty<Any, T> {
	private var field: Any? = null

	override fun getValue(thisRef: Any, property: KProperty<*>) = synchronized(this) {
		if (field === null) {
			field = init()
		}
		@Suppress("UNCHECKED_CAST")
		field as T
	}

	fun clear() {
		field = null
	}
}

fun <T : Any> KProperty0<T>.clear() {
	isAccessible = true

	@Suppress("UNCHECKED_CAST")
	(getDelegate() as ClearableLazyInstance<T>).clear()
}
