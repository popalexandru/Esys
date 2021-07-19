package com.example.leagueapp.repository

import android.content.Context
import android.util.Log
import com.example.leagueapp.networking.model.models.RunesObj
import com.example.leagueapp.networking.model.models.Summoner
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.merakianalytics.orianna.Orianna
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import timber.log.Timber

import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    val firebase : FirebaseFirestore
) {
    fun writeSummonerInDtabase(summoner: Summoner){
        firebase.collection("Data").document(summoner.region).collection("Summoner Profiles").document(summoner.name).set(summoner)
            .addOnFailureListener {
                Timber.e("Inserting ${summoner.accountId} failed: ${it.message}")
            }
            .addOnCanceledListener {
                Timber.d("Inserting ${summoner.accountId} canceled")
            }
    }

/*    suspend fun readRunesForChamp(key : String) : Flow<RunesObj?> {
        val docRef = firebase.collection("Data").document("Runes").collection(key).orderBy("counter", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                val doc = it.documents.get(0).toObject(RunesObj::class.java)


            }
    }*/
}