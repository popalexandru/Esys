package com.example.leagueapp.models

import com.example.leagueapp.repository.ReadJsonRepository

data class PlayedChampModel(
    val id: Long,
    val points: Int,
    val champLevel: Int,
){
    var champName : String
    init {
        champName = ReadJsonRepository.getChampNameById(id.toString())
    }
}


