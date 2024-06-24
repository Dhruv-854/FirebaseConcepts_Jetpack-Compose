package com.dhruv.realtimedb.firebaseRealTimeDb.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Updater
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhruv.realtimedb.firebase.RealTimeModel
import com.dhruv.realtimedb.util.ResultState
import com.dhruv.realtimedb.util.showMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun RealTimeScreen(
    modifier: Modifier = Modifier,
    isInsert: MutableState<Boolean>,
    viewModel: RealTimeViewModel = hiltViewModel(),
) {
    val title = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val isDialog = remember {
        mutableStateOf(false)
    }
    val res = viewModel.res.value

    val isUpdate = remember {
        mutableStateOf(false)
    }



    if (isInsert.value) {

        AlertDialog(
            onDismissRequest = {
                isInsert.value = false
            },
            text = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = title.value, onValueChange = {
                        title.value = it
                    },
                        placeholder = {
                            Text(text = "Title")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(value = description.value, onValueChange = {
                        description.value = it
                    },
                        placeholder = {
                            Text(text = "Description")
                        }
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        scope.launch(Dispatchers.Main) {
                            viewModel.insert(
                                RealTimeModel.RealtimeItems(
                                    title = title.value,
                                    description = description.value
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Success -> {
                                        context.showMsg(it.data)
                                        isDialog.value = false
                                        isInsert.value = false
                                    }

                                    is ResultState.Failure -> {
                                        context.showMsg(
                                            it.msg.toString()
                                        )
                                        isDialog.value = false
                                    }

                                    is ResultState.Loading -> {
                                        isDialog.value = true
                                    }
                                }
                            }
                        }
                    }) {
                        Text(text = "Save")
                    }
                }
            }
        )

    }

    if (isUpdate.value){
        Update(isUpdate = isUpdate, itemState = viewModel.updateRes.value, viewModel = viewModel)
    }

    if (res.item.isNotEmpty()) {
        LazyColumn {
            items(
                res.item,
                key = {
                    it.key!!
                }
            ) { res ->
                EachRow(itemState = res.item!! , onUpdate = {
                    isUpdate.value = true
                    viewModel.setData(
                        res
                    )
                }, onDelete = {
                    scope.launch(Dispatchers.Main){
                        viewModel.delete(res.key!!).collect{
                            when (it) {
                                is ResultState.Success -> {
                                    context.showMsg(it.data)
                                    isDialog.value = true

                                }

                                is ResultState.Failure -> {
                                    context.showMsg(
                                        it.msg.toString()
                                    )
                                    isDialog.value = true

                                }

                                is ResultState.Loading -> {
                                    isDialog.value = true
                                }
                            }
                        }
                    }
                })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
    if (res.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    if (res.error.isNotEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = res.error)
        }
    }
}

@Composable
fun EachRow(
    modifier: Modifier = Modifier,
    itemState: RealTimeModel.RealtimeItems,
    onUpdate: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onUpdate()
                }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = itemState.title!!,
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                    IconButton(
                        onClick = {
                            onDelete()
                        },
                        modifier = modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete, contentDescription = null,
                            tint = Color.Red
                        )

                    }
                }
                Text(
                    text = itemState.description!!,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                )
            }
        }
    }

}

@Composable
fun Update(
    modifier: Modifier = Modifier,
    isUpdate: MutableState<Boolean>,
    itemState: RealTimeModel,
    viewModel: RealTimeViewModel,
) {
    val title = remember {
        mutableStateOf(itemState.item?.title )
    }
    val description = remember {
        mutableStateOf(itemState.item?.description)
    }


    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (isUpdate.value) {

        AlertDialog(
            onDismissRequest = {
                isUpdate.value = false
            },
            text = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(value = title.value!!, onValueChange = {
                        title.value = it
                    },
                        placeholder = {
                            Text(text = "Title")
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TextField(value = description.value!!, onValueChange = {
                        description.value = it
                    },
                        placeholder = {
                            Text(text = "Description")
                        }
                    )
                }
            },
            confirmButton = {
                Box(
                    modifier = modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        scope.launch(Dispatchers.Main) {
                            viewModel.update(
                                RealTimeModel(
                                    item = RealTimeModel.RealtimeItems(
                                        title = title.value,
                                        description = description.value
                                    ),
                                    key = itemState.key
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Success -> {
                                        context.showMsg(it.data)
                                        isUpdate.value = false
                                    }

                                    is ResultState.Failure -> {
                                        context.showMsg(
                                            it.msg.toString()
                                        )

                                    }

                                    is ResultState.Loading -> {
                                    }
                                }
                            }
                        }

                    }) {
                        Text(text = "Save")
                    }
                }
            }
        )

    }
}