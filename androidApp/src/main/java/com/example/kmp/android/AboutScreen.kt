package com.example.kmp.android

import android.widget.Toolbar
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun AboutScreen (){
    
    Column {
        Toolbar()
        ContentView()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar() {
    TopAppBar(title = { 
        Text(text = "About Device")
    })
}
@Composable
fun ContentView() {
    val items = makeItems()
}

fun makeItems(): List<Pair<String,String>> {
return emptyList()

}
