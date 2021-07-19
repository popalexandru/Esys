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
class RunesViewModel
   @Inject constructor(
       private val firebaseRepository: FirebaseRepository
   ) : ViewModel()  {



}