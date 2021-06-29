package com.example.leagueapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorites_table")
data class FavoriteItem(
    @PrimaryKey
    @ColumnInfo(name = "accountId") val accountId : String,
    @ColumnInfo(name = "profileIconId") val profileIconId : Int,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name = "summonerLevel") val summonerLevel : Long,
    @ColumnInfo(name = "regionName") val regionName : String
) : Serializable
