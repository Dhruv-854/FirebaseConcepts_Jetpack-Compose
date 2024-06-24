package com.dhruv.realtimedb.firebase

data class RealTimeModel(
    val item : RealtimeItems?,
    val key : String? =""
){
    data class RealtimeItems(
        val title: String? ="",
        val description: String? ="",
    )
}
