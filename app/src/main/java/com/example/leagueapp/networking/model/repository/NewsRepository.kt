package com.example.leagueapp.networking.model.repository

import android.util.Log
import com.example.leagueapp.database.FavoriteDAO
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.database.SearchDAO
import com.example.leagueapp.database.SearchItem
import com.example.leagueapp.networking.model.Utils.Platform
import com.example.leagueapp.networking.model.api.RetrofitInstance
import com.example.leagueapp.networking.model.models.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import timber.log.Timber
import java.net.URL
import javax.inject.Inject
import kotlin.system.measureTimeMillis

object NewsRepository {
        val newsUrl = "https://lolstatic-a.akamaihd.net/frontpage/apps/prod/harbinger-l10-website/en-us/production/en-us/page-data/news/game-updates/page-data.json"
        val news = mutableListOf<News>()
        var picurl = ""

        fun readNews() = CoroutineScope(Dispatchers.IO).launch {
            val duration = measureTimeMillis {
                val response = JSONObject(URL(newsUrl).readText())
                val articles = response.getJSONObject("result")
                        .getJSONObject("pageContext")
                        .getJSONObject("data")
                        .getJSONArray("sections")
                        .getJSONObject(0)
                        .getJSONObject("props")
                        .getJSONArray("articles")

                picurl = response.getJSONObject("result")
                        .getJSONObject("pageContext")
                        .getJSONObject("data")
                        .getJSONObject("image").getString("url")

                for(newsIndex in 0..80)
                {
                    val newItem = articles.getJSONObject(newsIndex)
                    val termIds = newItem.getJSONArray("termIds")

                    val termIdsList = mutableListOf<String>()

                    for(terms in 0 until termIds.length()){
                        termIdsList.add(termIds.getString(terms))
                    }

                    news.add(News(
                            newItem.getJSONObject("link").getString("url"),
                            newItem.getString("title"),
                            newItem.getString("date"),
                            newItem.getString("imageUrl"),
                            termIdsList)

                    )

                    Timber.d("Adding ${newItem.getString("title")}")
                }
            }
            Timber.d("Duration of news reading $duration")
        }
}