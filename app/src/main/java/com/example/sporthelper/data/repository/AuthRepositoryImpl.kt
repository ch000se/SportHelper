package com.example.sporthelper.data.repository

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.sporthelper.data.util.Constants
import com.example.sporthelper.domain.model.AuthStatus
import com.example.sporthelper.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager,
) : AuthRepository {

    override val authStatus: Flow<AuthStatus> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            trySend(
                if (user != null) {
                    AuthStatus.AUTHENTICATED
                } else {
                    AuthStatus.UNAUTHENTICATED
                }
            )
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.distinctUntilChanged()

    override suspend fun signOut(): Result<Boolean> {
        return try {
            firebaseAuth.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInAnonymously(): Result<Boolean> {
        return try {
            firebaseAuth.signInAnonymously().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(context: Context): Result<Boolean> {
        return try {
            val authCredentialResult = getGoogleAuthCredential(context)
            if (authCredentialResult.isSuccess) {
                val authCredential = authCredentialResult.getOrThrow()

                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                Result.success(isNewUser)

            } else {
                Result.failure(
                    authCredentialResult.exceptionOrNull()
                        ?: Exception("Unknown error occurred while obtaining Google credentials")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean> {
        return try {
            val authCredentialResult = getGoogleAuthCredential(context)
            if (authCredentialResult.isSuccess) {
                val authCredential = authCredentialResult.getOrThrow()
                run {
                    firebaseAuth.currentUser?.linkWithCredential(authCredential)?.await()
                    Result.success(true)
                }
            } else {
                Result.failure(
                    authCredentialResult.exceptionOrNull()
                        ?: Exception("Unknown error occurred while obtaining Google credentials")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getGoogleAuthCredential(context: Context): Result<AuthCredential> {
        return try {
            val nonce = UUID.randomUUID().toString()
            val signInWithGoogle: GetSignInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(Constants.GOOGLE_CLIENT_ID)
                .setNonce(nonce)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest
                .Builder()
                .addCredentialOption(signInWithGoogle)
                .build()
            val credential = credentialManager.getCredential(context, request).credential
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            Result.success(authCredential)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}