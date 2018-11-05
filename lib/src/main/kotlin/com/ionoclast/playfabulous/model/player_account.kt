// player_account.kt
// PlayFab Player Account data models.
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


//region Player Register Request

data class RegisterPlayerRequest(
		val TitleId: String,
		val DisplayName: String? = null,
		val Username: String? = null,
		val Email: String? = null,
		val Password: String? = null,
		val RequireBothUsernameAndEmail: Boolean = false
)

//endregion

//region Player Register Response

data class RegisterPlayerResult(
		var PlayFabId: PlayFabId,
		var SessionTicket: SessionTicket?,
		var Username: String?
//		var SettingsForUser: UserSettings? = null,
//		var EntityToken: EntityTokenData? = null
)

typealias RegisterUserResponse = PlayFabResponse<RegisterPlayerResult>

//endregion
