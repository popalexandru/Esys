package com.example.leagueapp.networking.model.models

data class MatchList(
    val startIndex : Int,
    val totalGames : Int,
    val endIndex : Int,
    val matches : List<MatchReference>
){
    constructor() : this(0, 0, 0,
        emptyList()
        )
}
