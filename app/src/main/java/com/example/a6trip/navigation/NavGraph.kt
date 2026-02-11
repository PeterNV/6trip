package com.example.a6trip.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a6trip.data.auth.AuthRepository
import com.example.a6trip.ui.screens.home.HomeScreen
import com.example.a6trip.ui.screens.login.LoginScreen
import com.example.a6trip.ui.screens.register.RegisterScreen
import com.example.a6trip.ui.screens.resetPassword.ResetPasswordScreen
import com.example.a6trip.ui.screens.welcome.WelcomeScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTER = "register"
    const val RESET = "reset"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val authRepo = remember(context) { AuthRepository(Firebase.auth, context) }

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onNavigateToLogin = { navController.navigate(Routes.LOGIN) },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                isLoggedIn = authRepo.isLoggedIn
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                authRepository = authRepo,
                onBack = { navController.popBackStack() },
                onRegister = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onReset = { navController.navigate(Routes.RESET) },

            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                authRepository = authRepo,
                onSignOut = {
                    authRepo.signOut()
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                authRepository = authRepo,
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.RESET) {
            ResetPasswordScreen(
                authRepository = authRepo,
                onBack = { navController.popBackStack() },
                onResetPasswordSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.RESET) { inclusive = true }
                    }
                }
            )
        }
    }
}
