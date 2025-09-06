package com.johan.inventario

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import appDataBase.DBFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            val db = DBFactory(this).createDataBase()
//            App(db)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {




}

