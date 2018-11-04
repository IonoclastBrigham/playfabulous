// login.kt
// PlayFab Login data models.
//
// btoskin <brigham@ionoclast.com>
// Copyright © 2018 Ionoclast Laboratories, LLC.
//
// This software is part of the PlayFabulous library. It is distributable
// under the terms of a modified MIT License. You should have received a copy of
// the license in the file LICENSE. If not, see:
//   <https://github.com/IonoclastBrigham/playfabulous/blob/master/LICENSE>
////////////////////////////////////////////////////////////////////////////////


package com.ionoclast.playfabulous.client.model

import java.util.*


//region Login Request

data class PlayerInfoRequestParams(
		var GetCharacterInventories: Boolean = false,
		var GetCharacterList: Boolean = false,
		var GetPlayerProfile: Boolean = false,
		var GetPlayerStatistics: Boolean = false,
		var GetTitleData: Boolean = false,
		var GetUserAccountInfo: Boolean = false,
		var GetUserData: Boolean = false,
		var GetUserInventory: Boolean = false,
		var GetUserReadOnlyData: Boolean = false,
		var GetUserVirtualCurrency: Boolean = false,
		var PlayerStatisticNames: List<String>? = null,
//		var ProfileConstraints: PlayerProfileViewConstraints? = null,
		var TitleDataKeys: List<String>? = null,
		var UserDataKeys: List<String>? = null,
		var UserReadOnlyDataKeys: List<String>? = null
)

data class EmailLoginRequest(
		val TitleId: String,
		val Email: String,
		val Password: String,
		val InfoRequestParameters: PlayerInfoRequestParams? = null
)

//endregion

//region Login Response

inline class SessionTicket(val value: String)

inline class PlayFabId(val value: String)

enum class UserDataPermission {
	Private,
	Public
}

data class UserDataRecord(
	var Value: String? = null,
	var LastUpdated: Date? = null,
	var Permission: UserDataPermission? = null
)

data class PlayerInfo(
//	var AccountInfo: UserAccountInfo? = null,
	var TitleData: Map<String, String>? = null,
	var UserReadOnlyData: Map<String, UserDataRecord>? = null,
	var UserData: Map<String, UserDataRecord>? = null,
	var UserDataVersion: Long = 0L,
	var UserReadOnlyDataVersion: Long = 0L
//	var PlayerProfile: PlayerProfileModel? = null,
//	var PlayerStatistics: List<StatisticValue>? = null,
//	var CharacterList: List<CharacterResult>? = null,
//	var CharacterInventories: List<CharacterInventory>? = null,
//	var UserInventory: List<ItemInstance>? = null,
//	var UserVirtualCurrency: Map<String, Int>? = null,
//	var UserVirtualCurrencyRechargeTimes: Map<String, VirtualCurrencyRechargeTime>? = null
)

data class LoginResult(
	var PlayFabId: PlayFabId,
	var SessionTicket: SessionTicket,
	var NewlyCreated: Boolean = false,
	var LastLoginTime: Date? = null,
	var InfoResultPayload: PlayerInfo? = null
//	var EntityToken: EntityTokenResponse? = null,
//	var SettingsForUser: UserSettings? = null
)

typealias LoginResponse = PlayFabResponse<LoginResult>

//endregion
