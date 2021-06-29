package com.example.leagueapp.datastore
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

data class FilterPreferences(val region : String){

}

data class FilterPreferences2(val yes : Boolean){

}

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context : Context){
    private val dataStore = context.createDataStore("user_preferences")

    val regionFlow = dataStore.data
            .catch { exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map { preferences ->
                val region = preferences[PreferenceKeys.REGION] ?: ""
                FilterPreferences(region)
            }

    val alarmFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val alarm = preferences[PreferenceKeys.SCHEDULED_ALARM] ?: false
            FilterPreferences2(alarm)
        }

    suspend fun updateRegion(region: String){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.REGION] = region
        }
    }

    suspend fun isAlarmScheduled(yes : Boolean){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SCHEDULED_ALARM] = yes
        }
    }

    private object PreferenceKeys{
        val REGION = preferencesKey<String>("region")
        val SCHEDULED_ALARM = preferencesKey<Boolean>("scheduled")
    }
}