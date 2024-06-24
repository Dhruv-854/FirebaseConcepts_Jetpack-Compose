package com.dhruv.realtimedb.firestoredb.Ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhruv.realtimedb.firestoredb.FirestoreModelResponse
import com.dhruv.realtimedb.util.CommonDialog
import com.dhruv.realtimedb.util.ResultState
import com.dhruv.realtimedb.util.showMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FirestoreScreen(
    modifier: Modifier = Modifier,
    isInput: MutableState<Boolean>,
    viewModel: FIrestoreViewModel = hiltViewModel(),
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val isDialog = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val res = viewModel.res.value
    val isUpdate = remember {
        mutableStateOf(false)
    }

    if (isDialog.value) {
        CommonDialog()
    }

    if (isInput.value) {
        AlertDialog(
            onDismissRequest = {
                isInput.value = false

            },
            text = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    TextField(
                        value = title, onValueChange = { title = it },
                        placeholder = {
                            Text(text = "Title")
                        }
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    TextField(
                        value = description,
                        onValueChange = { description = it },
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
                                FirestoreModelResponse.FirestoreItem(
                                    title = title,
                                    description = description
                                )
                            ).collect {
                                when (it) {
                                    is ResultState.Success -> {
                                        isInput.value = false
                                        isDialog.value = false
                                        context.showMsg(it.data)
                                        viewModel.getItem()
                                    }

                                    is ResultState.Failure -> {
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

    if (isUpdate.value) {
        UpdateData(
            item = viewModel.updateData.value,
            viewModel = viewModel,
            isUpdate = isUpdate,
            isDialog = isDialog
        )
    }


    if (res.data.isNotEmpty()) {
        LazyColumn {
            items(
                res.data,
                key = {
                    it.key!!
                }
            ) { items ->
                EachRow1(itemState = items, onUpdate = {
                    isUpdate.value = true
                    viewModel.setData(items)
                }
                ){
                    scope.launch(Dispatchers.Main) {
                        viewModel.delete(items.key!!).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isUpdate.value = false
                                    isDialog.value = false
                                    context.showMsg(it.data)
                                    viewModel.getItem()
                                }

                                is ResultState.Failure -> {
                                    isDialog.value = false
                                }

                                is ResultState.Loading -> {
                                    isDialog.value = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    if (res.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if (res.error.isNotEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = res.error)
        }
    }

}

@Composable
fun EachRow1(
    modifier: Modifier = Modifier,
    itemState: FirestoreModelResponse,
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
                        text = itemState.item?.title!!,
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
                    text = itemState.item?.description!!,
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
fun UpdateData(
    modifier: Modifier = Modifier,
    item: FirestoreModelResponse,
    viewModel: FIrestoreViewModel,
    isUpdate: MutableState<Boolean>,
    isDialog: MutableState<Boolean>,

    ) {
    var title by remember { mutableStateOf(item.item?.title) }
    var description by remember { mutableStateOf(item.item?.description) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    AlertDialog(
        onDismissRequest = {
            isUpdate.value = false

        },
        text = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                TextField(
                    value = title!!, onValueChange = { title = it },
                    placeholder = {
                        Text(text = "Title")
                    }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                TextField(
                    value = description!!,
                    onValueChange = { description = it },
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
                            FirestoreModelResponse(
                                item = FirestoreModelResponse.FirestoreItem(
                                    title = title,
                                    description = description
                                ),
                                key = item.key
                            )
                        ).collect {
                            when (it) {
                                is ResultState.Success -> {
                                    isUpdate.value = false
                                    isDialog.value = false
                                    context.showMsg(it.data)
                                    viewModel.getItem()
                                }

                                is ResultState.Failure -> {
                                    isDialog.value = false
                                }

                                is ResultState.Loading -> {
                                    isDialog.value = true
                                }
                            }
                        }
                    }
                }) {
                    Text(text = "Update")
                }
            }
        }
    )
}

