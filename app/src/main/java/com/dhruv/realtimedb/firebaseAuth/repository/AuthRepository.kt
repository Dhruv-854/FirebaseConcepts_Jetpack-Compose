package com.dhruv.realtimedb.firebaseAuth.repository

import com.dhruv.realtimedb.firebaseAuth.AuthUser
import com.dhruv.realtimedb.util.ResultState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun createUser(
        auth : AuthUser
    ) : Flow<ResultState<String>>

    fun loginUser(
        auth : AuthUser
    ) : Flow<ResultState<String>>

}