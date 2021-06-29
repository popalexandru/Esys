package com.example.leagueapp.networking.model.models

import com.example.leagueapp.repository.ReadJsonRepository

data class PlayerObject(
    val participant: Participant,
    val participantIdentity: ParticipantIdentity,
    val region: String
)
