// DateDeserializer.kt
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2017-2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.kotlin.serialization

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * *Inflates a [Date] object from the given UNIX style epoch timestamp (or a fallback format).*
 *
 * @see GsonBuilder.registerTypeAdapter
 */
class DateDeserializer(fallbackDateFormats: Array<String>?) : JsonDeserializer<Date> {
    private val parsers by lazy { fallbackDateFormats?.map { fmt -> SimpleDateFormat(fmt) }?.toTypedArray() }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        try {
            return Date(json.asLong)
        } catch (e: Exception) {
            val jsonString = json.asString
            parsers?.forEach {
                val parser = it.clone() as DateFormat // `parse()` isn't reentrant, so we clone each SDF per-call
                try {
                    return parser.parse(jsonString)
                } catch (e: Exception) {
                    // ignore
                }
            }
            throw ParseException("Unknown date format $jsonString", 0)
        }
    }
}
