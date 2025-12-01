package com.example.myuniapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.myuniapp.nav.AppNavGraph
import com.example.myuniapp.theme.MyUniAppTheme
import com.example.myuniapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyUniApp()
        }
    }
}

@Composable
fun MyUniApp() {
    var darkTheme by rememberSaveable { mutableStateOf(true) } // стартуем с dark

    MyUniAppTheme(darkTheme = darkTheme) {
        Surface {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            AppNavGraph(
                navController = navController,
                authViewModel = authViewModel,
                darkTheme = darkTheme,
                onToggleTheme = { darkTheme = !darkTheme }
            )
        }
    }
}


@Composable
fun MyUniAppRoot() {
    MyUniAppTheme(darkTheme = true) {
        Surface {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            AppNavGraph(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}
