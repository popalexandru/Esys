package com.example.leagueapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.database.SearchItem
import com.example.leagueapp.datastore.PreferencesManager
import com.example.leagueapp.models.ChampionData
import com.example.leagueapp.networking.model.Utils.APIResponse.NOT_FOUND
import com.example.leagueapp.networking.model.Utils.APIResponse.OK
import com.example.leagueapp.networking.model.Utils.APIResponse.FORBIDDEN
import com.example.leagueapp.networking.model.Utils.APIResponse.RATE_LIMIT_EXCEEDED
import com.example.leagueapp.networking.model.Utils.APIResponse.UNSUPPORTED
import com.example.leagueapp.networking.model.Utils.APIResponse.UNAUTHORIZED
import com.example.leagueapp.networking.model.Utils.APIResponse.BAD_REQUEST
import com.example.leagueapp.networking.model.Utils.APIResponse.INTERNAL_SERVER_ERROR
import com.example.leagueapp.networking.model.Utils.APIResponse.SERVICE_UNAVAILABLE
import com.example.leagueapp.networking.model.Utils.APIResponse.SERVER_INNACESSIBLE
import com.example.leagueapp.networking.model.api.RetrofitInstance
import com.example.leagueapp.networking.model.models.Region
import com.example.leagueapp.networking.model.models.Summoner
import com.example.leagueapp.networking.model.repository.Repository
import com.example.leagueapp.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.Headers
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
   @Inject constructor(
       private val repository: Repository,
       private val firebaseRepository: FirebaseRepository,
       private val preferencesManager: PreferencesManager
   ) : ViewModel()  {

    //private val repository = Repository()

    val searchQuery = MutableStateFlow("")
    val summonerExists = MutableStateFlow(false)

    val champExchange = MutableStateFlow(ChampionData())

    val preferencesFlow = preferencesManager.regionFlow
    val isAlarmScheduled = preferencesManager.alarmFlow

    fun setRegion(regionInput: String) = viewModelScope.launch {
        preferencesManager.updateRegion(regionInput)
    }

    fun setAlarm(yes : Boolean) = viewModelScope.launch {
        preferencesManager.isAlarmScheduled(yes)
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    private val searchesFlow = searchQuery
        .flatMapLatest { query ->
            repository.readSearches(query)
        }

    fun favoriteExists(accountId : String) = CoroutineScope(Dispatchers.IO).launch {
        summonerExists.value = repository.favoriteExists(accountId)
    }

    val favoritesFlow = repository.readFavorites().asLiveData()

    val searches = searchesFlow.asLiveData()

    private val _uiState : MutableStateFlow<SummonerUiState> = MutableStateFlow(SummonerUiState.Empty())
    val uiState : StateFlow<SummonerUiState> = _uiState

    fun emptyState(){
        _uiState.value = SummonerUiState.Empty()
    }

    val myResponse : MutableLiveData<Summoner> = MutableLiveData()
    val myHeaders : MutableLiveData<Headers> = MutableLiveData()

    fun getSummoner(name : String, regionInput : String) {
        viewModelScope.launch {
                _uiState.value = SummonerUiState.Loading()

            val newName = name.replace(" ", "%20")

            Timber.d("new name $newName")

                val response = repository.getSummonerByName(newName, regionInput.toLowerCase())

                println("HTTP Code ${response.code()}")

                when(response.code()){
                    NOT_FOUND.code -> {
                        _uiState.value = SummonerUiState.Error("Summoner not found, did you select the right server? | ${response.message()}")
                    }

                    OK.code -> {
                        val summonerResponse = response.body()!!
                        summonerResponse.lastTimeFetched = System.currentTimeMillis()
                        summonerResponse.region = regionInput

                        firebaseRepository.writeSummonerInDtabase(summonerResponse)
                        _uiState.value = SummonerUiState.Success(summonerResponse)
                        myHeaders.postValue(response.headers())
                    }

                    SERVER_INNACESSIBLE.code ->{
                        _uiState.value = SummonerUiState.Error("Server is innacesible | ${response.message()}")
                    }

                    FORBIDDEN.code -> {
                        _uiState.value = SummonerUiState.Error("API expired, contact the owner | ${response.message()}")
                    }

                    RATE_LIMIT_EXCEEDED.code -> {
                        _uiState.value = SummonerUiState.Error("Rate limit exceeded ! | ${response.message()}")
                    }

                    UNSUPPORTED.code -> {
                        _uiState.value = SummonerUiState.Error("Unsupported | ${response.message()}")
                    }

                    UNAUTHORIZED.code -> {
                        _uiState.value = SummonerUiState.Error("Authorization error | ${response.message()}")
                    }

                    BAD_REQUEST.code -> {
                        _uiState.value = SummonerUiState.Error("Bad request format | ${response.message()}")
                    }

                    INTERNAL_SERVER_ERROR.code -> {
                        _uiState.value = SummonerUiState.Error("Internal server error, try again later | ${response.message()}")
                    }

                    SERVICE_UNAVAILABLE.code -> {
                        _uiState.value = SummonerUiState.Error("Service unavailable, try again later | ${response.message()}")
                    }
                }

        }
    }

/*
    fun printDuration(rawResponse : Response){
        val duration = rawResponse.receivedResponseAtMillis() - rawResponse.sentRequestAtMillis()

        Log.d("APIResponse", "Get summoner duration: $duration ms")
        Log.d("APIResponse", "$rawResponse")
    }
*/

    fun insertSearch(search : String) = CoroutineScope(Dispatchers.IO).launch {
        repository.insertSearch(SearchItem(search))
    }

    fun insertFavorite(summoner: Summoner) = CoroutineScope(Dispatchers.IO).launch {
        repository.insertFavorite(FavoriteItem(summoner.accountId, summoner.profileIconId, summoner.name, summoner.summonerLevel, summoner.region))
    }

    fun deleteFavorite(favoriteItem: FavoriteItem) = CoroutineScope(Dispatchers.IO).launch {
        repository.deleteFavorite(favoriteItem)
    }

    sealed class SummonerUiState{
        data class Success(val summoner: Summoner) : SummonerUiState()
        data class Error(val exception: String) : SummonerUiState()
        class Loading() : SummonerUiState()
        class Empty() : SummonerUiState()
    }
}