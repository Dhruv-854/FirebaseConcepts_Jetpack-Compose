package com.dhruv.realtimedb.firebaseAuth.Ui

import androidx.lifecycle.ViewModel
import com.dhruv.realtimedb.firebaseAuth.AuthUser
import com.dhruv.realtimedb.firebaseAuth.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo : AuthRepository
): ViewModel() {

    fun createUser(authUser : AuthUser) = repo.createUser(authUser)

    fun loginUser(authUser: AuthUser) = repo.loginUser(authUser )



}