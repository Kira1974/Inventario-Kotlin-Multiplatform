package com.johan.inventario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johan.inventario.Model.ProductRepository
import com.johan.inventario.Model.Productos
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EditProduct(
    producto: Productos,
    productStateHolder: ProductStateHolder,
    showAddDialog: Boolean,
    onDismiss: () -> Unit,
    productRepository: ProductRepository
) {


    val coroutineScope = rememberCoroutineScope()

    var productName by remember { mutableStateOf(TextFieldValue(producto.nombre)) }
    var productEntries by remember { mutableStateOf(TextFieldValue(producto.comprasActuales.toString())) }
    var productEntriesPrecie by remember { mutableStateOf(TextFieldValue("*****")) }
    var productCategory by remember { mutableStateOf(TextFieldValue(producto.categoria)) }
    var productExitsPrecie by remember { mutableStateOf(TextFieldValue(producto.precioVenta)) }
    var productExitsPrecieCarry by remember { mutableStateOf(TextFieldValue(producto.precioVentallevar)) }
    var productExitsPrecieInusual by remember { mutableStateOf(TextFieldValue(producto.precioVentaPorMayorClienteNormal)) }
    var productExitsPrecieUsual by remember { mutableStateOf(TextFieldValue(producto.precioVentaPorMayorClienteFrecuente)) }
    var productState by remember { mutableStateOf(producto.estado) }

    var isFormValid by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }


    var desde by remember { mutableStateOf(TextFieldValue(producto.desde)) }
    var hasta by remember { mutableStateOf(TextFieldValue(producto.hasta)) }


    if (showAddDialog) {
        AlertDialog(onDismissRequest = {

            onDismiss()
        },


            text = {


                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "Datos Basicos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalAlignment = Alignment.Bottom
                    )
                    {
                        Text("Nombre del producto: ", fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(25.dp))
                        TextField(
                            value = productName,
                            onValueChange = {
                                productName = it

                            },
                            modifier = Modifier.fillMaxWidth(0.8f),
//                                    .border(
//                                    width = 0.5.dp,
//                                    color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                    shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                                ),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        verticalAlignment = Alignment.Bottom

                    )
                    {

                        Text("Categoria del producto:", fontSize = 15.sp)
                        Spacer(modifier = Modifier.width(25.dp))

                        TextField(

                            value = productCategory,
                            onValueChange = { productCategory = it },
                            modifier = Modifier.fillMaxWidth(0.8f),
//                                 .border(
//                                  width = 0.5.dp,
//                                  color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                  shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                              ),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                                unfocusedIndicatorColor = Color.Black,
                            )

                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp).padding(4.dp),
                        verticalAlignment = Alignment.Bottom

                    ) {

                        Text(
                            text = "Notificación de Stock",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(35.dp))
//                        Text(
//                            text = "Desde ",
//                            fontSize = 16.sp,
//                        )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp).padding(4.dp),
                        verticalAlignment = Alignment.Bottom

                    ) {

                        TextField(
                            value = desde,
                            placeholder = { Text("Desde") },
                            onValueChange = {
                                if (it.text.all { char -> char.isDigit() }) { // Solo aceptar si todos los caracteres son números
                                    desde = it
                                }


                            },
                            modifier = Modifier
                                .width(80.dp),
//                                .border(
//                                    width = 0.5.dp,
//                                    color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                    shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                                ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )
//                        Text(
//                            text = "Hasta ",
//                            fontSize = 16.sp,
//                        )
                        Spacer(modifier = Modifier.width(20.dp)) // Espacio entre "Desde" y "Hasta"

                        TextField(
                            value = hasta,
                            placeholder = { Text("Hasta") },
                            onValueChange = {
                                if (it.text.all { char -> char.isDigit() }) { // Solo aceptar si todos los caracteres son números
                                    hasta = it
                                }
                            },
                            modifier = Modifier
                                .width(80.dp),
//                                .border(
//                                    width = 0.5.dp,
//                                    color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                    shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                                ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Etiqueta y campo para el precio
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                    ) {

                        Text(
                            "Nota: Menor a eso se requiere urgente, mayor a eso hay suficientes.",
                            fontSize = 12.sp
                        )

                    }


                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {

                        Text(
                            "Disponibles",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().height(60.dp).padding(4.dp),
                    )
                    {
                        Column(
                            modifier = Modifier
                                .weight(1f),

                            ) {

                            TextField(
                                value = productEntries,
                                onValueChange = {
                                    if (it.text.all { char -> char.isDigit() }) { // Solo aceptar si todos los caracteres son números
                                        productEntries = it
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.6f),
                                placeholder = { Text("Cantidad ") },
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color.Transparent,
                                    cursorColor = Color.Black,
                                    focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                                    unfocusedIndicatorColor = Color.Black,
                                )
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f),
                        ) {

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
                                modifier = Modifier.fillMaxWidth(0.6f),
//                                 .border(
//                                  width = 0.5.dp,
//                                  color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                  shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                              ),
                                placeholder = { Text("Precio") },
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

                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp).padding(4.dp),
                        verticalAlignment = Alignment.Bottom

                    ) {

                        Text(
                            "Precios",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().height(70.dp).padding(4.dp),
                    )
                    {
                        Column(
                            modifier = Modifier
                                .weight(1f),

                            ) {

                            TextField(
                                value = productExitsPrecie,
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
                                            productExitsPrecie = newTextFieldValue.copy(
                                                text = formattedNumber,
                                                selection = TextRange(formattedNumber.length) // Mantener el cursor al final
                                            )
                                        } catch (e: NumberFormatException) {
                                            // Si hay error en la conversión, simplemente actualizamos el texto sin formato
                                            productExitsPrecie = newTextFieldValue.copy(
                                                text = filteredText
                                            )
                                        }
                                    } else {
                                        // Si está vacío, actualizamos el valor vacío
                                        productExitsPrecie = newTextFieldValue.copy(text = "")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.95f),
//                                    .border(
//                                    width = 0.5.dp,
//                                    color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                    shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                                ),
                                placeholder = { Text("Publico instalado ") },
                                maxLines = 1,
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
                        Column(
                            modifier = Modifier
                                .weight(1f),
                        ) {

                            TextField(

                                value = productExitsPrecieCarry,
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
                                            productExitsPrecieCarry = newTextFieldValue.copy(
                                                text = formattedNumber,
                                                selection = TextRange(formattedNumber.length) // Mantener el cursor al final
                                            )
                                        } catch (e: NumberFormatException) {
                                            // Si hay error en la conversión, simplemente actualizamos el texto sin formato
                                            productExitsPrecieCarry = newTextFieldValue.copy(
                                                text = filteredText
                                            )
                                        }
                                    } else {
                                        // Si está vacío, actualizamos el valor vacío
                                        productExitsPrecieCarry = newTextFieldValue.copy(text = "")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.95f),
//                                 .border(
//                                  width = 0.5.dp,
//                                  color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                  shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                              ),
                                placeholder = { Text("Publico llevar") },
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
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().height(70.dp).padding(4.dp),
                    )
                    {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            TextField(

                                value = productExitsPrecieUsual,
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
                                            productExitsPrecieUsual = newTextFieldValue.copy(
                                                text = formattedNumber,
                                                selection = TextRange(formattedNumber.length) // Mantener el cursor al final
                                            )
                                        } catch (e: NumberFormatException) {
                                            // Si hay error en la conversión, simplemente actualizamos el texto sin formato
                                            productExitsPrecieUsual = newTextFieldValue.copy(
                                                text = filteredText
                                            )
                                        }
                                    } else {
                                        // Si está vacío, actualizamos el valor vacío
                                        productExitsPrecieUsual = newTextFieldValue.copy(text = "")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(0.95f),
//                                 .border(
//                                  width = 0.5.dp,
//                                  color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                  shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                              ),
                                placeholder = { Text("Por mayor frecuente") },
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
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            TextField(

                                value = productExitsPrecieInusual,
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
                                            productExitsPrecieInusual = newTextFieldValue.copy(
                                                text = formattedNumber,
                                                selection = TextRange(formattedNumber.length) // Mantener el cursor al final
                                            )
                                        } catch (e: NumberFormatException) {
                                            // Si hay error en la conversión, simplemente actualizamos el texto sin formato
                                            productExitsPrecieInusual = newTextFieldValue.copy(
                                                text = filteredText
                                            )
                                        }
                                    } else {
                                        // Si está vacío, actualizamos el valor vacío
                                        productExitsPrecieInusual =
                                            newTextFieldValue.copy(text = "")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
//                                 .border(
//                                  width = 0.5.dp,
//                                  color = Color.DarkGray, // Color del borde, ajústalo a lo que prefieras
//                                  shape = RoundedCornerShape(8.dp) // Bordes redondeados
//                              ),
                                placeholder = { Text("Por mayor no frecuente") },
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
                    }


                }
            },
            shape = RoundedCornerShape(16.dp),


            confirmButton = {
                Button(
                    onClick = {
                        isFormValid = productName.text.isNotEmpty() &&
                                productCategory.text.isNotEmpty() &&
                                productEntries.text.isNotEmpty() &&
                                productEntriesPrecie.text.isNotEmpty() &&
                                productExitsPrecie.text.isNotEmpty() &&
                                productExitsPrecieUsual.text.isNotEmpty() &&
                                productExitsPrecieInusual.text.isNotEmpty() &&
                                desde.text.isNotEmpty() &&
                                hasta.text.isNotEmpty()


                        if (isFormValid) {

                            // Intenta convertir las entradas a enteros, con manejo de posibles valores nulos
                            val currentEntries = productEntries.text.toIntOrNull()
                            val minValue = desde.text.toIntOrNull()
                            val maxValue = hasta.text.toIntOrNull()

                            // Verificamos que currentEntries, minValue y maxValue no sean nulos
                            if (currentEntries != null && minValue != null && maxValue != null) {
                                productState = when {
                                    currentEntries in minValue..maxValue -> "Requerido"
                                    currentEntries < minValue && currentEntries > 0 -> "Insuficiente"
                                    currentEntries == 0 -> "Inexistente"
                                    else -> "Suficiente"
                                }
                            } else {
                                // Manejo de error si alguna de las entradas es nula
                                productState = "N/P"
                            }

                            if (productName.text.isEmpty()) {
                                productName = TextFieldValue("N/P")
                            }
                            if (productCategory.text.isEmpty()) {
                                productCategory = TextFieldValue("N/P")
                            }
                            if (productEntriesPrecie.text.isEmpty()) {
                                productEntriesPrecie = TextFieldValue("0")
                            }

                          producto.nombre = productName.text
                           producto.categoria = productCategory.text
                           producto.precioCompra = productEntriesPrecie.text
                           producto.comprasActuales = productEntries.text.toIntOrNull() ?: 0
                           producto.desde = desde.text
                           producto.hasta = hasta.text
                           producto.precioVenta= productExitsPrecie.text
                            producto.precioVentallevar = productExitsPrecieCarry.text
                           producto.precioVentaPorMayorClienteFrecuente = productExitsPrecieUsual.text
                               producto.precioVentaPorMayorClienteNormal =productExitsPrecieInusual.text
                            producto.estado = productState
                            producto.stock = producto.comprasActuales - producto.ventas


                            productStateHolder.updateProduct(producto)
                            coroutineScope.launch {
                                productRepository.actualizarProducto(producto).collect{

                                } // Llama al método de actualización
                            }
                            onDismiss() // Cerrar el diálogo
                        } else {
                            showErrorDialog = true
                        }

                    },
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        productName = TextFieldValue("")
                        productCategory = TextFieldValue("")
                        desde = TextFieldValue("")
                        hasta = TextFieldValue("")
                        productEntries = TextFieldValue("")
                        productEntriesPrecie = TextFieldValue("")
                        productExitsPrecie = TextFieldValue("")
                        productExitsPrecieCarry = TextFieldValue("")
                        productExitsPrecieUsual = TextFieldValue("")
                        productExitsPrecieInusual = TextFieldValue("")
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color.Gray),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevation(6.dp)
                ) {
                    Text("Cancelar")
                }
            })
    }






    if (showErrorDialog) {
        AlertDialog(

            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(text = "Advertencia")
            },
            text = {
                Text("Debes llenar todos los campos.")
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("OK")
                }
            }
        )
    }

}

