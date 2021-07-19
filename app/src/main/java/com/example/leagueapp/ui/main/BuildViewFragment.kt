package com.example.leagueapp.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.leagueapp.R
import com.example.leagueapp.adapters.TipsAdapter
import com.example.leagueapp.databinding.FragmentChampBuildBinding
import com.example.leagueapp.models.ChampionData
import com.example.leagueapp.networking.model.models.Match
import com.example.leagueapp.repository.ReadJsonRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class BuildViewFragment : Fragment(){
    private var _binding: FragmentChampBuildBinding? = null
    private val binding get() = _binding!!
    val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChampBuildBinding.inflate(inflater, container, false )

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val champ = arguments?.getSerializable("champ") as ChampionData

        binding.apply {
            toolbarText.setText(champ.id)
            circularImageView.load("https://raw.communitydragon.org/pbe/plugins/rcp-be-lol-game-data/global/default/v1/champion-icons/${champ.key}.png")
            passive.load("https://ddragon.leagueoflegends.com/cdn/${ReadJsonRepository.PATCH}/img/passive/${champ.skillList[0]}")
            imgSkill1.load("https://ddragon.leagueoflegends.com/cdn/${ReadJsonRepository.PATCH}/img/spell/${champ.skillList[1]}")
            imgSkill2.load("https://ddragon.leagueoflegends.com/cdn/${ReadJsonRepository.PATCH}/img/spell/${champ.skillList[2]}")
            imgSkill3.load("https://ddragon.leagueoflegends.com/cdn/${ReadJsonRepository.PATCH}/img/spell/${champ.skillList[3]}")
            imgSkill4.load("https://ddragon.leagueoflegends.com/cdn/${ReadJsonRepository.PATCH}/img/spell/${champ.skillList[4]}")

            champTitle.setText(champ.title)
            champTags.setText(champ.tags)
        }

        database.collection("Data").document("Runes").collection(champ.key).orderBy("counter", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                if(it.documents.size > 0){


                val doc = it.documents[0]
                val data = doc.data

                Timber.d("Reading the doc with counter: ${data?.get("counter") as Long} for champ ${champ.key}")

                val perk0 = data?.get("perk0") as Long
                val perk1 = data.get("perk1") as Long
                val perk2 =data.get("perk2") as Long
                val perk3 =data.get("perk3") as Long
                val perk4 =data.get("perk4") as Long
                val perk5 = data.get("perk5") as Long
                val perkPrimary = data.get("primaryStyle") as Long
                val perkSecondary = data.get("subStyle") as Long

                binding.apply {
                    imgPrimary.load(ReadJsonRepository.getRuneById(perkPrimary.toInt()))
                    imgSecondary.load(ReadJsonRepository.getRuneById(perkSecondary.toInt()))
                    rune0.load(ReadJsonRepository.getRuneById(perk0.toInt()))
                    rune1.load(ReadJsonRepository.getRuneById(perk1.toInt()))
                    rune2.load(ReadJsonRepository.getRuneById(perk2.toInt()))
                    rune3.load(ReadJsonRepository.getRuneById(perk3.toInt()))
                    rune4.load(ReadJsonRepository.getRuneById(perk4.toInt()))
                    rune5.load(ReadJsonRepository.getRuneById(perk5.toInt()))
                }

                }else{
                    binding.runeCV.visibility = View.GONE
                }
                    val proTipsAdapter = TipsAdapter(false)
                    val conTipsAdapter = TipsAdapter(true)

                    binding.apply {
                        proTipsTv.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            hasFixedSize()
                            adapter = proTipsAdapter
                            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                        }

                        conTipsRV.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            hasFixedSize()
                            adapter = conTipsAdapter
                            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                        }
                    }
                    proTipsAdapter.submitList(champ.proTipsList)
                    conTipsAdapter.submitList(champ.conTipsList)


            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}