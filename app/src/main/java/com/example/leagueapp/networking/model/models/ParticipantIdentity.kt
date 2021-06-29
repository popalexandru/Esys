package com.example.leagueapp.networking.model.models

import java.io.Serializable

data class ParticipantIdentity(
        val participantId : Int,
        val player: Player
) : Serializable {
    constructor() : this(0, Player())
}
