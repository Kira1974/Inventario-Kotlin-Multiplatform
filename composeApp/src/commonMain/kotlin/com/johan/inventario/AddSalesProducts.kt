package com.johan.inventario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.johan.inventario.Model.ProductRepository
import com.johan.inventario.Model.ProductSalesRepository
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDate


@Composable
fun AddSalesProducts(
    showAddProductDialog: Boolean,
    onDismiss: () -> Unit,
    productStateHolder: ProductStateHolder,
    productStateHolderSales: ProductStateHolder,
    salesRepository: ProductSalesRepository,
    productRepository: ProductRepository


) {
    val coroutineScope = rememberCoroutineScope()


    val newProductosVentas = remember { mutableStateListOf<Ventas>() }
    var totalVentas = remember { mutableStateOf("0") }
    val selectedPrecie = remember { mutableStateOf("0") }

    var showDialogInventario by remember { mutableStateOf(false) }

    var showDialogShopping by remember { mutableStateOf(false) }

    val selectedButton = remember { mutableStateOf("Ninguno") }

    var filteredProducts by remember { mutableStateOf(emptyList<Productos>()) }


    var productName by remember { mutableStateOf("") }
    var productExits by remember { mutableStateOf(TextFieldValue("")) }
    var productExitsPrecie by remember { mutableStateOf(("0")) }
    var productExitsPrecieCarry by remember { mutableStateOf(("0")) }
    var productExitsPrecieInusual by remember { mutableStateOf(("0")) }
    var productExitsPrecieUsual by remember { mutableStateOf(("0")) }


    var expanded by remember { mutableStateOf(false) } // Estado para controlar si el menú está expandido

    if (showAddProductDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss()
                               totalVentas.value = "0"},
            text = {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "Agregar Nueva Venta",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp

                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalAlignment = Alignment.Bottom
                    )
                    {
                        // Nombre del producto
                        Text(text = "Nombre del producto:", fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(25.dp))
                        TextField(
                            value = productName,
                            onValueChange = { newText ->
                                productName = newText
                                expanded = newText.isNotEmpty() // Solo mostrar la lista si hay texto

                                // Dividir el texto de búsqueda en palabras individuales y convertir todo a minúsculas
                                val searchWords = newText.split(" ").map { it.trim().lowercase() }

                                // Filtrar productos basados en lo que escribe el usuario
                                filteredProducts = productStateHolder.productList
                                    .filter { product ->
                                        val name = product.nombre.lowercase()

                                        // Verificar si todas las palabras del texto de búsqueda están contenidas en el nombre
                                        searchWords.all { searchWord ->
                                            name.contains(searchWord)
                                        }
                                    }
                                    .sortedBy { it.nombre } // Ordenar alfabéticamente
                            }
                            ,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // LazyColumn para mostrar los productos filtrados
                    Box(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                        if (expanded && filteredProducts.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp) // Limitar la altura máxima de la lista
                                    .padding(top = 4.dp) // Un pequeño espaciado entre el TextField y la lista
                                    .background(Color(238, 238, 238))
                                    .zIndex(2f)
                            ) {
                                items(filteredProducts) { product ->
                                    Text(
                                        text = product.nombre,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                productName = product.nombre
                                                expanded =
                                                    false // Cerrar la lista al seleccionar
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                        Column(

                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().height(60.dp).padding(4.dp),
                                verticalAlignment = Alignment.Bottom
                            )
                            {
                                Text("Cantidad de ventas:", fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(25.dp))

                                TextField(
                                    value = productExits,
                                    onValueChange = {
                                        if (it.text.all { char -> char.isDigit() }) { // Solo aceptar si todos los caracteres son números
                                            productExits = it
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(0.8f),
//                                    .border(
//                                    width = 0.5.dp,
//                                    color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                    shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                                ),
                                    maxLines = 1,
                                    colors = TextFieldDefaults.textFieldColors(
                                        backgroundColor = Color.Transparent,
                                        cursorColor = Color.Black,
                                        focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                                        unfocusedIndicatorColor = Color.Black,
                                    )
                                )

                            }


                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().height(60.dp).padding(4.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {

                                // Precio
                                Text(text = "Precio de venta:", fontSize = 15.sp)
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Columna 1 - Unidad
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = {
                                            selectedButton.value = "Publico instalado" // Cambia el estado al pulsar el botón
                                            productStateHolder.productList.forEach { product ->
                                                if (product.nombre == productName) {
                                                    productExitsPrecie = product.precioVenta
                                                }
                                            }
                                            selectedPrecie.value = productExitsPrecie
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = if (selectedButton.value == "Publico instalado") Color(
                                                127, 182, 198
                                            ) else Color.White
                                        ),
                                        border = BorderStroke(1.2.dp, Color(127, 182, 198))
                                    ) {
                                        Text("Publico instalado")
                                    }
                                    // Texto debajo del botón
                                    productStateHolder.productList.forEach { product ->
                                        if (product.nombre == productName) {
                                            Text("$ ${product.precioVenta}")
                                        }
                                    }
                                }


                                // Columna 2 - Cliente Normal
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = {
                                            selectedButton.value =
                                                "Publico llevar" // Cambia el estado al pulsar el botón
                                            productStateHolder.productList.forEach { product ->
                                                if (product.nombre == productName) {
                                                    productExitsPrecieCarry =
                                                        product.precioVentallevar
                                                }
                                            }
                                            selectedPrecie.value = productExitsPrecieCarry

                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = if (selectedButton.value == "Publico llevar") Color(
                                                127, 182, 198
                                            ) else Color.White
                                        ),
                                        border = BorderStroke(1.2.dp, Color(127, 182, 198))
                                    ) {
                                        Text("Publico llevar")
                                    }
                                    // Texto debajo del botón

                                    productStateHolder.productList.forEach { product ->
                                        if (product.nombre == productName) {
                                            Text("$ ${product.precioVentallevar}")
                                        }
                                    }

                                }
                                }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {

                                // Columna 3 - Cliente Frecuente
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = {
                                            selectedButton.value =
                                                "Por mayor frecuente"
                                            productStateHolder.productList.forEach { product ->
                                                if (product.nombre == productName) {
                                                    productExitsPrecieUsual =
                                                        product.precioVentaPorMayorClienteFrecuente
                                                }
                                            }
                                            selectedPrecie.value = productExitsPrecieUsual

                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = if (selectedButton.value == "Por mayor frecuente") Color(
                                                127, 182, 198
                                            ) else Color.White
                                        ),
                                        border = BorderStroke(1.2.dp, Color(127, 182, 198))
                                    ) {
                                        Text("Por mayor frecuente")
                                    }
                                    productStateHolder.productList.forEach { product ->
                                        if (product.nombre == productName) {
                                            Text("$ ${product.precioVentaPorMayorClienteFrecuente}")
                                        }
                                    }
                                }
                                // Columna 4 - Cliente Frecuente
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Button(
                                        onClick = {
                                            selectedButton.value =
                                                "Por mayor no frecuente"
                                            productStateHolder.productList.forEach { product ->
                                                if (product.nombre == productName) {
                                                    productExitsPrecieInusual =
                                                        product.precioVentaPorMayorClienteNormal
                                                }
                                            }
                                            selectedPrecie.value = productExitsPrecieInusual

                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = if (selectedButton.value == "Por mayor no frecuente") Color(
                                                127, 182, 198
                                            ) else Color.White
                                        ),
                                        border = BorderStroke(1.2.dp, Color(127, 182, 198))
                                    ) {
                                        Text("Por mayor no frecuente")
                                    }
                                    productStateHolder.productList.forEach { product ->
                                        if (product.nombre == productName) {
                                            Text("$ ${product.precioVentaPorMayorClienteNormal}")
                                        }
                                    }
                                }

                            }


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End

                            ) {
                                Button(
                                    onClick = {
                                        if (productName == ("") || productExits == TextFieldValue(
                                                ""
                                            )
                                        ) {
                                            showDialogShopping = true
                                        } else {
                                            val newVenta = Ventas(
                                                productoId = productStateHolder.productList.find { producto -> producto.nombre == productName }?.codigo
                                                    ?: "N/P", // Usa el código para establecer la relación de clave foránea
                                                nombre = productName,
                                                categoria = productStateHolder.productList.find { producto -> producto.nombre == productName }?.categoria
                                                    ?: "N/P",
                                                cantidad = productExits.text.toIntOrNull() ?: 0,
                                                precioSeleccionado = selectedPrecie.value,
                                                fecha = LocalDate.now().toString() // o la fecha que desees
                                            )
                                           newProductosVentas.add(newVenta)
                                            productName = ("")
                                            selectedPrecie.value = ("0")
                                            selectedButton.value = "ninguno"
                                           productExits = TextFieldValue("")
                                        }
                                    },
                                    modifier = Modifier.padding(4.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                                ) {
                                    Text("Añadir")
                                }
                            }
                        }
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de productos agregados
                    Text(
                        text = "Productos Agregados",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .height(120.dp),
                        contentPadding = PaddingValues(1.dp),
                    ) {
                        items(newProductosVentas) { venta ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween

                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                        .background(
                                            Color(235, 235, 235),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                ) {
                                    Text(
                                        text = venta.nombre,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.weight(1f).padding(12.dp)
                                    )
                                    Text(
                                        fontWeight = FontWeight.SemiBold,
                                        text = "Cantidad: ${venta.cantidad}",
                                        modifier = Modifier.weight(1f).padding(12.dp)
                                    )
                                    Text(
                                        fontWeight = FontWeight.SemiBold,
                                        text = "Precio: ${venta.precioSeleccionado}",
                                        modifier = Modifier.weight(1f).padding(12.dp)
                                    )
                                }
                                Row() {
                                    IconButton(
                                        onClick = { newProductosVentas.remove(venta)
                                            if (newProductosVentas.isEmpty()) totalVentas.value = "0"
                                            var totalventas= 0
                                            for(venta in newProductosVentas) {

                                                var localeCO = java.util.Locale("es", "CO")
                                                val formatter = NumberFormat.getInstance(localeCO)
                                                val numero = formatter.parse(venta.precioSeleccionado)
                                                        ?.toInt() ?: 0

                                                val total = venta.cantidad * numero
                                                totalventas += total
                                                totalVentas.value = formatter.format(totalventas)

                                            }
                                            },
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Eliminar producto",
                                            tint = Color.Red
                                        )
                                    }
                                }


                            }
                        }
                    }
                    var verdaderoTotal = 0
                    for(venta in newProductosVentas){

                        var localeCO = java.util.Locale("es", "CO")
                        val formatter = NumberFormat.getInstance(localeCO)
                        val numero = formatter.parse(venta.precioSeleccionado)?.toInt() ?: 0


                        val total = venta.cantidad * numero
                        verdaderoTotal += total
                        totalVentas.value = formatter.format(verdaderoTotal)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().height(25.dp),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(text = "Total: $${totalVentas.value}")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {


                        Button(
                            onClick = {
                                productName = ("")
                                productExits = TextFieldValue("")
                                newProductosVentas.clear()
                                totalVentas.value = "0"
                                onDismiss()
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                if (newProductosVentas.isEmpty()) {
                                    showDialogShopping = true
                                } else {
                                    var inventarioInsuficiente = false
                                    newProductosVentas.forEach { venta ->

                                        productStateHolder.productList.forEach { product ->
                                            if (product.nombre == venta.nombre) {
                                                val ventas = product.ventas
                                                val nuevaVenta = venta.cantidad
                                                if ((product.comprasActuales - (ventas + nuevaVenta)) < 0) {
                                                    inventarioInsuficiente = true
                                                    showDialogInventario = true
                                                }
                                            }
                                        }
                                    }

                                    if (!inventarioInsuficiente) {
                                        newProductosVentas.forEach { venta ->
                                            productStateHolderSales.addSale(venta)
                                            coroutineScope.launch {
                                                salesRepository.guardarVenta(venta).collect{

                                                }
                                                if(productStateHolderSales.salesList.size >6000){
                                                    var ultimo = productStateHolderSales.salesList[0]
                                                    productStateHolderSales.deleteSale(ultimo)
                                                    coroutineScope.launch {
                                                        salesRepository.eliminarVenta(ultimo).collect{}
                                                    }
                                                }
                                            }
                                            productStateHolder.productList.forEach { product ->
                                                if (product.nombre == venta.nombre) {
                                                    product.ventas += venta.cantidad
                                                    product.stock =
                                                        product.comprasActuales - product.ventas
                                                    val currentEntries = product.stock
                                                    val minValue = product.desde.toIntOrNull()
                                                    val maxValue = product.hasta.toIntOrNull()

                                                    if (currentEntries != null && minValue != null && maxValue != null) {
                                                        product.estado = when {
                                                            currentEntries in minValue..maxValue -> "Requerido"
                                                            currentEntries < minValue && currentEntries > 0 -> "Insuficiente"
                                                            currentEntries == 0 -> "Inexistente"
                                                            else -> "Suficiente"
                                                        }
                                                    } else {
                                                        product.estado = "N/P"
                                                    }
                                                    coroutineScope.launch(Dispatchers.IO) {
                                                        productRepository.actualizarProducto(product).collect{

                                                        }
                                                    }

                                                }
                                            }
                                        }

                                        newProductosVentas.clear()
                                        totalVentas.value = "0"
                                        onDismiss()
                                    }
                                }
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                        ) {
                            Text("Guardar")
                        }


                    }
                }
            },

            confirmButton = { null },
            dismissButton = { null }


        )


    }
    if (showDialogInventario) {
        AlertDialog(

            onDismissRequest = { showDialogInventario = false },
            title = {
                Text(text = "Advertencia")
            },
            text = {
                Text("No hay suficientes productos en el inventario.")
            },
            confirmButton = {
                Button(
                    onClick = { showDialogInventario = false },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(127, 182, 198))

                ) {
                    Text("OK")
                }
            }
        )
    }
    if (showDialogShopping) {
        AlertDialog(

            onDismissRequest = { showDialogShopping = false },
            title = {
                Text(text = "Advertencia")
            },
            text = {
                Text("No has anexado ningun producto.")
            },
            confirmButton = {
                Button(
                    onClick = { showDialogShopping = false },
                    modifier = Modifier.padding(4.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(127, 182, 198))

                ) {
                    Text("OK")
                }
            }
        )
    }
}