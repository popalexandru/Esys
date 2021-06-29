package com.example.leagueapp.ui.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leagueapp.adapters.GoldEarnAdapter
import com.example.leagueapp.adapters.MatchViewerAdapter
import com.example.leagueapp.adapters.VisionAdapter
import com.example.leagueapp.databinding.GoldEarnedFragmentBinding
import com.example.leagueapp.databinding.VisionScoreLayoutBinding
import com.example.leagueapp.networking.model.models.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VisionScoreFragment(private val match: Match, private val name : String) : Fragment(), MatchViewerAdapter.onClicked {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true)
    }

    private var _binding : VisionScoreLayoutBinding? = null
    private val binding get() = _binding!!

    private lateinit var receivedSummoner: Summoner
    private var summonerExists : Boolean = false

    private val globalViewModel : GlobalViewModel by viewModels()
    private val viewModel : MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VisionScoreLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teamOneIdentity = match.participantIdentities.subList(0, 5)

        var myTeam = 0

        for(participant in teamOneIdentity){
            if(participant.player.summonerName.equals(name)){
                myTeam = 100
            }
        }

        if(myTeam != 100) myTeam = 200

        val participants = match.participants.sortedWith(compareBy{it.stats.visionScore})

        var myId = 0;
        for(participant in match.participantIdentities){
            if(participant.player.summonerName == name){
                myId = participant.participantId
            }
        }

        populateRecyclerViews(participants.reversed(), myTeam, myId)
    }


    private fun champLink(champId : Int) : String {
        return "https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champId}.png"
    }

    private fun populateRecyclerViews(teamOne: List<Participant>, teamTwo: Int, myId: Int) {
        val adapterOne = VisionAdapter(myId)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            hasFixedSize()
            adapter = adapterOne
        }

        adapterOne.submitList(teamOne)
        adapterOne.maxValue = teamOne[0].stats.visionScore.toLong()
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
        viewModel.getSummoner(item.participantIdentity.player.summonerName, item.region)

        viewModel.uiState.asLiveData().observe(viewLifecycleOwner){ state ->
            when(state){

                is MainViewModel.SummonerUiState.Success -> {
                    val bundle = bundleOf("summoner" to state.summoner)

                    //findNavController().navigate(R.id.action_matchViewFragment_to_summonerFragment, bundle)

                    viewModel.insertSearch(state.summoner.name)
                    viewModel.emptyState()
                }

                is MainViewModel.SummonerUiState.Error -> {
                    //binding.getButton.clearAnimation()
                    //binding.nameInputLayout.error = state.exception
                    Toast.makeText(requireContext(), "Can't do that ${state.exception}", Toast.LENGTH_SHORT).show()
                }

                is MainViewModel.SummonerUiState.Empty -> {
                    //binding.getButton.revertAnimation()
                }

            }
        }
    }
}