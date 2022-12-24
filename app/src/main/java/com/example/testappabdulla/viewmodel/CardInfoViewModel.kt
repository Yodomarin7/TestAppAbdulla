package com.example.testappabdulla.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.repository.CardRepository
import com.example.data.source.retrofit.CardRetrofit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CardInfoUiState(
    val showProgress: Boolean,
    val error: String,
    val data: CardRetrofit.CardInfoModel?,
)
sealed class CardInfoUiEvent {
    class Get(val card: String): CardInfoUiEvent()
}

class CardInfoViewModel(
    private val cardRepository: CardRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(
        CardInfoUiState(showProgress = true, error = "", data = null)
    )
    val uiState: StateFlow<CardInfoUiState> = _uiState

    fun reduce(event: CardInfoUiEvent) {
        when(event) {
            is CardInfoUiEvent.Get -> { getCardInfo(event.card) }
        }
    }

    private fun getCardInfo(card: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(showProgress = true, error = "")

            when(val result = cardRepository.getCardInfo(card)) {
                is CardRetrofit.CardInfoResponse.Success -> {
                    _uiState.value = _uiState.value.copy(showProgress = false, error = "", data = result.data)
                }
                is CardRetrofit.CardInfoResponse.Fail -> {
                    _uiState.value = _uiState.value.copy(showProgress = false, error = result.msg)
                }
            }

        }
    }
}

class CardInfoViewModelFactory(
    private val cardRepository: CardRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CardInfoViewModel::class.java)) {
            return CardInfoViewModel(cardRepository) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}