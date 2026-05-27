package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.ChatMessageEntity
import com.example.data.Question
import com.example.data.UserSessionEntity
import com.example.ui.NeetViewModel

// --- COMMON STYLING CONSTANTS FOR PREMIUM THEME ---
val GradientDeepPurple = Brush.verticalGradient(
    colors = listOf(Color(0xFF2C1E4E), Color(0xFF140D26))
)
val GradientGold = Brush.horizontalGradient(
    colors = listOf(Color(0xFFFFB300), Color(0xFFFF8F00))
)
val GoldColor = Color(0xFFFFB300)
val DarkCharcoal = Color(0xFF1C1C24)
val SurfaceCardBg = Color(0xFF232331)

// ==========================================
// 1. LOGIN SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: NeetViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(GradientDeepPurple)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Branding Title
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "App Logo",
                tint = GoldColor,
                modifier = Modifier
                    .size(80.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                text = "NEET AI Pro",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Crack India's Toughest Medical Exam with real-time AI guidance",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCardBg),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (!viewModel.isOtpSent) "Login / Sign Up" else "Enter OTP Code",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Mobile Field
                    OutlinedTextField(
                        value = viewModel.phoneInput,
                        onValueChange = { 
                            if (it.length <= 10 && it.all { c -> c.isDigit() }) {
                                viewModel.phoneInput = it 
                            }
                        },
                        label = { Text("Mobile Number", color = Color.White.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = GoldColor) },
                        prefix = { Text("+91 ", color = Color.White) },
                        placeholder = { Text("98765 43210") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = !viewModel.isOtpSent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    // OTP Field (Shows after send-otp clicked)
                    AnimatedVisibility(visible = viewModel.isOtpSent) {
                        OutlinedTextField(
                            value = viewModel.otpInput,
                            onValueChange = { viewModel.otpInput = it },
                            label = { Text("One-Time Password (OTP)", color = Color.White.copy(alpha = 0.6f)) },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GoldColor) },
                            placeholder = { Text("123456 (Mock)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        )
                    }

                    // Validation Error Text
                    viewModel.errorText?.let { err ->
                        Text(
                            text = err,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // Submit Button
                    Button(
                        onClick = {
                            if (!viewModel.isOtpSent) {
                                viewModel.sendOtp()
                            } else {
                                viewModel.verifyOtpAndLogin()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag(if (viewModel.isOtpSent) "verify_otp_button" else "send_otp_button")
                    ) {
                        if (viewModel.isAuthLoading) {
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = if (!viewModel.isOtpSent) "Send OTP" else "Verify & Login",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // Demo Accounts Fast Bypass Section
            Text(
                text = "EVALUATION PANEL (FAST BYPASS)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Free Bypass
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A223E)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.quickLogin(isPremium = false) }
                        .testTag("demo_free_login")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Guest Student", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Standard Free Plan", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }

                // Premium Bypass
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF38233C)),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.quickLogin(isPremium = true) }
                        .testTag("demo_premium_login")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("👑 Elite User", color = GoldColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Unlocks AI Solutions", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}


// ==========================================
// 2. DASHBOARD SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: NeetViewModel,
    userSession: UserSessionEntity,
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val attempts by viewModel.attempts.collectAsState()
    
    // Sync active attempts whenever list changes
    LaunchedEffect(attempts) {
        viewModel.syncWithAttempts(attempts)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Welcome, ${userSession.name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (userSession.isPremium) {
                                    Icon(Icons.Default.Star, "Premium", tint = GoldColor, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("ELITE PREMIUM MEMBER", fontSize = 10.sp, color = GoldColor, fontWeight = FontWeight.SemiBold)
                                } else {
                                    Text("Free Acccount Support", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f))
                                }
                            }
                        }
                        
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(Icons.Default.Refresh, "Logout", tint = Color.LightGray)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF20153D),
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (userSession.isPremium) {
                        onNavigateToChat()
                    } else {
                        viewModel.showUpgradeDialog = true
                    }
                },
                containerColor = GoldColor,
                contentColor = Color.Black
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Ask ChatGPT")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ask AI Tutor", fontWeight = FontWeight.Bold)
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF14141E))
        ) {
            // Subject Tab row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF20153D))
                        .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Physics", "Chemistry", "Biology").forEach { sub ->
                        val isSel = viewModel.selectedSubject == sub
                        val tintColor = if (isSel) GoldColor else Color.White.copy(alpha = 0.5f)
                        val barColor = if (isSel) GoldColor else Color.Transparent

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable { viewModel.onSubjectChanged(sub) }
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = sub,
                                color = tintColor,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(3.dp)
                                    .background(barColor, CircleShape)
                            )
                        }
                    }
                }
            }

            val subjectQuestions = viewModel.getFilteredQuestions()
            val activeQuestion = viewModel.getActiveQuestion()

            if (activeQuestion == null) {
                item {
                    Text(
                        "No questions seeded in this topic yet.",
                        color = Color.White,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                // Horizontal Question Index row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            enabled = viewModel.selectedQuestionIndex > 0,
                            onClick = { viewModel.onQuestionChanged(viewModel.selectedQuestionIndex - 1) }
                        ) {
                            Icon(Icons.Default.ArrowBack, "Prev", tint = if (viewModel.selectedQuestionIndex > 0) Color.White else Color.Gray)
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            subjectQuestions.forEachIndexed { i, q ->
                                val isCur = viewModel.selectedQuestionIndex == i
                                val isAttempted = attempts.find { it.questionId == q.id }
                                val circColor = when {
                                    isCur -> GoldColor
                                    isAttempted != null -> {
                                        if (isAttempted.isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    }
                                    else -> Color.White.copy(alpha = 0.2f)
                                }
                                val textColor = if (isCur) Color.Black else Color.White

                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .size(32.dp)
                                        .background(circColor, CircleShape)
                                        .clickable { viewModel.onQuestionChanged(i) }
                                ) {
                                    Text(
                                        text = "${i + 1}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                }
                            }
                        }

                        IconButton(
                            enabled = viewModel.selectedQuestionIndex < subjectQuestions.size - 1,
                            onClick = { viewModel.onQuestionChanged(viewModel.selectedQuestionIndex + 1) }
                        ) {
                            Icon(Icons.Default.ArrowForward, "Next", tint = if (viewModel.selectedQuestionIndex < subjectQuestions.size - 1) Color.White else Color.Gray)
                        }
                    }
                }

                // Practice Question content
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCardBg),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Badge indicating topic
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier
                                        .background(Color(0xFF2E2E3A), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = activeQuestion.topic,
                                        color = GoldColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Difficulty: Hard",
                                    color = Color(0xFFFF5252),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Question Title
                            Text(
                                text = "Q: ${activeQuestion.text}",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // MCQ list card options
                            activeQuestion.options.forEachIndexed { optIdx, optionText ->
                                val isSelected = viewModel.selectedOptionIndex == optIdx
                                val isCorrectOption = optIdx == activeQuestion.correctOptionIndex
                                val isSubmitted = viewModel.hasSubmittedAnswer

                                val bgOptionColor = when {
                                    isSubmitted && isCorrectOption -> Color(0xFF1B5E20) // Right answer glowing green
                                    isSubmitted && isSelected && !isCorrectOption -> Color(0xFF450D0D) // Wrong selected highlighted dark red
                                    isSelected -> Color(0xFF2C243E)
                                    else -> Color(0xFF1E1E2A)
                                }

                                val borderStroke = when {
                                    isSubmitted && isCorrectOption -> Modifier.border(1.5.dp, Color(0xFF4CAF50), RoundedCornerShape(10.dp))
                                    isSubmitted && isSelected && !isCorrectOption -> Modifier.border(1.5.dp, Color(0xFFF44336), RoundedCornerShape(10.dp))
                                    isSelected -> Modifier.border(1.5.dp, GoldColor, RoundedCornerShape(10.dp))
                                    else -> Modifier
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bgOptionColor)
                                        .then(borderStroke)
                                        .clickable(enabled = !isSubmitted) {
                                            viewModel.selectedOptionIndex = optIdx
                                        }
                                        .padding(12.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { if (!isSubmitted) viewModel.selectedOptionIndex = optIdx },
                                        colors = androidx.compose.material3.RadioButtonDefaults.colors(
                                            selectedColor = GoldColor,
                                            unselectedColor = Color.White.copy(alpha = 0.5f)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = optionText,
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    
                                    if (isSubmitted) {
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (isCorrectOption) {
                                            Icon(Icons.Default.Check, "Correct", tint = Color(0xFF4CAF50))
                                        } else if (isSelected) {
                                            Icon(Icons.Default.Close, "Incorrect", tint = Color(0xFFF44336))
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Submit / Retry actions
                            if (!viewModel.hasSubmittedAnswer) {
                                Button(
                                    onClick = { viewModel.submitAnswer() },
                                    enabled = viewModel.selectedOptionIndex != null,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldColor,
                                        disabledContainerColor = Color.Gray.copy(alpha = 0.4f)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("submit_answer_button")
                                ) {
                                    Text("Submit Answer", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                OutlinedButton(
                                    onClick = { viewModel.resetCurrentAttempt() },
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldColor),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldColor),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                ) {
                                    Text("Re-try this Question", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                // AI step-by-step gate answers card
                item {
                    if (viewModel.hasSubmittedAnswer) {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInVertically()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (userSession.isPremium) {
                                    // PREMIUM SOLUTION CARD
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D2F)),
                                        shape = RoundedCornerShape(14.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00B0FF)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, "Correct Answer", tint = Color(0xFF00E676))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "AI Verified Correct Option: Option ${activeQuestion.correctOptionIndex + 1}",
                                                    color = Color(0xFF00E676),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Text(
                                                text = "Verified Step-by-Step AI Breakdown:",
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = activeQuestion.solutionText,
                                                color = Color.White.copy(alpha = 0.8f),
                                                fontSize = 13.sp,
                                                lineHeight = 20.sp
                                            )
                                        }
                                    }
                                } else {
                                    // FREE LOCKED GATE CARD
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFF35241D)),
                                        shape = RoundedCornerShape(14.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF7043)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Lock, "Solution locked", tint = Color(0xFFFF7043))
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Step-by-Step AI Solution Available",
                                                    color = Color(0xFFFF7043),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Understand tricky steps, formulae derivations and shortcut calculations verified by NEET Experts with Elite AI Premium plans.",
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center,
                                                lineHeight = 18.sp
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Button(
                                                onClick = { viewModel.showUpgradeDialog = true },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF7043)),
                                                modifier = Modifier.testTag("upgrade_to_premium_button")
                                            ) {
                                                Icon(Icons.Default.Star, null, tint = Color.White)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Unlock AI Breakdown with Premium", fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // spacer bottom list cushion
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Modern Checkout Dialog Sheet
    if (viewModel.showUpgradeDialog) {
        PremiumUpgradeSheet(viewModel = viewModel)
    }
}


// ==========================================
// 3. COHESIVE MODERN CHECKOUT UPGRADE SHEET
// ==========================================
@Composable
fun PremiumUpgradeSheet(viewModel: NeetViewModel) {
    var selectedPlanPrice by remember { mutableStateOf("₹299") }
    var selectedPlanTitle by remember { mutableStateOf("NEET Elite Monthly Pass") }

    Dialog(onDismissRequest = { if (!viewModel.isProcessingPayment) viewModel.showUpgradeDialog = false }) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCardBg),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header brand badge
                Row(
                    modifier = Modifier
                        .background(Color(0xFF38233C), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, "Premium", tint = GoldColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ELITE PREMIUM ROADMAP", color = GoldColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Upgrade to NEET Pro",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Unlock step-by-step AI breakdowns and interactive doubts solver tutors powered by Gemini. Clear all doubts instantly!",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Plan 1: Monthly Pass
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedPlanPrice == "₹299") Color(0xFF2E2442) else Color(0xFF1E1E2A))
                        .border(
                            width = 1.dp,
                            color = if (selectedPlanPrice == "₹299") GoldColor else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            selectedPlanPrice = "₹299"
                            selectedPlanTitle = "NEET Elite Monthly Pass"
                        }
                        .padding(16.dp)
                ) {
                    RadioButton(
                        selected = selectedPlanPrice == "₹299",
                        onClick = {
                            selectedPlanPrice = "₹299"
                            selectedPlanTitle = "NEET Elite Monthly Pass"
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Monthly Elite Pass", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Billed monthly. Cancel anytime.", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                    Text("₹299/mo", color = GoldColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Plan 2: Package bundle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selectedPlanPrice == "₹1,499") Color(0xFF2E2442) else Color(0xFF1E1E2A))
                        .border(
                            width = 1.dp,
                            color = if (selectedPlanPrice == "₹1,499") GoldColor else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            selectedPlanPrice = "₹1,499"
                            selectedPlanTitle = "NEET Cracker Full Bundle"
                        }
                        .padding(16.dp)
                ) {
                    RadioButton(
                        selected = selectedPlanPrice == "₹1,499",
                        onClick = {
                            selectedPlanPrice = "₹1,499"
                            selectedPlanTitle = "NEET Cracker Full Bundle"
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Full Prep Cracker pack", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Full-year access till NEET Exam.", color = Color.White.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                    Text("₹1,499", color = GoldColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Authorization Payment button
                Button(
                    onClick = { viewModel.processUpgradePayment() },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldColor),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !viewModel.isProcessingPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("make_payment_button")
                ) {
                    if (viewModel.isProcessingPayment) {
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contacting Bank Gateway...", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    } else {
                        Text("Authorize & Pay $selectedPlanPrice", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { viewModel.showUpgradeDialog = false },
                    enabled = !viewModel.isProcessingPayment
                ) {
                    Text("Close & Back to Practice", color = Color.White.copy(alpha = 0.6f))
                }
            }
        }
    }
}


// ==========================================
// 4. INTERACTIVE DOUBT CHAT SOLVER TUTOR SCREEN
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveDoubtChatScreen(
    viewModel: NeetViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val activeQuestion = viewModel.getActiveQuestion()
    val chatListState = rememberLazyListState()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, viewModel.isAiThinking) {
        if (messages.isNotEmpty()) {
            chatListState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "NEET AI Doubt Tutor", 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        activeQuestion?.let {
                            Text(
                                text = "Doubts relative to ${it.subject} \u2022 ${it.topic}",
                                fontSize = 10.sp,
                                color = GoldColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearChatHistory() }) {
                        Icon(Icons.Default.Close, "Clear Chats", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF20153D),
                    titleContentColor = Color.White
                )
            )
        },
        modifier = modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFF14141E))
        ) {
            // Optional reference prompt cards for convenient clicking
            val suggestions = remember(activeQuestion) {
                if (activeQuestion?.id == 1) {
                    listOf(
                        "Explain step 3: d(k*r*t)/dt derivative calculation",
                        "Why is centripetal acceleration ac = v^2/r?",
                        "Help me calculate total work done"
                    )
                } else if (activeQuestion?.id == 3) {
                    listOf(
                        "What is Saytzeff's Rule in elimination?",
                        "Why is trans-2-butene more stable than cis-2-butene?",
                        "Define E2 reaction mechanism steps"
                    )
                } else {
                    listOf(
                        "Summary of core principles in this topic",
                        "Show me another solved hard NEET problem",
                        "Explain correct answer derivation in simpler terms"
                    )
                }
            }

            // Message Board LazyList
            LazyColumn(
                state = chatListState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Initial welcoming bot dialogue
                item {
                    ChatBubble(
                        role = "model",
                        text = "Hello! I am your elite NEET Prep AI Doubt Tutor 🩺. " +
                                (activeQuestion?.let { "Let's explore your doubts about the active question on '${it.topic}'. " } ?: "Ask me any scientific questions, formulas or steps!") +
                                "Ask me anything - I am ready to break down equations step-by-step!"
                    )
                }

                items(messages) { msg ->
                    ChatBubble(role = msg.role, text = msg.text)
                }

                if (viewModel.isAiThinking) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(end = 64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E1E2A))
                                .padding(12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = GoldColor,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "AI Tutor is analyzing step-by-step...",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Suggestion shortcuts row
            AnimatedVisibility(visible = messages.isEmpty() && !viewModel.isAiThinking) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Text(
                        "Suggested doubts to Ask AI:",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.take(2).forEach { suggestion ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF252538))
                                    .clickable {
                                        viewModel.chatMessageInput = suggestion
                                        viewModel.sendMessageToAi()
                                    }
                                    .border(0.5.dp, GoldColor.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = suggestion,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    lineHeight = 15.sp,
                                    maxLines = 3
                                )
                            }
                        }
                    }
                }
            }

            // Chat text input controls block
            SurfaceCardSection(
                value = viewModel.chatMessageInput,
                onValueChange = { viewModel.chatMessageInput = it },
                onSendPressed = { viewModel.sendMessageToAi() },
                isAiThinking = viewModel.isAiThinking
            )
        }
    }
}

