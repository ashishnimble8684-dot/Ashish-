package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.NeetViewModel
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.InteractiveDoubtChatScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.theme.MyApplicationTheme

enum class NeetAppScreen {
    LOGIN,
    DASHBOARD,
    CHAT
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    
    // Global Exception Logging
    val originalHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
      Log.e("MainActivityCrash", "Uncaught runtime crash", throwable)
      originalHandler?.uncaughtException(thread, throwable)
    }

    setContent {
      MyApplicationTheme {
        val viewModel: NeetViewModel = viewModel()
        val userSession by viewModel.activeSession.collectAsState()
        
        var currentLocalScreen by remember { mutableStateOf(NeetAppScreen.DASHBOARD) }

        // State-driven routing engine matching active user session
        val currentScreen = when {
            userSession == null -> NeetAppScreen.LOGIN
            currentLocalScreen == NeetAppScreen.LOGIN -> NeetAppScreen.DASHBOARD
            else -> currentLocalScreen
        }

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          when (currentScreen) {
              NeetAppScreen.LOGIN -> {
                  LoginScreen(
                      viewModel = viewModel,
                      modifier = Modifier.padding(innerPadding)
                  )
              }
              NeetAppScreen.DASHBOARD -> {
                  userSession?.let { session ->
                      DashboardScreen(
                          viewModel = viewModel,
                          userSession = session,
                          onNavigateToChat = { currentLocalScreen = NeetAppScreen.CHAT },
                          modifier = Modifier.padding(innerPadding)
                      )
                  }
              }
              NeetAppScreen.CHAT -> {
                  InteractiveDoubtChatScreen(
                      viewModel = viewModel,
                      onNavigateBack = { currentLocalScreen = NeetAppScreen.DASHBOARD },
                      modifier = Modifier.padding(innerPadding)
                  )
              }
          }
        }
      }
    }
  }
}
