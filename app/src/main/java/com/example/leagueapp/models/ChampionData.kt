package com.example.leagueapp.models

import java.io.Serializable

data class ChampionData(
    val key : String,
    val id : String,
    val title : String,
    val tags : String,
    val skillList: MutableList<String>,
    val startItems: List<String>?,
    val recommendedItems: List<String>?,
    val situationalItems: List<String>?,
    val proTipsList : List<String>,
    val conTipsList : List<String>
    ) : Serializable{
    constructor() : this("", "", "", "", mutableListOf(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
    }