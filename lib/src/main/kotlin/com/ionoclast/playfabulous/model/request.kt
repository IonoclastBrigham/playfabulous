// request.kt
// PlayFab general request data models.
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


inline class SecretKey(val value: String) {
	override fun toString() = value
}

inline class SessionTicket(val value: String) {
	override fun toString() = value
}

inline class PlayFabId(val value: String) {
	override fun toString() = value
}
