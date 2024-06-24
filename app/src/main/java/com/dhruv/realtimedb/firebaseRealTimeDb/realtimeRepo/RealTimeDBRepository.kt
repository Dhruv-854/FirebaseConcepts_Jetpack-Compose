package com.dhruv.realtimedb.firebaseRealTimeDb.realtimeRepo

import com.dhruv.realtimedb.firebase.RealTimeModel
import com.dhruv.realtimedb.util.ResultState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RealTimeDBRepository @Inject constructor(
    private val db: DatabaseReference,
) : RealTimeRepository {


    override fun insert(
        item: RealTimeModel.RealtimeItems,
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        db.push().setValue(
            item
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(ResultState.Success("Success"))
            }
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }

        awaitClose {
            close()
        }
    }

    override fun getItems(): Flow<ResultState<List<RealTimeModel>>> = callbackFlow {
        trySend(ResultState.Loading)


        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = snapshot.children.map {
                    RealTimeModel(
                        it.getValue(RealTimeModel.RealtimeItems::class.java),
                        key = it.key
                    )
                }
                trySend(ResultState.Success(items))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Failure(error.toException()))
            }
        }

        db.addValueEventListener(valueEvent)

        awaitClose {
            db.removeEventListener(valueEvent)
            close()
        }


    }

    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        db.child(key).removeValue().addOnCompleteListener {
            trySend(ResultState.Success("Item Deleted"))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }
        awaitClose {
            close()
        }
    }

    override fun update(res: RealTimeModel): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map = HashMap<String, Any>()
        map["title"] = res.item?.title!!
        map["description"] = res.item.description!!

        db.child(res.key!!).updateChildren(map).addOnCompleteListener {
            trySend(ResultState.Success("Item Updated"))
        }.addOnFailureListener {
            trySend(ResultState.Failure(it))
        }
        awaitClose {
            close()
        }

    }
}