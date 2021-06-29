package com.example.leagueapp.ui.main

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueapp.Constants.FLEX
import com.example.leagueapp.Constants.SOLO_DUO
import com.example.leagueapp.networking.model.models.*
import com.example.leagueapp.networking.model.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Headers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel
@Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _uiState : MutableStateFlow<SummonerUiState> = MutableStateFlow(SummonerUiState.Empty())
    val uiState : StateFlow<SummonerUiState> = _uiState

    val myResponse : MutableLiveData<Summoner> = MutableLiveData()
    val myHeaders : MutableLiveData<Headers> = MutableLiveData()

    val summonerLeagues = MutableStateFlow<Set<LeagueEntry>>(emptySet())
    val matchHistory = MutableStateFlow<MatchList>(MatchList())
    var lastMatchFetched = 0L
    private lateinit var summonerPackage: SummonerPackage

    private var matchesList : MutableList<MatchReference> = mutableListOf()

    private val matchesListLive : MutableLiveData<List<Match>> = MutableLiveData()

    fun getLiveMatches() : LiveData<List<Match>>{
        return matchesListLive
    }

    suspend fun getChampionMasteries(encryptedId: String, summoner: SummonerPackage) = repository.getChampionMasteries(encryptedId, summoner.region).collect {
        summoner.championMasteries = it.body()!!
    }

    suspend fun getSummonerLeagues(encryptedId: String, summonerPackage: SummonerPackage) = repository.getSummonerLeagues(encryptedId, summonerPackage.region).collect {
        for(items in it){
            if(items.queueType.equals(SOLO_DUO)){
                summonerPackage.soloEntry = items
            }

            if(items.queueType.equals(FLEX)){
                    summonerPackage.flexEntry = items
            }
        }
    }

/*    suspend fun getMatches(accountId: String, summonerPackage: SummonerPackage){
        repository.getMatchList(accountId).collect {
            summonerPackage.matchList = it
        }
    }*/

    /*********************************
     *  fetch the matches from
     *  the matchesReferences
     **********************************/
    suspend fun getMatches(accountId: String, summonerPackage: SummonerPackage, puuid: String){
/*        val matchReferences = repository.getMatchReferences(accountId)
        matchesList = matchReferences as MutableList<MatchReference>*/
        //matchesList = repository.getMatchReferences(accountId) as MutableList<MatchReference>

        matchesList = repository.getMatchListV4(accountId, summonerPackage.region) as MutableList<MatchReference>

        /**********************/

        val returnMatchList : MutableList<Match> = mutableListOf()

        val job = coroutineScope {
            launch { repository.getMatch(matchesList[0].gameId, returnMatchList, summonerPackage.region) }
            launch { repository.getMatch(matchesList[1].gameId, returnMatchList, summonerPackage.region) }
            launch { repository.getMatch(matchesList[2].gameId, returnMatchList, summonerPackage.region) }
            launch { repository.getMatch(matchesList[3].gameId, returnMatchList, summonerPackage.region) }
        }

        matchesList.removeAt(0)
        matchesList.removeAt(0)
        matchesList.removeAt(0)
        matchesList.removeAt(0)

        job.join()
        sortMatchList(returnMatchList)
        summonerPackage.matchList = returnMatchList
    }

    /*********************************
     *  function called when recycler
     *  view load more is called
     **********************************/
    fun loadMoreGames() = viewModelScope.launch {
        val matchToFetch1 = matchesList[0].gameId
        val matchToFetch2 = matchesList[1].gameId
        val matchToFetch3 = matchesList[2].gameId

        val theList = summonerPackage.matchList as MutableList<Match>
        val job = coroutineScope {
            launch{ repository.getMatch(matchToFetch1, theList, summonerPackage.region)}
            launch{ repository.getMatch(matchToFetch2, theList, summonerPackage.region)}
            launch{ repository.getMatch(matchToFetch3, theList, summonerPackage.region)}
        }
        job.join()

        matchesList.removeAt(0)
        matchesList.removeAt(0)
        matchesList.removeAt(0)

        sortMatchList(theList)
        matchesListLive.postValue(theList)

/*        _uiState.value = SummonerUiState.Success(
            summonerPackage
        )*/
    }

    /*********************************
     *  sort match list after fetch
     **********************************/
    private fun sortMatchList(returnMatchList: MutableList<Match>) {
        returnMatchList.sortWith { o1, o2 -> o2.gameCreation.compareTo(o1.gameCreation) }
    }
    /** sortMatchList **/


    /*********************************
     *  lauch fetch of masteries leagues and matches
     *  and create the summoner package
     **********************************/
    suspend fun getAll(encryptedId : String, accountId : String, puuid : String, region: String){
        val timeBefore = System.currentTimeMillis()
        val summoner = SummonerPackage()
        summoner.region = region

        val job = coroutineScope {
            launch  { getChampionMasteries(encryptedId, summoner) }
            launch  { getSummonerLeagues(encryptedId, summoner) }
            launch  { getMatches(accountId, summoner, puuid) }
        }

        job.join()

        val timeAfter = System.currentTimeMillis()

        val timeSpent = timeAfter - timeBefore

        Log.d("timespent","Time spent for fetching summoner $timeSpent")

        lastMatchFetched = 4

        summonerPackage = summoner
        _uiState.value = SummonerUiState.Success(
                summoner
        )
    } /** getAll **/

    sealed class SummonerUiState{
        data class Success(val summonerPackage: SummonerPackage) : SummonerUiState()
        data class Error(val exception: String) : SummonerUiState()
        class Loading() : SummonerUiState()
        class Empty() : SummonerUiState()
    }
}