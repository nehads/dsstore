package com.nehads.friends.model.remote

data class Episodes(
    val name: String = "",
    val seasonNumber: Int = 0,
    val id: Int = 0,
    val episodes: List<EpisodesItem>?
)

data class EpisodesItem(
    val seasonNumber: Int = 0,
    val stillPath: String = "",
    val episodeNumber: Int = 0,
    val voteAverage: Double = 0.0,
    val name: String = "",
    val id: Int = 0,
    val voteCount: Int = 0
)

