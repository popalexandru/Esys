package com.example.leagueapp.networking.model.models

data class MatchReference(
    val role : String,
    val platformId : String,
    val lane : String,
    val season : Int,
    val champion : Int,
    val queue : Int,
    val gameId : Long,
    val timestamp : Long,
){
    constructor() : this("", "", "", 0, 0, 0, 0L, 0L)
}
