package com.example.a6trip.data.auth

import android.content.Context
import com.example.a6trip.ui.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthRepository(
    private val auth: FirebaseAuth = Firebase.auth,
    context: Context
) {
    companion object {
        private const val PREFS_NAME = "LoginPrefs"
        private const val KEY_EMAIL = "email"
        private const val KEY_SENHA = "senha"
        private const val KEY_LEMBRAR = "lembrar"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val db = Firebase.firestore
    val currentUser get() = auth.currentUser
    val isLoggedIn get() = currentUser != null

    fun getSavedCredentials(): SavedCredentials? {
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val senha = prefs.getString(KEY_SENHA, null) ?: return null
        return if (prefs.getBoolean(KEY_LEMBRAR, false)) SavedCredentials(email, senha) else null
    }

    fun saveCredentials(email: String, senha: String) {
        prefs.edit()
            .putString(KEY_EMAIL, email)
            .putString(KEY_SENHA, senha)
            .putBoolean(KEY_LEMBRAR, true)
            .apply()
    }

    fun clearCredentials() {
        prefs.edit().clear().apply()
    }

    fun signIn(email: String, password: String, onResult: (Result<Unit>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)

            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun signUp(email: String, password: String, name: String, cpf: String, onResult: (Result<Unit>) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            onResult(Result.success(Unit))
                            val uid = auth.currentUser!!.uid
                            db.collection("users").document(uid).set(User(name, email, cpf))
                        }
                }
            }
            .addOnFailureListener { onResult(Result.failure(it)) }
    }

    fun resetPassword(email: String, onResult: (Result<Unit>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onResult(Result.success(Unit))
            }
            .addOnFailureListener { exception ->
                onResult(Result.failure(exception))
            }
    }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUserProfile(onResult: (Result<User?>) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            onResult(Result.success(null))
            return
        }
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val name = snapshot.getString("name") ?: ""
                    val email = snapshot.getString("email") ?: ""
                    val cpf = snapshot.getString("cpf") ?: ""
                    onResult(Result.success(User(name, email, cpf)))
                } else {
                    onResult(Result.success(null))
                }
            }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }

    data class SavedCredentials(val email: String, val senha: String)
}
