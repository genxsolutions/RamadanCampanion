package com.ramadan.companion.domain.companion.model

/**
 * Domain model for AI companion chat.
 * Ready for future streaming / backend.
 */
data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long
)
