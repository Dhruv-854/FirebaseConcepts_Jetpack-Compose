package com.dhruv.realtimedb.firebaseRealTimeDb.realtimeRepo

import com.dhruv.realtimedb.firebase.RealTimeModel
import com.dhruv.realtimedb.util.ResultState
import kotlinx.coroutines.flow.Flow

interface RealTimeRepository {

    fun insert(
        item: RealTimeModel.RealtimeItems,
    ): Flow<ResultState<String>>

    fun getItems(): Flow<ResultState<List<RealTimeModel>>>

    fun delete(
        key : String
    ): Flow<ResultState<String>>


    fun update(
        res : RealTimeModel
    ) : Flow<ResultState<String>>


}