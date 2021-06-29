package com.example.leagueapp.networking.model.models

data class LeagueEntry(
        val leagueId : String,
        val summonerId : String,
        val summonerName : String,
        val queueType : String,
        val tier : String,
        val rank : String,
        val leaguePoints : Int,
        val wins : Int,
        val loses : Int,
        val hotStreak : Boolean,
        val veteran : Boolean,
        val freshBlood : Boolean,
        val inactive : Boolean,
        val miniSeries : MiniSeries,
){
    constructor() : this("", "", "", "", "", "", 0, 0, 0, false, false, false, false,
                MiniSeries()
        )
}
