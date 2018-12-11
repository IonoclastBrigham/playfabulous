// auth.kt
// PlayFab Authentication data models.
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
import java.util.*


// region Login Request

data class PlayerInfoRequestParams(
		val GetCharacterInventories: Boolean = false,
		val GetCharacterList: Boolean = false,
		val GetPlayerProfile: Boolean = false,
		val GetPlayerStatistics: Boolean = false,
		val GetTitleData: Boolean = false,
		val GetUserAccountInfo: Boolean = false,
		val GetUserData: Boolean = false,
		val GetUserInventory: Boolean = false,
		val GetUserReadOnlyData: Boolean = false,
		val GetUserVirtualCurrency: Boolean = false,
		val PlayerStatisticNames: List<String>? = null,
//		val ProfileConstraints: PlayerProfileViewConstraints? = null,
		val TitleDataKeys: List<String>? = null,
		val UserDataKeys: List<String>? = null,
		val UserReadOnlyDataKeys: List<String>? = null
)

data class EmailLoginRequest(
		val TitleId: String,
		val Email: String,
		val Password: String,
		val InfoRequestParameters: PlayerInfoRequestParams? = null
)

// endregion

// region Login Response

inline class AndroidDeviceInfo(val value: String) {
	override fun toString() = value
}

inline class IosDeviceInfo(val value: String) {
	override fun toString() = value
}

inline class XboxInfo(val value: String) {
	override fun toString() = value
}

data class CustomIdInfo(val CustomId: String)

data class PrivateAccountInfo(val Email: String)

data class UserAccountInfo(
		val Created: Date,
		val PlayFabId: PlayFabId,
		val Username: String? = null,
		val AndroidDeviceInfo: AndroidDeviceInfo? = null,
		val IosDeviceInfo: IosDeviceInfo? = null,
		val XboxInfo: XboxInfo? = null,
		val CustomIdInfo: CustomIdInfo? = null,
		val PrivateInfo: PrivateAccountInfo? = null
//		val FacebookInfo: FacebookInfo? = null,
//		val FacebookInstantGamesIdInfo: FacebookInstantGamesIdInfo? = null,
//		val GameCenterInfo: GameCenterInfo? = null,
//		val GoogleInfo: GoogleInfo? = null,
//		val KongregateInfo: KongregateInfo? = null,
//		val NintendoSwitchDeviceIdInfo: NintendoSwitchDeviceIdInfo? = null,
//		val OpenIdInfo: List<OpenIdInfo>? = null,
//		val PsnInfo: PsnInfo? = null,
//		val SteamInfo: SteamInfo? = null,
//		val TitleInfo: TitleInfo? = null,
//		val TwitchInfo: TwitchInfo? = null,
//		val WindowsHelloInfo: UserWindowsHelloInfo? = null,
)

enum class UserDataPermission {
	Private,
	Public
}

data class UserDataRecord(
	val Value: String? = null,
	val LastUpdated: Date? = null,
	val Permission: UserDataPermission? = null
)

data class PlayerProfile(
		val PublisherId: String,
		val TitleId: String,
		val PlayerId: PlayFabId
)

data class PlayerInfo(
	val AccountInfo: UserAccountInfo? = null,
	val TitleData: Map<String, String>? = null,
	val UserReadOnlyData: Map<String, UserDataRecord>? = null,
	val UserData: Map<String, UserDataRecord>? = null,
	val UserDataVersion: Long = 0L,
	val UserReadOnlyDataVersion: Long = 0L,
	val PlayerProfile: PlayerProfile? = null
//	val PlayerStatistics: List<StatisticValue>? = null,
//	val CharacterList: List<CharacterResult>? = null,
//	val CharacterInventories: List<CharacterInventory>? = null,
//	val UserInventory: List<ItemInstance>? = null,
//	val UserVirtualCurrency: Map<String, Int>? = null,
//	val UserVirtualCurrencyRechargeTimes: Map<String, VirtualCurrencyRechargeTime>? = null
)

data class LoginResult(
	val PlayFabId: PlayFabId,
	val SessionTicket: SessionTicket,
	val NewlyCreated: Boolean = false,
	val LastLoginTime: Date? = null,
	val InfoResultPayload: PlayerInfo? = null
//	val EntityToken: EntityTokenResponse? = null,
//	val SettingsForUser: UserSettings? = null
)

typealias LoginResponse = DeferredResponse<LoginResult>

// endregion

// region Session Validation Request

data class SessionValidationRequest(
		@Expose(serialize = false, deserialize = false)
		val TitleId: String = "",
		val SessionTicket: SessionTicket
)

// endregion

// region Session Validation Response

data class SessionValidationResult(val UserInfo: UserAccountInfo)

typealias SessionValidationResponse = DeferredResponse<SessionValidationResult>

// endregion
