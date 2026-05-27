package com.example.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AttemptEntity
import com.example.data.ChatMessageEntity
import com.example.data.Question
import com.example.data.QuestionPool
import com.example.data.NeetRepository
import com.example.data.UserSessionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NeetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = NeetRepository(db)

    // --- Flows from Database ---
    val activeSession: StateFlow<UserSessionEntity?> = repository.activeSession
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val attempts: StateFlow<List<AttemptEntity>> = repository.attempts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    // --- Login UI State ---
    var phoneInput by mutableStateOf("")
    var otpInput by mutableStateOf("")
    var errorText by mutableStateOf<String?>(null)
    var isOtpSent by mutableStateOf(false)
    var isAuthLoading by mutableStateOf(false)


    // --- Dashboard UI State ---
    var selectedSubject by mutableStateOf("Physics") // "Physics", "Chemistry", "Biology"
    var selectedQuestionIndex by mutableStateOf(0)

    // Selected option index for currently visible question (null if none)
    var selectedOptionIndex by mutableStateOf<Int?>(null)

    // Has user clicked submit on the active question?
    var hasSubmittedAnswer by mutableStateOf(false)


    // --- Upgrade and Payment state ---
    var showUpgradeDialog by mutableStateOf(false)
    var isProcessingPayment by mutableStateOf(false)


    // --- Chat state ---
    var chatMessageInput by mutableStateOf("")
    var isAiThinking by mutableStateOf(false)


    // --- HELPERS ---
    
    // Retrieves list of questions filtering by selected subject
    fun getFilteredQuestions(): List<Question> {
        return QuestionPool.questions.filter { it.subject.equals(selectedSubject, ignoreCase = true) }
    }

    // Retrieves current active question
    fun getActiveQuestion(): Question? {
        val flt = getFilteredQuestions()
        return if (selectedQuestionIndex in flt.indices) flt[selectedQuestionIndex] else null
    }

    // Resets answer status when switching question or subject
    fun onSubjectChanged(newSubject: String) {
        selectedSubject = newSubject
        selectedQuestionIndex = 0
        resetAnswerState()
    }

    fun onQuestionChanged(index: Int) {
        selectedQuestionIndex = index
        resetAnswerState()
    }

    private fun resetAnswerState() {
        val activeQ = getActiveQuestion()
        val pastAttempt = attempts.value.find { it.questionId == activeQ?.id }
        if (pastAttempt != null) {
            selectedOptionIndex = pastAttempt.selectedOptionIndex
            hasSubmittedAnswer = true
        } else {
            selectedOptionIndex = null
            hasSubmittedAnswer = false
        }
    }

    // Synchronize local states with Room evaluations on flow emissions
    fun syncWithAttempts(allAttempts: List<AttemptEntity>) {
        val activeQ = getActiveQuestion()
        val pastAttempt = allAttempts.find { it.questionId == activeQ?.id }
        if (pastAttempt != null) {
            selectedOptionIndex = pastAttempt.selectedOptionIndex
            hasSubmittedAnswer = true
        }
    }


    // --- ACTIONS ---

    // 1. Send OTP simulated flow
    fun sendOtp() {
        if (phoneInput.length < 10) {
            errorText = "Please enter a valid 10-digit mobile number"
            return
        }
        isAuthLoading = true
        viewModelScope.launch {
            // Simulated delay for verification service
            kotlinx.coroutines.delay(600)
            isOtpSent = true
            isAuthLoading = false
            errorText = null
        }
    }

    // 2. Verify OTP & Log In (Demo models "8888888888" -> Premium, and other -> Free)
    fun verifyOtpAndLogin() {
        if (otpInput.length < 4) {
            errorText = "Please enter a valid 4-to-6 digit OTP"
            return
        }
        isAuthLoading = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(800)
            
            // Check credentials to determine Premium status
            val isPremiumUser = phoneInput == "8888888888"
            val testName = if (isPremiumUser) "Premium Student" else "NEET Aspirant"
            
            repository.loginUser(
                phoneNumber = phoneInput,
                name = testName,
                isPremium = isPremiumUser
            )
            
            isAuthLoading = false
            errorText = null
        }
    }

    // Quick Login bypass button for evaluation convenience
    fun quickLogin(isPremium: Boolean) {
        isAuthLoading = true
        viewModelScope.launch {
            val phone = if (isPremium) "8888888888" else "9876543210"
            val name = if (isPremium) "Premium Student" else "Guest Student"
            repository.loginUser(phoneNumber = phone, name = name, isPremium = isPremium)
            isAuthLoading = false
        }
    }

    // 3. Submit question practice answer
    fun submitAnswer() {
        val q = getActiveQuestion() ?: return
        val selIndex = selectedOptionIndex ?: return
        
        hasSubmittedAnswer = true
        val isCorrect = selIndex == q.correctOptionIndex
        
        viewModelScope.launch {
            repository.saveAttempt(q.id, selIndex, isCorrect)
        }
    }

    // Reset current question attempt to try again
    fun resetCurrentAttempt() {
        val q = getActiveQuestion() ?: return
        hasSubmittedAnswer = false
        selectedOptionIndex = null
        // We can just keep it in Room, but visually allow redo!
    }

    // 4. Upgrade Premium (Razorpay simulated card)
    fun processUpgradePayment() {
        val user = activeSession.value ?: return
        isProcessingPayment = true
        viewModelScope.launch {
            kotlinx.coroutines.delay(1800) // simulated bank transaction
            repository.upgradePremium(user.phoneNumber)
            isProcessingPayment = false
            showUpgradeDialog = false
        }
    }

    // 5. Doubt Chat System
    fun sendMessageToAi() {
        val input = chatMessageInput.trim()
        if (input.isEmpty()) return
        
        val activeQ = getActiveQuestion()
        chatMessageInput = ""
        isAiThinking = true
        
        viewModelScope.launch {
            // Save User Doubt Message
            repository.saveMessage("user", input, activeQ?.id)
            
            // Invoke Gemini API for response
            val aiReply = repository.askAiTutor(input, activeQ)
            
            // Save Ai Tutor Response
            repository.saveMessage("model", aiReply, activeQ?.id)
            isAiThinking = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChats()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            // Reset UI structures
            phoneInput = ""
            otpInput = ""
            isOtpSent = false
            selectedSubject = "Physics"
            selectedQuestionIndex = 0
            selectedOptionIndex = null
            hasSubmittedAnswer = false
        }
    }
}
