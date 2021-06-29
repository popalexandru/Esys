package com.example.leagueapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {

    @Query("SELECT * FROM favorites_table")
    fun readSearches() : Flow<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(favoriteItem : FavoriteItem)

    @Query("SELECT EXISTS(SELECT * FROM favorites_table WHERE accountId = :accountId)")
    fun favoriteExists(accountId : String) : Boolean

    @Delete
    suspend fun deleteSearch(favoriteItem: FavoriteItem)
}