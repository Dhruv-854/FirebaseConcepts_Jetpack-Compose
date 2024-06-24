package com.dhruv.realtimedb.firestoredb.repository

import com.dhruv.realtimedb.firestoredb.FirestoreModelResponse
import com.dhruv.realtimedb.util.ResultState
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {


    fun insert(
        item : FirestoreModelResponse.FirestoreItem
    ) : Flow<ResultState<String>>

    fun getItems() : Flow<ResultState<List<FirestoreModelResponse>>>

    fun delete(
        key : String
    ) : Flow<ResultState<String>>

    fun update(
        item : FirestoreModelResponse
    ) : Flow<ResultState<String>>

}