package com.example.leagueapp.networking.model.models

data class TeamStats(
        val towerKills : Int,
        val riftHeraldKills : Int,
        val inhibitorKills : Int,
        val dominionVictoryScore : Int,
        val dragonKills : Int,
        val baronKills : Int,
        val vilemawKills : Int,
        val teamId : Int,
        val firstBlood : Boolean,
        val firstBaron : Boolean,
        val firstDragon : Boolean,
        val firstInhibitor : Boolean,
        val firstTower : Boolean,
        val firstRiftHerald : Boolean,
        val win : String,
        val bans : List<TeamBans>
)
