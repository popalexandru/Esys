package com.example.leagueapp.models

import com.example.leagueapp.networking.model.models.Participant

class ParticipantTeamPair(
        val participants : List<Participant>,
        val teamId : Int
)