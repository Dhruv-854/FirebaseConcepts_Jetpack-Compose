package com.dhruv.realtimedb.firebaseAuth.Ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhruv.realtimedb.firebaseAuth.AuthUser
import com.dhruv.realtimedb.util.CommonDialog
import com.dhruv.realtimedb.util.ResultState
import com.dhruv.realtimedb.util.showMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    var email1 by remember {
        mutableStateOf("")
    }

    var password1 by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()
    val context = LocalContext .current
    var isDialog by remember {mutableStateOf(false)}

    if (isDialog) {
        CommonDialog()
    }


    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        item {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Register")
                Spacer(modifier = modifier.padding(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(text = "Email") }
                )
                Spacer(modifier = modifier.padding(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(text = "Password") }
                )
                Spacer(modifier = modifier.padding(8.dp))
                Button(onClick = {
                    scope.launch(Dispatchers.Main) {
                        viewModel.createUser(
                            AuthUser(
                                email, password
                            )
                        ).collect{
                            when(it){
                                is ResultState.Success -> {
                                    context.showMsg(it.data)

                                    isDialog = false
                                }
                                is ResultState.Failure -> {
                                    context.showMsg(it.msg.toString())
                                    isDialog = false
                                }
                                ResultState.Loading -> {
                                    isDialog = true
                                }
                            }
                        }
                    }
                }) {
                    Text(text = "Register")
                }
            }
        }

        item {
            Column(
                modifier = modifier
                    .fillMaxWidth().padding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Login")
                Spacer(modifier = modifier.padding(8.dp))
                TextField(
                    value = email1,
                    onValueChange = { email1 = it },
                    placeholder = { Text(text = "Email") }
                )
                Spacer(modifier = modifier.padding(8.dp))
                TextField(
                    value = password1,
                    onValueChange = { password1 = it },
                    placeholder = { Text(text = "Password") }
                )
                Spacer(modifier = modifier.padding(8.dp))
                Button(onClick = {
                    scope.launch(Dispatchers.Main) {
                        viewModel.loginUser(
                            AuthUser(
                                email1, password1
                            )
                        ).collect{
                            when(it){
                                is ResultState.Success -> {
                                    context.showMsg(it.data)

                                    isDialog = false
                                }
                                is ResultState.Failure -> {
                                    context.showMsg(it.msg.toString())
                                    isDialog = false
                                }
                                ResultState.Loading -> {
                                    isDialog = true
                                }
                            }
                        }
                    }
                }) {
                    Text(text = "Login")
                }
            }
        }


    }
}