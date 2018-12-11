// segment.kt
// PlayFab Player Segment data models.
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


// region Segment Listing

data class SegmentData(val Id: String, val Name: String)

data class SegmentListResult(val Segments: List<SegmentData>)

typealias SegmentListResponse = DeferredResponse<SegmentListResult>

/**
 * @return a map of segment names to segment IDs.
 * @see SEGMENT_NAME_ALL_PLAYERS
 */
fun SegmentListResult.toSegmentMap() = Segments.associate { it.Name to it.Id }

/**
 * Default segment name for all players, for use with segment name-id map.
 * @see toSegmentMap
 */
const val SEGMENT_NAME_ALL_PLAYERS = "All Players"

// endregion

// region Players In Segment

inline class ContinuationToken(val value: String)

data class AccountInfo(
		val Platform: String,
		val PlatformUserId: String,
		val Email: String
)

data class PlayerProfile(
		val PlayerId: PlayFabId,
		val Created: Date,
		val LastLogin: Date,
		val LinkedAccounts: List<AccountInfo>
)

data class PlayersInSegmentRequest(
		@Expose(serialize = false, deserialize = false)
		val TitleId: String,
		val SegmentId: String,
		/** Default 1000, max 10,000 (will fail if greater). */
		val MaxBatchSize: Int = 1000,
		/** Default 300, max 1,800 (will fail if greater). */
		val SecondsToLive: Int = 300,
		val ContinuationToken: ContinuationToken? = null
)

data class PlayersInSegmentResult(
		val ProfilesInSegment: Int,
		val ContinuationToken: ContinuationToken?,
		val PlayerProfiles: List<PlayerProfile>
)

typealias PlayersInSegmentResponse = DeferredResponse<PlayersInSegmentResult>

// endregion
