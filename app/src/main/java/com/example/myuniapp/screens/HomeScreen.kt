package com.example.myuniapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myuniapp.data.local.EventEntity
import com.example.myuniapp.viewmodel.EventViewModel
import kotlinx.coroutines.launch

private enum class HomeSection {
    Welcome, Events, MyEvents, Comment
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    eventViewModel: EventViewModel = viewModel()
) {
    val events by eventViewModel.events.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedSection by remember { mutableStateOf(HomeSection.Welcome) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                DrawerItem("Home") { selectedSection = HomeSection.Welcome; scope.launch { drawerState.close() } }
                DrawerItem("Event Db") { selectedSection = HomeSection.Events; scope.launch { drawerState.close() } }
                DrawerItem("My Events") { selectedSection = HomeSection.MyEvents; scope.launch { drawerState.close() } }
                DrawerItem("Comment") { selectedSection = HomeSection.Comment; scope.launch { drawerState.close() } }
            }
        }
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { Text("MyUni Events") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = onLogout) {
                            Icon(Icons.Default.Logout, contentDescription = "Logout")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                when (selectedSection) {
                    HomeSection.Welcome -> WelcomeSection()
                    HomeSection.Events -> EventsSection(
                        events = events,
                        query = searchQuery
                    )
                    HomeSection.MyEvents -> MyEventsSection(events = events)
                    HomeSection.Comment -> CommentSection(snackbarHostState = snackbarHostState)
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun WelcomeSection() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hello and welcome to Event app",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EventsSection(
    events: List<EventEntity>,
    query: String
) {
    val filtered = if (query.isBlank()) events
    else events.filter { it.title.contains(query, ignoreCase = true) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Events",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtered) { event ->
                EventRow(event)
            }
        }
    }
}

@Composable
private fun EventRow(event: EventEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = event.title, fontWeight = FontWeight.Bold)
        Text(text = "Time: ${event.time}")
        Text(text = "Place: ${event.place}")
    }
}

@Composable
private fun MyEventsSection(events: List<EventEntity>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "My Event",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(events) { event ->
                Text(
                    text = event.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CommentSection(snackbarHostState: SnackbarHostState) {
    var email by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Event comment",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isBlank() || comment.isBlank()) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Please fill all fields")
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Comment submitted")
                    }
                    email = ""
                    comment = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Submit")
        }
    }
}
