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

import com.google.gson.annotations.Expose


// region Player Register Request

data class RegisterPlayerRequest(
		val TitleId: String,
		val DisplayName: String? = null,
		val Username: String? = null,
		val Email: String? = null,
		val Password: String? = null,
		val RequireBothUsernameAndEmail: Boolean = false
)

// endregion

// region Player Register Response

data class RegisterPlayerResult(
		@Expose(serialize = false, deserialize = false)
		val TitleId: String = "",
		var PlayFabId: PlayFabId,
		var SessionTicket: SessionTicket?,
		var Username: String?
//		var SettingsForUser: UserSettings? = null,
//		var EntityToken: EntityTokenData? = null
)

typealias RegisterUserResponse = DeferredResponse<RegisterPlayerResult>

// endregion

// region Player Info Request

data class PlayerInfoRequest(
		val TitleId: String,
		val PlayFabId: PlayFabId,
		val InfoRequestParameters: PlayerInfoRequestParams
)

// endregion

// region Player Info Response

data class PlayerInfoResult(
		val PlayFabId: PlayFabId,
		val InfoResultPayload: PlayerInfo
)

typealias PlayerInfoResponse = DeferredResponse<PlayerInfoResult>

// endregion
