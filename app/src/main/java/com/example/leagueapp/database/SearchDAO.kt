package com.example.leagueapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDAO {

    @Query("SELECT * FROM searches_table WHERE name LIKE '%' || :query || '%'")
    fun readSearches(query : String) : Flow<List<SearchItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search : SearchItem)

    @Delete
    suspend fun deleteSearch(search: SearchItem)
}