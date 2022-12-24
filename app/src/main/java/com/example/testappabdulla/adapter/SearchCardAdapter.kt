package com.example.testappabdulla.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.data.source.room.CardEntity
import com.example.testappabdulla.R
import com.example.testappabdulla.screen.SearchCardFragmentDirections
import com.example.testappabdulla.viewmodel.SearchCardUiEvent
import com.example.testappabdulla.viewmodel.SearchCardViewModel

interface ForItemTouch {
    fun onSwiped(position: Int)
}

class SearchCardAdapter(
    private val navController: NavController,
    private val searchCardViewModel: SearchCardViewModel
): ListAdapter<CardEntity, SearchCardAdapter.ViewHolder>(Comparator()), ForItemTouch  {

    class Comparator: DiffUtil.ItemCallback<CardEntity>() {
        override fun areItemsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: CardEntity, newItem: CardEntity): Boolean {
            return oldItem.card == newItem.card
        }
    }

    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val txtNumber: TextView = item.findViewById(R.id.mtrl_list_item_text)

        init {
            item.setOnClickListener {
                val item = getItem(adapterPosition)
                val action = SearchCardFragmentDirections.actionSearchCardFragmentToCardInfoFragment(item.card)
                navController.navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_card_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.txtNumber.text = item.card
    }

    override fun onSwiped(position: Int) {
        val item = getItem(position)
        searchCardViewModel.reduce(SearchCardUiEvent.Delete(CardEntity(item.uid, item.card)))
    }
}