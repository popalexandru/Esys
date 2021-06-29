package com.example.leagueapp.networking.model.api

import com.example.leagueapp.Constants.AWS_REQUEST_LINK
import com.example.leagueapp.networking.model.Utils.Constants
import com.example.leagueapp.networking.model.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface RiotApi {
    @GET(AWS_REQUEST_LINK)
    suspend fun getSummonerLeagues(
            @Query("region") region : String,
            @Query("encryptedSummonerId") encryptedSummonerId : String,
            @Query("request") request : String
    ) : Response<Set<LeagueEntry>>

    @GET(AWS_REQUEST_LINK)
    suspend fun getSummonerByRegion(
            @Query("region") region : String,
            @Query("summonerName") summonerName : String,
            @Query("request") request : String
    ) : Response<Summoner>

    @GET(AWS_REQUEST_LINK)
    suspend fun getChampionMastery(
            @Query("region") region : String,
            @Query("encryptedSummonerId") encryptedSummonerId : String,
            @Query("request") request : String
    ) : Response<List<ChampionMastery>>

     @GET(AWS_REQUEST_LINK)
     suspend fun getMatchV4(
             @Query("region") region: String,
             @Query("matchId") matchId: Long,
             @Query("request") request : String
     ) : Response<Match>

    @GET(AWS_REQUEST_LINK)
    suspend fun getMatchListV4(
            @Query("region") region: String,
            @Query("encryptedSummonerId") encryptedSummonerId : String,
            @Query("request") request : String
    ) : Response<MatchList>

    /***********************************
     ********     MATCH-V5    ***********
     ***********************************/

    @GET("https://vl4wzt8sg6.execute-api.us-east-2.amazonaws.com/stage/matchlist")
    suspend fun getMatchListV5(
            @Query("region") region: String,
            @Query("puuid") puuid : String
    ) : Response<List<String>>

    @GET("https://vl4wzt8sg6.execute-api.us-east-2.amazonaws.com/stage/match")
    suspend fun getMatchV5(
            @Query("region") region: String,
            @Query("matchId") matchId: String
    ) : Response<Match>
}