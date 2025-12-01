package com.example.myuniapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myuniapp.viewmodel.AuthUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

var email by remember { mutableStateOf("") }
var password by remember { mutableStateOf("") }
var emailError by remember { mutableStateOf<String?>(null) }
var passError by remember { mutableStateOf<String?>(null) }

fun validate(): Boolean {
    emailError = when {
        email.isBlank() -> "Email can't be empty"
        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email"
        else -> null
    }
    passError = when {
        password.isBlank() -> "Password can't be empty"
        password.length < 6 -> "Min 6 characters"
        else -> null
    }
    return emailError == null && passError == null
}


@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Log in Form",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        isError = emailError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (emailError != null) {
                        Text(emailError!!, color = MaterialTheme.colorScheme.error)
                    }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        isError = passError != null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passError != null) {
                        Text(passError!!, color = MaterialTheme.colorScheme.error)
                    }


                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        if (validate()) {
                            authViewModel.login(email, password)
                        }
                    }) {
                        Text("Login")
                    }


                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = onSignupClick) {
                        Text("Don't have an account? SIGNUP")
                    }
                }
            }
        }
    }
}

