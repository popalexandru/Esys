package com.example.leagueapp.networking.model.Utils

enum class Platform(val id : String, name : String) {
    BR("BR1", "br"),
    EUNE("EUN1", "eune"),
    EUW("EUW1", "euw"),
    JP("JP1", "jp"),
    KR("KR", "kr"),
    LAN("LA1", "lan"),
    LAS("LA2", "las"),
    NA("NA1", "na"),
    OCE("OC1", "oce"),
    RU("RU", "ru"),
    TR("TR1", "tr");

    fun getHost() : String {
        return "https://" + id.toLowerCase() + "api.riotgames.com"
    }
}