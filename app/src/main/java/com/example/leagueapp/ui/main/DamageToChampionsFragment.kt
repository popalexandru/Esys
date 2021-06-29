package com.example.leagueapp.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.leagueapp.R
import com.example.leagueapp.adapters.GraphViewAdapter
import com.example.leagueapp.adapters.MatchViewerAdapter
import com.example.leagueapp.databinding.DamageDealtFragmentBinding
import com.example.leagueapp.databinding.LayoutGameViewerBinding
import com.example.leagueapp.databinding.LayoutGraphItemBinding
import com.example.leagueapp.networking.model.Utils.Utility
import com.example.leagueapp.networking.model.models.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DamageToChampionsFragment(private val match: Match, private val name : String) : Fragment(), MatchViewerAdapter.onClicked {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    private var _binding : DamageDealtFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var receivedSummoner: Summoner
    private var summonerExists : Boolean = false

    private val globalViewModel : GlobalViewModel by viewModels()
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DamageDealtFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*        val match = arguments?.getSerializable("match") as Match
        val name = arguments?.getString("name")
        //val region = arguments?.getString("region")*/

        val teamOne = match.participants.subList(0, 5)
        val teamOneIdentity = match.participantIdentities.subList(0, 5)
        val teamTwo = match.participants.subList(5, 10)
        val teamTwoIdentity = match.participantIdentities.subList(5, 10)


        var myTeam = 0

        for(participant in teamOneIdentity){
            if(participant.player.summonerName.equals(name)){
                myTeam = 100
            }
        }

        if(myTeam != 100) myTeam = 200

        val participants = match.participants.sortedWith(compareBy{it.stats.totalDamageDealtToChampions})

        var id = 0;

        for(particit in match.participantIdentities){
            if(particit.player.summonerName.equals(name)){
                id = particit.participantId
            }
        }

        populateRecyclerViews(participants.reversed(), myTeam, id)

        //loadBannedChamps(match)

        //setTimeAndDate(match)

        //setTeamStatistics(teamOne, teamTwo, match)
    }

    private fun setTeamStatistics(
        teamOne: MutableList<PlayerObject>,
        teamTwo: MutableList<PlayerObject>,
        match: Match
    ) = binding.apply {
        var visionTeamOne = 0L
        var visionTeamTwo = 0L

        for(player in teamOne){
            visionTeamOne += player.participant.stats.visionScore
        }

        for(player in teamTwo){
            visionTeamTwo += player.participant.stats.visionScore
        }


    }
    private fun champLink(champId : Int) : String {
        return "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champId}.png"
    }

    private fun populateRecyclerViews(teamOne: List<Participant>, teamTwo: Int, myId : Int) {
        val key = HashMap<List<Participant>, Int>()

        val adapterOne = GraphViewAdapter(myId)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = adapterOne
        }

        adapterOne.submitList(teamOne)
        adapterOne.maxValue = teamOne[0].stats.totalDamageDealtToChampions
        adapterOne.myTeam = teamTwo
    }

    private fun getParticipantIdentity(participantId: Int, participantIdentities: List<ParticipantIdentity>): ParticipantIdentity {
        var participantIdentity = ParticipantIdentity()

        for(identity in participantIdentities){
            if(identity.participantId == participantId){
                participantIdentity = identity
            }
        }

        return participantIdentity
    }

    override fun onSummonerClicked(item: PlayerObject) {
        //findNavController().navigate(R.id.action_damageToChampionsFragment_to_jungleTimerFragment)
    }
}