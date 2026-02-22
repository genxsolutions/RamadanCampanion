package com.ramadan.companion.feature.companion.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramadan.companion.domain.companion.AiCompanionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class CompanionUiState(
    val messages: List<ChatMessageUi> = emptyList(),
    val inputText: String = "",
    val isLoading: Boolean = false
)

data class ChatMessageUi(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val time: String
)

@HiltViewModel
class CompanionViewModel @Inject constructor(
    private val repository: AiCompanionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CompanionUiState())
    val state: StateFlow<CompanionUiState> = _state.asStateFlow()

    init {
        _state.update {
            it.copy(
                messages = listOf(
                    ChatMessageUi(
                        id = "welcome",
                        text = "It's okay if today was busy. Let's rebuild your day together.",
                        isFromUser = false,
                        time = nowTime()
                    ),
                    ChatMessageUi(
                        id = "user1",
                        text = "I missed Dhuhr prayer because of work",
                        isFromUser = true,
                        time = nowTime()
                    )
                )
            )
        }
    }

    fun updateInput(text: String) {
        _state.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val currentInput = _state.value.inputText.trim()
        if (currentInput.isEmpty() || _state.value.isLoading) return

        val userMessage = ChatMessageUi(
            id = "user_${System.currentTimeMillis()}",
            text = currentInput,
            isFromUser = true,
            time = nowTime()
        )
        _state.update {
            it.copy(
                messages = it.messages + userMessage,
                inputText = "",
                isLoading = true
            )
        }

        viewModelScope.launch {
            val messagesBeforeThis = _state.value.messages.dropLast(1) // exclude the user message we just added
            val history = messagesBeforeThis
                .filter { it.text.isNotBlank() }
                .fold(emptyList<Pair<String, String>>() to null as String?) { (pairs, pending), msg ->
                    if (msg.isFromUser) {
                        pairs to msg.text
                    } else {
                        val user = pending
                        if (user != null) (pairs + (user to msg.text)) to null
                        else pairs to null
                    }
                }.first
            val reply = repository.sendMessage(currentInput, history)
            val assistantMessage = ChatMessageUi(
                id = "ai_${System.currentTimeMillis()}",
                text = reply,
                isFromUser = false,
                time = nowTime()
            )
            _state.update {
                it.copy(
                    messages = it.messages + assistantMessage,
                    isLoading = false
                )
            }
        }
    }

    private fun nowTime(): String =
        SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
}
