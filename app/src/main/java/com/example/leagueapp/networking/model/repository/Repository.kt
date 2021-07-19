package com.example.leagueapp.networking.model.repository

import com.example.leagueapp.database.FavoriteDAO
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.database.SearchDAO
import com.example.leagueapp.database.SearchItem
import com.example.leagueapp.networking.model.api.RetrofitInstance
import com.example.leagueapp.networking.model.api.RiotApi
import com.example.leagueapp.networking.model.models.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class Repository @Inject constructor(
    val searchesDao : SearchDAO,
    val favoriteDAO: FavoriteDAO,
    val firebase : FirebaseFirestore
){
    var lastRegion : String = ""

    // AWS
    suspend fun getSummonerByName(name : String, region: String) : Response<Summoner> {
        Timber.d("Fetching the summoner called $name")
        return RetrofitInstance.apige.getSummonerByRegion(region, name, "summoner")
    }

    // AWS
    suspend fun getChampionMasteries(encryptedId :String , region: String) : Flow<Response<List<ChampionMastery>>>{
        return flow {
            Timber.d("Fetching the champions masteries")
            emit(RetrofitInstance.apige.getChampionMastery(region.toLowerCase(), encryptedId, "mastery"))
        }
    }

    // AWS
    suspend fun getSummonerLeagues(encryptedId: String, region: String) : Flow<MutableList<LeagueEntry>>{
        return flow{
            Timber.d("Fetching the summoner leagues")
            val returnValue = RetrofitInstance.apige.getSummonerLeagues(region, encryptedId, "leagues")

            val leaguesSet = returnValue.body()

            val leaguesList = mutableListOf<LeagueEntry>()

            leaguesSet?.forEach {
                leaguesList.add(it)
            }

            emit( leaguesList )
        }

    }

    suspend fun getMatch(gameId : Long, list : MutableList<Match>, region: String){
        Timber.d("Fetching the match $gameId")
        val match = RetrofitInstance.apige.getMatchV4(region, gameId, "match").body()
            if (match != null) {
                match.loadDetails()
                list.add(match)

                addMatchToFirebase(match, region)
            }
    }

    fun addMatchToFirebase(match: Match, region: String){
        firebase.collection("Data").document(region).collection("Games").document(match.gameId.toString()).set(match)
            .addOnFailureListener {
                Timber.e("Inserting ${match.gameId} failed: ${it.message}")
            }
            .addOnCanceledListener {
                Timber.d("Inserting ${match.gameId.toString()} canceled")
            }

        for(participant in match.participants){
            val participantTeam = participant.teamId
            var win = 0;

            if(match.teams[0].win == "Win" && (participantTeam == match.teams[0].teamId)) win = 1

            val runes = hashMapOf(
                "primaryStyle" to participant.stats.perkPrimaryStyle,
                "subStyle" to participant.stats.perkSubStyle,
                "perk0" to participant.stats.perk0,
                "perk1" to participant.stats.perk1,
                "perk2" to participant.stats.perk2,
                "perk3" to participant.stats.perk3,
                "perk4" to participant.stats.perk4,
                "perk5" to participant.stats.perk5,
                "counter" to 1,
                "wins" to win
            )

            val runesName = participant.stats.perk0.toString() +
             participant.stats.perk1.toString() +
             participant.stats.perk2.toString() +
             participant.stats.perk3.toString() +
             participant.stats.perk4.toString() +
             participant.stats.perk5.toString()

            Timber.d("Naming of namin ${participant.championName} which is ${participant.championId}")

            firebase.collection("Data").document("Runes").collection(participant.championId.toString()).document(runesName).get()
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val document = it.result

                        if (document != null) {
                            if(document.exists()){
                                Timber.d("document $runesName existed")

                                firebase.collection("Data").document("Runes").collection(participant.championId.toString()).document(runesName)
                                    .update("counter", FieldValue.increment(1))

                                if(win == 1){
                                    firebase.collection("Data").document("Runes").collection(participant.championId.toString()).document(runesName)
                                        .update("wins", FieldValue.increment(1))
                                }
                            }else{
                                Timber.d("document $runesName DID NOT exist")

                                firebase.collection("Data").document("Runes").collection(participant.championId.toString())
                                    .document(runesName).set(runes)
                            }
                        }
                    }
                }

        }

    }



    suspend fun getMatchListV4(accountId: String, region : String) : List<MatchReference>?{
        val reply =  RetrofitInstance.apige.getMatchListV4(region, accountId, "matchList")
        return reply.body()?.matches
    }

    suspend fun getMatchV4(matchId: Long) : Match? {
        return RetrofitInstance.apige.getMatchV4("eun1", matchId, "match").body()
    }

    suspend fun getMatchReference2(accountId: String) : List<String>?{
        val reply = RetrofitInstance.apige.getMatchListV5("europe", accountId)
        Timber.d("${reply.message()} woaaaaaaaaaaaaaaw")
        return reply.body()
    }

    suspend fun getMatch2(gameId: String) : Match? {
        val reply = RetrofitInstance.apige.getMatchV5("europe", gameId)
        Timber.d("ad")
        return reply.body()
    }
