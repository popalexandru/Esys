package com.example.leagueapp.repository

import android.content.Context
import android.util.Log
import com.example.leagueapp.networking.model.models.Summoner
import com.google.firebase.firestore.FirebaseFirestore
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
        Log.d("Firebase", "Writing ${summoner.accountId} to ${firebase.app.name}")
        //firebase.collection("Summoners").document(summoner.accountId).set(summoner)

        firebase.collection("EUNE").document("Summoners").set(summoner)
            .addOnFailureListener {
                Timber.e("Inserting ${summoner.accountId} failed: ${it.message}")
            }
            .addOnCanceledListener {
                Timber.d("Inserting ${summoner.accountId} canceled")
            }
    }
}