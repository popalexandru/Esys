package com.example.leagueapp.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.leagueapp.models.ChampionData
import com.example.leagueapp.models.News
import com.example.leagueapp.models.Rune
import com.example.leagueapp.models.Spell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

object ReadJsonRepository{
    val PATCH = "11.14.1"
    val championFullURL = "https://ddragon.leagueoflegends.com/cdn/${PATCH}/data/en_US/championFull.json"
    val summonerFullURL = "https://ddragon.leagueoflegends.com/cdn/${PATCH}/data/en_US/summoner.json"
    val runesFullURL = "https://ddragon.leagueoflegends.com/cdn/${PATCH}/data/en_US/runesReforged.json"
    val spellsFullURL = "https://ddragon.leagueoflegends.com/cdn/${PATCH}/img/sprite/spell0.png"
    val newsURL = "https://lolstatic-a.akamaihd.net/frontpage/apps/prod/harbinger-l10-website/en-us/production/en-us/page-data/news/game-updates/page-data.json"

    var jsonsRead = false
    var championsData : ArrayList<ChampionData> = ArrayList<ChampionData>()
    var runesData : ArrayList<Rune> = ArrayList()
    var spellsData : ArrayList<Spell> = ArrayList()
    var news : ArrayList<News> = ArrayList()

    init {
        CoroutineScope(IO).launch {
            val apiResponse = URL(spellsFullURL).readText()
        }

        CoroutineScope(IO).launch {
            val apiResponse = URL(championFullURL).readText()

            val fullJSON = JSONObject(apiResponse)
            val dataField  = fullJSON.getJSONObject("data")

            val dataIterator : Iterator<String> = dataField.keys()

            while(dataIterator.hasNext()){
                /* get champ object */
                val currentChamp = dataField.getJSONObject(dataIterator.next())

                val tags = currentChamp.getJSONArray("tags")
                val spells = currentChamp.getJSONArray("spells")
                val passive = currentChamp.getJSONObject("passive")
                val protips = currentChamp.getJSONArray("allytips")
                val contips = currentChamp.getJSONArray("enemytips")

                var tagsString : String = ""

                for(index in 0..(tags.length()- 1)){
                    tagsString += tags.getString(index) + " "
                } // concatenating the tags of the champ

                championsData.add(ChampionData(
                    currentChamp.getString("key"),
                    currentChamp.getString("id"),
                    currentChamp.getString("title"),
                    tagsString,
                    getSillsFromJSON(spells, passive),
                    null,
                    null,
                    null,
                    getListFromArray(protips),
                    getListFromArray(contips)))
            }
            jsonsRead = true
        }

        CoroutineScope(IO).launch {
            val apiResponse = URL(runesFullURL).readText()
            val fullJSON = JSONArray(apiResponse)

            for(runeObject in 0..fullJSON.length() - 1){
                val runeIndex = fullJSON.getJSONObject(runeObject)

                val id : Int = runeIndex.getString("id").toInt()
                val name = runeIndex.getString("name")
                var downloadPath = "https://ddragon.leagueoflegends.com/cdn/img/" + runeIndex.getString("icon")

                var currentRune = Rune(id, name, downloadPath)

                runesData.add(currentRune)

                val slotsArray = runeIndex.getJSONArray("slots")

                for(index in 0..slotsArray.length() - 1){
                    val slotIndex = slotsArray.getJSONObject(index)
                    val runesArray = slotIndex.getJSONArray("runes")

                    for(index in 0..runesArray.length() - 1){
                        val runeIndexSub = runesArray.getJSONObject(index)

                        val IDSub = runeIndexSub.getInt("id")
                        val nameSub = runeIndexSub.getString("name")
                        val url = "https://ddragon.leagueoflegends.com/cdn/img/" + runeIndexSub.getString("icon")

                        var currentRune = Rune()
                        currentRune.id = IDSub
                        currentRune.name = nameSub
                        currentRune.downloadPath = url

                        runesData.add(currentRune)
                    }
                }
            }
        }

/*        CoroutineScope(IO).launch {
            val apiResponse = URL(summonerFullURL).readText()

       }*/

        CoroutineScope(IO).launch {

            val apiResponse = URL(newsURL).readText()
            val fullNewsJSON = JSONObject(apiResponse)
            val articles = fullNewsJSON.getJSONObject("result").getJSONObject("pageContext").getJSONObject("data")
                .getJSONArray("sections").getJSONObject(0).getJSONObject("props").getJSONArray("articles")


            for(index in 0..15){
                val new = articles.getJSONObject(index)
                //var newObject = News()

                val url = new.getString("imageUrl")
                val date =  new.getString("date")
                val link = new.getJSONObject("link").getString("url")
                val title = new.getString("title")

                val newObject = News(link, title, date, url)

                news.add(newObject)

            }

            val obj = news.get(0)
        }
    }



    fun getChampNameById(id : String) : String{
        var name : String = "N/A"

        for(champ in championsData){
            if(champ.key == id){
                return champ.id
            }
        }

        return name
    }

    private fun getSillsFromJSON(spells: JSONArray, passive: JSONObject): MutableList<String> {
        var skillsName : MutableList<String> = mutableListOf()

        var passiveImage = passive.getJSONObject("image")
        skillsName.add(passiveImage.getString("full"))

        for(index in 0..(spells.length()-1)){
            var currentIndex = spells.getJSONObject(index)
            var imageObj = currentIndex.getJSONObject("image")

            skillsName.add(imageObj.getString("full"))
        }

        return skillsName
    }

    private fun getListFromArray(array: JSONArray): List<String>{
        val returnList = mutableListOf<String>()

        for(index in 0..(array.length()-1)){
            val currentTip = array.get(index) as String
            returnList.add(currentTip)

            Timber.d("tiptip $currentTip")
        }

        return returnList
    }

    fun getRuneById(id : Int) : String{
        for(rune in runesData){
            if(rune.id == id)
                return rune.downloadPath
        }

        return ""
    }

    fun getRuneNameById(id: Int): String{
        for(rune in runesData){
            if(rune.id == id)
                return rune.name
        }
        return ""
    }

    fun getSpellByKey(key : String) : Spell{
        val spell = Spell()

        for(spello in spellsData){
            if(spello.key.equals(key))
                return spello
        }
        return spell
    }
}