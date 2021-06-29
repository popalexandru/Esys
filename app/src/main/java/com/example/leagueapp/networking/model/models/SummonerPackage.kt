package com.example.leagueapp.networking.model.models

data class SummonerPackage(
        var championMasteries: List<ChampionMastery>,
        var soloEntry: LeagueEntry,
        var flexEntry: LeagueEntry,
        var matchList: List<Match>,
        var region : String
) {
    constructor() : this(emptyList(), LeagueEntry(), LeagueEntry(), emptyList(), "")
}
