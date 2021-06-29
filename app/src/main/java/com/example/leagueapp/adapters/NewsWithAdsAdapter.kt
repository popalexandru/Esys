package com.example.leagueapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.R
import com.example.leagueapp.database.FavoriteItem
import com.example.leagueapp.databinding.*
import com.example.leagueapp.models.PlayedChampModel
import com.example.leagueapp.networking.model.models.ChampionMastery
import com.example.leagueapp.networking.model.models.News
import com.example.leagueapp.repository.ReadJsonRepository
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class
NewsWithAdsAdapter(private val listener : onDeleteClick) : ListAdapter<News, RecyclerView.ViewHolder>(
    DiffCallback()
){
    private val ADItem = 1
    private val Item = 2
    lateinit var context : Context

    override fun getItemViewType(position: Int): Int {
        return if (position % 5 == 0) {
            Item
        } else {
            ADItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ADItem){
            val binding = AdUnifiedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AdViewHolder(binding)
        } else {
            val binding = NewsitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CakeViewHolder(binding, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is AdViewHolder -> {
                val adHolder : AdViewHolder = holder as AdViewHolder
                val builder = AdLoader.Builder(context, "ca-app-pub-4479200586800321/6961703752")
                builder.forNativeAd {
                    adHolder
                }
            }
            is CakeViewHolder -> {
                val currentCake = getItem(position)

                holder.layout.setOnClickListener {
                    if(position != RecyclerView.NO_POSITION){
                        val cake = getItem(position)
                        listener.onSummonerClicked(cake)
                    }
                }
            }
        }
        //holder.bind(currentCake)
    }

    inner class CakeViewHolder(private val binding : NewsitemBinding, private val listener : onDeleteClick) : RecyclerView.ViewHolder(binding.root){
        val layout = binding.cardView

        fun bind(champ : News){
            binding.apply {
                title.setText(champ.title)
                img.load(champ.imageUrl)
            }
        }
    }

    inner class AdViewHolder(private val binding : AdUnifiedBinding) : RecyclerView.ViewHolder(binding.root){

    }

    class DiffCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: News, newItem: News) =
            oldItem == newItem
    }

    interface onDeleteClick{
        fun onSummonerClicked(item : News)
    }

    private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
        // Set the media view.
        adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)

        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        //adView.mediaView.setMediaContent(nativeAd.mediaContent)

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView.visibility = View.INVISIBLE
        } else {
            adView.bodyView.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView.visibility = View.INVISIBLE
        } else {
            adView.callToActionView.visibility = View.VISIBLE
            (adView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            adView.iconView.visibility = View.GONE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon.drawable
            )
            adView.iconView.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd)
    }
}