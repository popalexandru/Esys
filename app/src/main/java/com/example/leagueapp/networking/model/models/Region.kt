package com.example.leagueapp.networking.model.models

import java.util.*

enum class Region(val id: String, val regionName: String) {
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

    companion object {
        fun getPlatformById(id: String): Region {
            for (region in Region.values()) {
                if (region.id.equals(id, ignoreCase = true)) {
                    return region
                }
            }
            throw NoSuchElementException("Unknown platform ID: $id")
        }

        fun getPlatformByName(name: String): Region {
            for (region in Region.values()) {
                if (region.name.equals(name, ignoreCase = true)) {
                    return region
                }
            }
            throw NoSuchElementException("Unknown platform name: $name")
        }
    }
}