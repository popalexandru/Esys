package com.example.leagueapp.networking.model.models

data class ParticipantTimeline(
        val participantId : Int = 0,
        val csDiffPerMinDeltas : Map<String, Double> = mapOf(),
        val damageTakenPerMinDeltas : Map<String, Double> = mapOf(),
        val role : String = "",
        val damageTakenDiffPerMinDeltas : Map<String, Double> = mapOf(),
        val xpPerMinDeltas : Map<String, Double> = mapOf(),
        val xpDiffPerMinDeltas : Map<String, Double> = mapOf(),
        val lane : String = "",
        val creepsPerMinDeltas : Map<String, Double> = mapOf(),
        val goldPerMinDeltas : Map<String, Double> = mapOf()
)