// Single chat bubble UI component
@Composable
fun ChatBubble(role: String, text: String) {
    val isUser = role == "user"
    val backgroundBubbleColor = if (isUser) Color(0xFF4A148C) else Color(0xFF252535)
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val shape = if (isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp)
    } else {
        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomEnd = 16.dp)
    }
    val leadingPadding = if (isUser) 48.dp else 0.dp
    val trailingPadding = if (isUser) 0.dp else 48.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = leadingPadding, end = trailingPadding),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(shape)
                .background(backgroundBubbleColor)
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (isUser) "Student" else "🩺 NEET AI Tutor",
                    fontSize = 10.sp,
                    color = if (isUser) Color(0xFFE1BEE7) else GoldColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

// Bottom Input Panel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfaceCardSection(
    value: String,
    onValueChange: (String) -> Unit,
    onSendPressed: () -> Unit,
    isAiThinking: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF20153D))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text("Ask doubt: e.g. prove the equation step-by-step...", color = Color.White.copy(alpha = 0.5f), fontSize = 13.sp) },
                singleLine = false,
                maxLines = 4,
                enabled = !isAiThinking,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            )

            IconButton(
                onClick = onSendPressed,
                enabled = value.isNotBlank() && !isAiThinking,
                modifier = Modifier
                    .size(48.dp)
                    .background(if (value.isNotBlank() && !isAiThinking) GoldColor else Color.White.copy(alpha = 0.1f), CircleShape)
                    .testTag("send_doubt_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send doubt",
                    tint = if (value.isNotBlank() && !isAiThinking) Color.Black else Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}
