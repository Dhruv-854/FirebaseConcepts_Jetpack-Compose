package com.dhruv.realtimedb.firestoredb.Ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.realtimedb.firestoredb.FirestoreModelResponse
import com.dhruv.realtimedb.firestoredb.repository.FirestoreRepository
import com.dhruv.realtimedb.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FIrestoreViewModel @Inject constructor(
    private val repo: FirestoreRepository
) : ViewModel() {

    private val _res : MutableState<FirestoreState> = mutableStateOf(FirestoreState())
    val res : State<FirestoreState> = _res

    private val _updateDate:MutableState<FirestoreModelResponse> = mutableStateOf(FirestoreModelResponse(
        item = FirestoreModelResponse.FirestoreItem(

        ),
    ))

    val updateData:State<FirestoreModelResponse> = _updateDate

    fun setData(data : FirestoreModelResponse){
        _updateDate.value = data
    }

    fun insert(item : FirestoreModelResponse.FirestoreItem) = repo.insert(item)


    init {
        getItem()
    }

    fun getItem() = viewModelScope.launch {
        repo.getItems().collect{
            when(it) {
                is ResultState.Success -> {
                    _res.value = FirestoreState(data = it.data)
                }
                is ResultState.Failure -> {
                    _res.value = FirestoreState(error = it.msg.toString())
                }
                ResultState.Loading -> {
                    _res.value = FirestoreState(isLoading = true)
                }
            }
        }
    }

    fun delete(key : String) = repo.delete(key)

    fun update(item : FirestoreModelResponse) = repo.update(item)



}

data class FirestoreState(
    val data : List<FirestoreModelResponse> = emptyList(),
    val error : String = "",
    val isLoading : Boolean = false
)