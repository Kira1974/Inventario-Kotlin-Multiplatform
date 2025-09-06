package com.johan.inventario

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import appDataBase.DBFactory
import inventario.composeapp.generated.resources.Logo
import inventario.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import java.io.File


fun main() = application {

    val db = DBFactory.createDataBase()
    Window(
        onCloseRequest = {
            DBFactory.closeDatabase()
            exitApplication()

        },
        icon = painterResource(Res.drawable.Logo),
        title = "Inventario",
        state = rememberWindowState(
            placement = WindowPlacement.Maximized // La ventana se abrirá maximizada
        )
    ) {




        App(db, DBFactory) // Pasa la base de datos y el DBFactory a tu aplicación




    }
}

