package com.example.appmarvel.presenter.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.appmarvel.data.marvelresponse.Result
import com.example.appmarvel.databinding.MarvelListItemsBinding


class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            MarvelListItemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = characters.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.binding.apply {
            txtNameCharacter.text = character.name
            if (character.description == "") {
                txtDescriptionCharacter.text = "O personagem não possui descrição"
            } else {
                txtDescriptionCharacter.text = character.description
            }



            imgCharacter.load("${character.thumbnail.path}.${character.thumbnail.extension}") {
                crossfade(true)
            }

        }

    }

    inner class CharacterViewHolder(val binding: MarvelListItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.description == newItem.description &&
                    oldItem.thumbnail.path == newItem.thumbnail.path && oldItem.thumbnail.extension == newItem.thumbnail.extension
        }

    }
    private val differ = AsyncListDiffer(this, differCallBack)
    var characters: List<Result>
        get() = differ.currentList
        set(value) = differ.submitList(value)
}