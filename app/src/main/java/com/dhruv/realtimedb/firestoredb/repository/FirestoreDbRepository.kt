package com.dhruv.realtimedb.firestoredb.repository

import com.dhruv.realtimedb.firestoredb.FirestoreModelResponse
import com.dhruv.realtimedb.util.ResultState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreDbRepository @Inject constructor(
    private val db: FirebaseFirestore,
) : FirestoreRepository {
    override fun insert(item: FirestoreModelResponse.FirestoreItem): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            db.collection("user")
                .add(item).addOnSuccessListener {
                    trySend(ResultState.Success("Data is inserted with ${it.id}"))
                }.addOnFailureListener {
                    trySend(ResultState.Failure(it))
                }
            awaitClose{
                close()
            }
        }

    override fun getItems(): Flow<ResultState<List<FirestoreModelResponse>>> = callbackFlow {
        trySend(ResultState.Loading)
        db.collection("user")
            .get()
            .addOnSuccessListener {

                val items = it.map {data->
                    FirestoreModelResponse(
                        item = FirestoreModelResponse.FirestoreItem(
                            title = data["title"] as String?,
                            description = data["description"] as String?
                        ),
                        key = data.id
                    )
                }
                trySend(ResultState.Success(items))
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }
        awaitClose{
            close()
        }
    }


    override fun delete(key: String): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        db.collection("user")
            .document(key)
            .delete()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    trySend(ResultState.Success("Data is deleted"))
                }
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }
            awaitClose{
                close()
            }

    }

    override fun update(item: FirestoreModelResponse): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val map = HashMap<String , Any>()
        map["title"] = item.item?.title!!
        map["description"] = item.item?.description!!

        db.collection("user")
            .document(item.key!!)
            .update(map)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    trySend(ResultState.Success("Data is updated"))
                }
            }.addOnFailureListener{
                trySend(ResultState.Failure(it))
            }
            awaitClose{
                close()
            }

    }
}