package com.example.leagueapp.networking.model.models

import com.example.leagueapp.repository.ReadJsonRepository
import timber.log.Timber
import java.io.Serializable
import java.util.concurrent.TimeUnit

data class Match(
        val gameId : Long,
        val queueId : Int,
        val gameType : String,
        val gameDuration : Long,
        val platformId : String,
        val gameCreation : Long,
        val gameStartTimestamp : Long,
        val seasonId : Int,
        val gameVersion : String,
        val mapId : String,
        val gameMode : String,
        val participantIdentities : List<ParticipantIdentity>,
        val teams : List<TeamStats>,
        val participants : List<Participant>,
        var isRemake : Boolean = false,
        var duration : String = "N/A",
        var queue : Queue
) : Serializable {




    fun loadDetails(){
        val durationInMinutes = TimeUnit.SECONDS.toMinutes(gameDuration)
        val secondsRemained = gameDuration - TimeUnit.MINUTES.toSeconds(durationInMinutes)

        for(p in participants){
            p.championName = ReadJsonRepository.getChampNameById(p.championId.toString())
        }

        if(durationInMinutes < 5){
            isRemake = true
        }else{
            duration = "$durationInMinutes m $secondsRemained s"
        }

        queue = Queue.withID(queueId)

        Timber.d("Init of $gameId game duration: $gameDuration ${TimeUnit.MINUTES.toMillis(5)}")

        determineTheCarry()
    }

    private fun determineTheCarry(){
        var bestCarryScore1 = 0.0
        var bestCarryScore2 = 0.0

        for(participant in participants){
            participant.carryScore = computeCarryScore(participant.stats)

            if(participant.teamId == 100){
                if(participant.carryScore > bestCarryScore1) bestCarryScore1 = participant.carryScore
            }else{
                if(participant.carryScore > bestCarryScore2) bestCarryScore2 = participant.carryScore
            }
        }

        for (participant in participants){
            if(participant.teamId == 100){
                participant.isCarry = participant.carryScore == bestCarryScore1
            }else{
                participant.isCarry = participant.carryScore == bestCarryScore2
            }
        }
    }

    private fun computeCarryScore(stats: ParticipantStats): Double {
        var r = 0.0

        r += stats.totalDamageDealtToChampions % 1000
        r += stats.wardsPlaced * 0.2
        r += stats.turretKills
        r += stats.kills
        r += stats.assists
        r -= stats.deaths

        return r
    }

    private fun readTheTeams(listOne : MutableList<Participant>, listTwo : MutableList<Participant>, participants: List<Participant>){
        for(participant in participants){
            if(participant.teamId == 100){
                listOne.add(participant)
            }else{
                listTwo.add(participant)
            }
        }
    }
}
