package com.example.leagueapp.networking.model.models

data class MiniSeries(
    val losses : Int,
    val progress : String,
    val target : Int,
    val wins : Int
){
    constructor() : this(0, "", 0, 0)
}
