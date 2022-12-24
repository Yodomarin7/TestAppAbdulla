package com.example.testappabdulla.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CardRepository
import com.example.data.source.room.CardEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchCardUiState (
    val data: List<CardEntity>
)
sealed class SearchCardUiEvent {
    class Create(val card: CardEntity): SearchCardUiEvent()
    class Delete(val card: CardEntity): SearchCardUiEvent()
}

class SearchCardViewModel(
    private val cardRepository: CardRepository
): ViewModel() {
    private val _uiState = MutableStateFlow( SearchCardUiState(data = listOf()) )
    val uiState: StateFlow<SearchCardUiState> = _uiState

    init {
        viewModelScope.launch {
            cardRepository.getAllFromDao().collect { cardList ->
                _uiState.value = _uiState.value.copy(data = cardList)
            }
        }
    }

    fun reduce(event: SearchCardUiEvent) {
        when(event) {
            is SearchCardUiEvent.Create -> { create(event.card) }
            is SearchCardUiEvent.Delete -> { delete(event.card) }
        }
    }

    private fun create(card: CardEntity) {
        viewModelScope.launch {
            cardRepository.insertDao(card)
        }
    }

    private fun delete(card: CardEntity) {
        viewModelScope.launch {
            cardRepository.deleteDao(card)
        }
    }
}

class SearchCardViewModelFactory(
    private val cardRepository: CardRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchCardViewModel::class.java)) {
            return SearchCardViewModel(cardRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}














