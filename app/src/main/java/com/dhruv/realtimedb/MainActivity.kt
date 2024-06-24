package com.dhruv.realtimedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dhruv.realtimedb.firebaseAuth.Ui.AuthScreen
import com.dhruv.realtimedb.firebaseRealTimeDb.ui.RealTimeScreen
import com.dhruv.realtimedb.firestoredb.Ui.FirestoreScreen
import com.dhruv.realtimedb.ui.theme.RealTimeDBTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealTimeDBTheme {

                val isInput = remember {
                    mutableStateOf(false)
                }



                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 24.dp),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            isInput.value = true
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                ) { paddingValues ->
//                    FirestoreScreen(isInput = isInput, modifier = Modifier.padding(paddingValues))
                    AuthScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}

