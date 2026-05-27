package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class NeetRepository(private val db: AppDatabase) {

    private val dao = db.neetDao()

    // --- User Sessions ---
    val activeSession: Flow<UserSessionEntity?> = dao.getActiveSessionFlow()

    suspend fun loginUser(phoneNumber: String, name: String, isPremium: Boolean): UserSessionEntity = withContext(Dispatchers.IO) {
        val existing = dao.getSessionByPhone(phoneNumber)
        val session = if (existing != null) {
            existing.copy(name = name, isPremium = isPremium, lastLoginTimestamp = System.currentTimeMillis())
        } else {
            UserSessionEntity(phoneNumber = phoneNumber, name = name, isPremium = isPremium)
        }
        dao.saveSession(session)
        session
    }

    suspend fun upgradePremium(phoneNumber: String) = withContext(Dispatchers.IO) {
        dao.updatePremiumStatus(phoneNumber, true)
    }

    suspend fun logout() = withContext(Dispatchers.IO) {
        dao.clearSessions()
        dao.clearAttempts()
        dao.clearChatHistory()
    }


    // --- Question Attempts ---
    val attempts: Flow<List<AttemptEntity>> = dao.getAllAttemptsFlow()

    suspend fun saveAttempt(questionId: Int, selectedIndex: Int, isCorrect: Boolean) = withContext(Dispatchers.IO) {
        dao.saveAttempt(AttemptEntity(questionId, selectedIndex, isCorrect))
    }

    suspend fun resetAllAttempts() = withContext(Dispatchers.IO) {
        dao.clearAttempts()
    }


    // --- Chat doubt system ---
    val chatHistory: Flow<List<ChatMessageEntity>> = dao.getChatMessagesFlow()

    suspend fun saveMessage(role: String, text: String, questionId: Int? = null): ChatMessageEntity = withContext(Dispatchers.IO) {
        val message = ChatMessageEntity(role = role, text = text, questionId = questionId)
        dao.saveChatMessage(message)
        message
    }

    suspend fun askAiTutor(userInput: String, activeQuestion: Question?): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "AI Tutor Info:\nGemini API key is missing or not configured in user secrets. Please add GEMINI_API_KEY to the AI Studio Secrets panel."
        }

        // Build context from active question
        val contextPrompt = if (activeQuestion != null) {
            """
            The student has a doubt about a NEET exam question.
            [Question Details]
            Subject: ${activeQuestion.subject}
            Topic: ${activeQuestion.topic}
            Question Text: ${activeQuestion.text}
            Options:
            ${activeQuestion.options.mapIndexed { i, opt -> "${i + 1}. $opt" }.joinToString("\n")}
            Correct Option index: ${activeQuestion.correctOptionIndex + 1}
            Standard Solution: ${activeQuestion.solutionText}
            
            Based on this question, the student asks: "$userInput"
            Provide a deep, friendly explanation, focusing on core physical/chemical/biological principles.
            """.trimIndent()
        } else {
            """
            The student is preparing for the NEET entrance exam and asks: "$userInput"
            Provide an elegant, step-by-step scientific explanation. Use crisp formatting, bold headers, and motivating language.
            """.trimIndent()
        }

        // Load conversation history for full-context chat
        val previousChats = dao.getChatMessagesFlow().firstOrNull() ?: emptyList()
        val geminiContents = mutableListOf<Content>()
        
        // Add chat history to feed conversation state
        previousChats.forEach { chat ->
            geminiContents.add(
                Content(parts = listOf(Part(text = chat.text)))
            )
        }

        // Add current doubt request
        geminiContents.add(
            Content(parts = listOf(Part(text = contextPrompt)))
        )

        val systemInstruction = Content(
            parts = listOf(
                Part(
                    text = "You are an elite, patient, encouraging NEET AI Doubts Tutor. " +
                           "Your goal is to help students crack toughest medical questions and clear concepts. " +
                           "Always format formulas cleanly and explain derivations step-by-step."
                )
            )
        )

        try {
            val request = GenerateContentRequest(
                contents = geminiContents,
                systemInstruction = systemInstruction
            )
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "I was unable to formulate an answer. Could you please rephrase your doubt?"
        } catch (e: Exception) {
            Log.e("NeetRepository", "Error asking AI Tutor", e)
            "Doubt Tutor encountered an error: ${e.localizedMessage ?: e.message ?: "Unknown networking failure"}. Please check your connection."
        }
    }

    suspend fun clearChats() = withContext(Dispatchers.IO) {
        dao.clearChatHistory()
    }
}