/*
    suspend fun getMatchList(accountId : String) : Flow<List<Match>> {
        val timeBefore = System.currentTimeMillis()

        val matchList = RetrofitInstance.apige.getMatchHistory(accountId).body()?.matches
        val returnMatchList : MutableList<Match> = mutableListOf()

        val matchIds : MutableList<Long> = mutableListOf()

        for(index in 0..4){
            matchList?.get(index)?.let { matchIds.add(it.gameId) }
        }

        val job = coroutineScope {
            launch { getMatch(matchIds[0], returnMatchList) }
            launch { getMatch(matchIds[1], returnMatchList) }
            launch { getMatch(matchIds[2], returnMatchList) }
            launch { getMatch(matchIds[3], returnMatchList) }
            launch { getMatch(matchIds[4], returnMatchList) }
        }

        job.join()
        
        sortMatchList(returnMatchList)

*//*        for(index in 0..4){
            val matchReference = matchList?.get(index)

            val currentMatch = matchReference?.let { RetrofitInstance.apige.getMatch(it.gameId) }

            if (currentMatch != null) {
                if(currentMatch.isSuccessful){
                    currentMatch.body()?.let { returnMatchList.add(it) }
                }
            }

        }*//*



        val timeAfter = System.currentTimeMillis()

        val timeSpent = timeAfter - timeBefore

        Log.d("timespent","Time spent for fetching games $timeSpent")

        return flow {
            emit(returnMatchList)
        }
    }*/

    private fun sortMatchList(returnMatchList: MutableList<Match>) {
        returnMatchList.sortWith { o1, o2 -> o2.gameCreation.compareTo(o1.gameCreation) }
    }

    /************************************
            FAVORITES OPERATIONS
     ************************************/

    fun readFavorites() : Flow<List<FavoriteItem>> = favoriteDAO.readSearches()

    suspend fun insertFavorite(favoriteItem: FavoriteItem) = favoriteDAO.insertSearch(favoriteItem)

    suspend fun deleteFavorite(favoriteItem: FavoriteItem) = favoriteDAO.deleteSearch(favoriteItem)

    fun favoriteExists(accountId: String) = favoriteDAO.favoriteExists(accountId)

    /************************************
            DATABASE OPERATIONS
    ************************************/

    fun readSearches(query : String) : Flow<List<SearchItem>> = searchesDao.readSearches(query)

    suspend fun insertSearch(searchItem: SearchItem) = searchesDao.insertSearch(searchItem)

    fun deleteSearcg(searchItem: SearchItem) = CoroutineScope(Dispatchers.IO).launch {
        searchesDao.deleteSearch(searchItem)
    }

}