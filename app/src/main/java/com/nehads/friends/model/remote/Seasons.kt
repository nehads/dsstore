package com.nehads.friends.model.remote

data class Seasons(
    val numberOfEpisodes: Int = 0,
    val type: String = "",
    val id: Int = 0,
    val numberOfSeasons: Int = 0,
    val voteCount: Int = 0,
    val seasons: List<SeasonsItem>?,
    val name: String = ""
)

data class SeasonsItem(
    val airDate: String = "",
    val overview: String = "",
    val episodeCount: Int = 0,
    val name: String = "",
    val seasonNumber: Int = 0,
    val id: Int = 0,
    val posterPath: String = ""
)

