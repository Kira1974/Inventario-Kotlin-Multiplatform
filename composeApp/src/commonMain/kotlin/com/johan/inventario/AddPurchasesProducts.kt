package com.johan.inventario

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
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.ProductPurchasesRepository
import com.johan.inventario.Model.ProductRepository

import com.johan.inventario.Model.Productos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale


@Composable
fun AddPurchasesProducts(
    showAddProductDialog: Boolean,
    onDismiss: () -> Unit,
    productStateHolder: ProductStateHolder,
    productStateHolderPurchases: ProductStateHolder,
    purchasesRepository: ProductPurchasesRepository,
    productRepository: ProductRepository,


    ) {

    val coroutineScope = rememberCoroutineScope()

    var productName by remember { mutableStateOf("") }
    var productEntries by remember { mutableStateOf(TextFieldValue("")) }
    var productEntriesPrecie by remember { mutableStateOf(TextFieldValue("")) }

    var newProductosCompras = remember { mutableStateListOf<Compras>() }

    var filteredProducts by remember { mutableStateOf(emptyList<Productos>()) }

    var showDialog by remember { mutableStateOf(false) }


    var expanded by remember { mutableStateOf(false) } // Estado para controlar si el menú está expandido

    if (showAddProductDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },


            text = {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "Agregar Nueva Compra",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )
                    }

                    // Espaciado entre el título y el contenido
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.Bottom // Alinear los elementos al tope
                    ) {

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
                            },
                            modifier = Modifier.fillMaxWidth(0.8f),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))


                    Box(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                        // LazyColumn para mostrar los productos filtrados
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
                                                expanded = false // Cerrar la lista al seleccionar
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
                                Text("Cantidad de compras:", fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(25.dp))

                                TextField(
                                    value = productEntries,
                                    onValueChange = {
                                        if (it.text.all { char -> char.isDigit() }) { // Solo aceptar si todos los caracteres son números
                                            productEntries = it
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
                                Text(text = "Precio de compra:", fontSize = 15.sp)

                            }
                            Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth())
                    {
                        TextField(

                            value = productEntriesPrecie,
                            onValueChange = { newTextFieldValue ->
                                val newText = newTextFieldValue.text

                                // Filtramos solo los números
                                val filteredText = newText.filter { it.isDigit() }

                                if (filteredText.isNotEmpty()) {
                                    try {
                                        // Convertimos el número y formateamos con separador de miles
                                        val rawNumber = filteredText.toLongOrNull() ?: 0
                                        val formatter =
                                            NumberFormat.getInstance(Locale("es", "CO"))
                                        val formattedNumber = formatter.format(rawNumber)

                                        // Actualizamos el valor del TextField con el número formateado
                                        productEntriesPrecie = newTextFieldValue.copy(
                                            text = formattedNumber,
                                            selection = TextRange(formattedNumber.length) // Mantener el cursor al final
                                        )
                                    } catch (e: NumberFormatException) {
                                        // Si hay error en la conversión, simplemente actualizamos el texto sin formato
                                        productEntriesPrecie = newTextFieldValue.copy(
                                            text = filteredText
                                        )
                                    }
                                } else {
                                    // Si está vacío, actualizamos el valor vacío
                                    productEntriesPrecie = newTextFieldValue.copy(text = "")
                                }
                            },
                            modifier = Modifier.width(130.dp),
                            singleLine = true,
                            leadingIcon = {
                                Text(text = "$")  // Aquí agregas el símbolo "$" al inicio
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                                unfocusedIndicatorColor = Color.Black,
                            )

                        )
                    }



                            // Botones de opción
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        if (productName == ("") || productEntries == TextFieldValue(
                                                ""
                                            ) || productEntriesPrecie == TextFieldValue(
                                                ""
                                            )
                                        ) {
                                            showDialog = true
                                        } else {
                                            val newCompra = Compras(
                                                productoId = productStateHolder.productList.find { producto -> producto.nombre == productName }?.codigo
                                                    ?: "N/P", // Usa el código para establecer la relación de clave foránea
                                                nombre = productName,
                                                categoria = productStateHolder.productList.find { producto -> producto.nombre == productName }?.categoria
                                                    ?: "N/P",
                                                cantidad = productEntries.text.toIntOrNull() ?: 0,
                                                precioCompra = productEntriesPrecie.text,
                                                fecha = LocalDate.now().toString() // o la fecha que desees
                                            )
                                            newProductosCompras.add(newCompra)
                                            productName = ("")
                                            productEntries = TextFieldValue("")
                                            productEntriesPrecie = TextFieldValue("")
                                        }
                                    },
                                    modifier = Modifier.padding(4.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                                ) {
                                    Text("Añadir ")
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentPadding = PaddingValues(1.dp),
                    ) {
                        items(newProductosCompras) { compra ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                             horizontalArrangement = Arrangement.SpaceBetween

                            ){
                            Row(modifier = Modifier.fillMaxWidth(0.9f)
                                .background(Color(235,235,235),
                                    shape = RoundedCornerShape(16.dp)) ,
                                 ) {
                                Text(
                                    text = compra.nombre,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                                Text(
                                    text = ("Cantidad: ${compra.cantidad}"),
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                                Text(
                                    text = "Precio: ${compra.precioCompra}",
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f).padding(12.dp)
                                )
                            }
                                Row() {
                                    IconButton(
                                        onClick = { newProductosCompras.remove(compra) },
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

                    Row(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {


                        Button( // BOTON DE CANCELAR
                            onClick = {

                                productName = ("")
                                productEntries = TextFieldValue("")
                                productEntriesPrecie = TextFieldValue("")
                                newProductosCompras.clear()

                                onDismiss()
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                        ) {
                            Text("Cancelar")
                        }
                        Button( // BOTON DE CONFIRMAR
                            onClick = {

                                if (newProductosCompras.isEmpty()) {
                                    showDialog = true
                                } else {
                                    newProductosCompras.forEach { compra ->
                                        productStateHolderPurchases.addPurchase(compra)
                                        coroutineScope.launch {
                                            purchasesRepository.guardarCompra(compra).collect{}
                                        }
                                        if(productStateHolderPurchases.purchasesList.size >3000){
                                            var ultimo = productStateHolderPurchases.purchasesList[0]
                                            productStateHolderPurchases.deletePurchase(ultimo)
                                            coroutineScope.launch {
                                            purchasesRepository.eliminarCompra(ultimo).collect{}
                                        }
                                        }
                                        productStateHolder.productList.forEach { product ->
                                            if (product.nombre == compra.nombre) {
                                                product.comprasActuales += compra.cantidad
                                                product.precioCompra = compra.precioCompra
                                                product.stock =
                                                    product.comprasActuales - product.ventas
                                                val currentEntries = product.stock
                                                val minValue = product.desde.toIntOrNull()
                                                val maxValue = product.hasta.toIntOrNull()

                                                // Verificamos que currentEntries, minValue y maxValue no sean nulos
                                                if (currentEntries != null && minValue != null && maxValue != null) {
                                                    product.estado = when {
                                                        currentEntries in minValue..maxValue -> "Requerido"
                                                        currentEntries < minValue && currentEntries > 0 -> "Insuficiente"
                                                        currentEntries == 0 -> "Inexistente"
                                                        else -> "Suficiente"
                                                    }
                                                } else {
                                                    // Manejo de error si alguna de las entradas es nula
                                                    product.estado = "N/P"
                                                }


                                                coroutineScope.launch(Dispatchers.IO) {
                                                    productRepository.actualizarProducto(product).collect{

                                                    }
                                                }
                                            }


                                        }

                                    }

                                    // Intenta convertir las entradas a enteros, con manejo de posibles valores nulos
                                    newProductosCompras.clear()

                                    onDismiss()
                                } // Cerrar el diálogo },
                            },
                            modifier = Modifier.padding(4.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray)
                        ) {
                            Text("Guardar ")
                        }


                    }
                }
            },
            confirmButton = { null },
            dismissButton = { null }


        )


    }
    if (showDialog) {
        AlertDialog(

            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Advertencia")
            },
            text = {
                Text("No has anexado ningun producto.")
            },
            confirmButton = {
                Button(
                    onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(249, 109, 109))



                    ) {
                    Text("OK")
                }
            }
        )
    }
}
