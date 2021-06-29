package com.example.leagueapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.leagueapp.databinding.NewsitemBinding
import com.example.leagueapp.networking.model.models.News

class NewsAdapter(private val listener : onDeleteClick) : ListAdapter<News, NewsAdapter.CakeViewHolder>(
    DiffCallback()
){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        val binding = NewsitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CakeViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val currentCake = getItem(position)

        holder.layout.setOnClickListener {
            if(position != RecyclerView.NO_POSITION){
                val cake = getItem(position)
                listener.onNewsClicked(cake)
            }
        }

        holder.bind(currentCake)
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

    class DiffCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: News, newItem: News) =
            oldItem == newItem
    }

    interface onDeleteClick{
        fun onNewsClicked(item : News)
    }

}