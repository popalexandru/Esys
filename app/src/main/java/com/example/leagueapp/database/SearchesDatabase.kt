package com.example.leagueapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [SearchItem::class],
        version = 1
)
abstract class SearchesDatabase : RoomDatabase(){
    abstract fun getSearchesDao() : SearchDAO
}