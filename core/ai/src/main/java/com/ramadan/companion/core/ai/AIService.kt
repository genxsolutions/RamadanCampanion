package com.ramadan.companion.core.ai

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Retrofit interface for AI completion API.
 * API key passed via header from config; never in UI.
 */
interface AIService {

    @POST("v1/chat/completions")
    @Headers("Content-Type: application/json")
    suspend fun getCompletion(
        @Header("Authorization") authorization: String,
        @Body body: AiRequest
    ): AiResponse
}

data class AiRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<AiMessage>,
    val max_tokens: Int = 256
)

data class AiMessage(
    val role: String,
    val content: String
)

data class AiResponse(
    val choices: List<AiChoice>?
)

data class AiChoice(
    val message: AiMessage?
)
