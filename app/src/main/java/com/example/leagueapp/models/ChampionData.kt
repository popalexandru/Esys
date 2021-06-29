package com.example.leagueapp.models

class ChampionData(
    key: String, id: String, title: String, tags: String, skillList: MutableList<String>, startItems: List<String>?,
    recommendedItems: List<String>?, situationalItems: List<String>?){

    var keyIn  = key
    var idIn = id
    var skills = skillList
    var titles = title

}