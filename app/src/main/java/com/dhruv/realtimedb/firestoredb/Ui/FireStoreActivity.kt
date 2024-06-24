package com.dhruv.realtimedb.firestoredb.Ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dhruv.realtimedb.ui.theme.RealTimeDBTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FireStoreActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            RealTimeDBTheme{
                val isInput = remember {
                    mutableStateOf(false)
                }
                Surface {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(onClick ={
                                isInput.value = true
                            }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            }
                        }
                    ) {paddingValues ->
                        Column(
                            modifier = Modifier.padding(paddingValues)
                        ) {

                        }
                    }
                }
            }
        }
    }

}