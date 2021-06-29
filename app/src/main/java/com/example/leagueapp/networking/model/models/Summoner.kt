package com.example.leagueapp.networking.model.models

import java.io.Serializable

data class Summoner(
    val accountId : String,
    val profileIconId : Int,
    val revisionDate : Long,
    val name : String,
    val id : String,
    val puuid : String,
    val summonerLevel : Long,
    var lastTimeFetched : Long,
    var region: String
) : Serializable
