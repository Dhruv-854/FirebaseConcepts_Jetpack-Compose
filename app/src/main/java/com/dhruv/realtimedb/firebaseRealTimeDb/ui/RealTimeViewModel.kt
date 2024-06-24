package com.dhruv.realtimedb.firebaseRealTimeDb.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruv.realtimedb.firebase.RealTimeModel
import com.dhruv.realtimedb.firebaseRealTimeDb.realtimeRepo.RealTimeRepository
import com.dhruv.realtimedb.util.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealTimeViewModel @Inject constructor(
    private val repo: RealTimeRepository,
) : ViewModel() {
    private val _res: MutableState<ItemState> = mutableStateOf(ItemState())
    val res: State<ItemState> = _res

    fun insert(items: RealTimeModel.RealtimeItems) = repo.insert(items)

    private val _updateRes : MutableState<RealTimeModel> = mutableStateOf(
        RealTimeModel(
            item = RealTimeModel.RealtimeItems()
        )
    )

    val updateRes : State<RealTimeModel> = _updateRes
    fun setData(data: RealTimeModel) {
        _updateRes.value = data
    }

    init {
        viewModelScope.launch {

            repo.getItems().collect {
                when (it) {

                    is ResultState.Success -> {
                        _res.value = ItemState(item = it.data)
                    }

                    is ResultState.Failure -> {
                        _res.value = ItemState(error = it.msg.toString())
                    }

                    ResultState.Loading -> {
                        _res.value = ItemState(isLoading = true)

                    }
                }
            }
        }
    }

    fun delete(key: String) = repo.delete(key)
    fun update (item: RealTimeModel) = repo.update(item)
}

data class ItemState(

    val item: List<RealTimeModel> = emptyList(),

    val error: String = "",

    val isLoading: Boolean = false,
)