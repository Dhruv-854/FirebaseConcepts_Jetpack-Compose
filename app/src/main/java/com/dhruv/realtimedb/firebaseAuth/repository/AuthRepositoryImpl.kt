package com.dhruv.realtimedb.firebaseAuth.repository

import android.util.Log
import com.dhruv.realtimedb.firebaseAuth.AuthUser
import com.dhruv.realtimedb.util.ResultState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authdb : FirebaseAuth
) : AuthRepository {
    override fun createUser(auth: AuthUser): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)
        authdb.createUserWithEmailAndPassword(
            auth.email!!,
            auth.password!!
        ).addOnCompleteListener{
            if (it.isSuccessful){
                trySend(ResultState.Success("User Created"))
                Log.d("main" , "Current User id ${authdb.currentUser?.uid}")
            }
        }.addOnFailureListener{
            trySend(ResultState.Failure(it))
        }

        awaitClose{
            close()
        }
    }

    override fun loginUser(auth: AuthUser): Flow<ResultState<String>> = callbackFlow {

        trySend(ResultState.Loading)

        authdb.signInWithEmailAndPassword(
            auth.email!!,
            auth.password!!
        ).addOnSuccessListener {
            trySend(ResultState.Success("User Logged In"))
            Log.d("main" , "Current User id ${authdb.currentUser?.uid}")
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }

        awaitClose{
            close()
        }

    }
}