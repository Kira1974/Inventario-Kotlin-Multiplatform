
package com.johan.inventario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.ProductRepository
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas
import inventario.composeapp.generated.resources.Res
import inventario.composeapp.generated.resources.Sell
import inventario.composeapp.generated.resources.close_eye
import inventario.composeapp.generated.resources.logo_mejorado
import inventario.composeapp.generated.resources.open_eye
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.text.NumberFormat

@Composable
@Preview
fun ComunScreen(
    navController: NavHostController,
    title: String = "Prueba",
    buttonColor: Color = Color.White,
    buttonColor2: Color = Color.White,
    buttonColor3: Color = Color.White,
    isArrowVisible: Boolean = false,
    isSearchAndAddVisible: Boolean = false,
    ShowAddDialog: () -> Unit,  // Función como parámetro
    ShowNeedPassword: () -> Unit,  // Función como parámetro
    ShowNeedPasswordPurchases: () -> Unit,  // Función como parámetro
    toggleOpenEyes: () -> Unit,  // Actualiza el estado de los ojos
    areOpenEyes: Boolean, // Indica si los ojos están abiertos
    onSearchQueryChange: (String) -> Unit // Función para manejar la búsqueda

) {

    // Estado para controlar cuándo mostrar el diálogo o el contenido


    var searchQuery by remember { mutableStateOf("") }
    //Columna de la pantalla

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // En esta fila va el titulo ,y  los botones de compra y venta
        Row(
            modifier = Modifier.fillMaxWidth(.95f),
            horizontalArrangement = Arrangement.SpaceBetween, // Separar el texto y los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ponemos la flecha si asi lo indicamos
                if (isArrowVisible) {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = " Volver")
                    }
                }
                Spacer(modifier = Modifier.width(25.dp))
                Image(
                    painterResource(Res.drawable.logo_mejorado),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 230.dp, height = 120.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón para "Compras" con ícono
                Button(
                    onClick = { ShowNeedPasswordPurchases() },//vamos a compras
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, Color(249, 109, 109))
                )
                {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Compras",
                        modifier = Modifier.size(24.dp) // Tamaño del ícono
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el ícono y el texto
                    Text("Compras")
                }
                Spacer(modifier = Modifier.width(8.dp)) //3 Espacio entre los dos botones

                // Botón para "Ventas" con ícono
                Button(
                    onClick = { navController.navigate("sales") },//vamos a ventas
                    colors = ButtonDefaults.buttonColors(buttonColor2),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, Color(127, 182, 198))

                ) {
                    Icon(
                        painter = painterResource(Res.drawable.Sell),
                        contentDescription = "Ventas",
                        modifier = Modifier.size(24.dp) // Tamaño del ícono
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el ícono y el texto
                    Text("Ventas")
                }
                Spacer(modifier = Modifier.width(8.dp)) //3 Espacio entre los dos botones

                // Botón para "Ventas" con ícono
                Button(
                    onClick = { navController.navigate("resume") },//vamos a ventas
                    colors = ButtonDefaults.buttonColors(buttonColor3),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.2.dp, Color(255, 202, 51))

                ) {
                    Icon(
                        Icons.Filled.DateRange, // painterResource(Res.drawable.Sell),
                        contentDescription = "Resumen",
                        modifier = Modifier.size(24.dp) // Tamaño del ícono
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el ícono y el texto
                    Text("Resumen")
                }
            }
        }
        //Linea para un mejor estilo
        Divider(
            color = Color.Black,
            thickness = 1.dp,  // Grosor de la línea
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))

        //LOGOTIPO , Y BOTONES DE BUSQUEDA Y AGREGAR PRODUCTOS.

        Row(

            modifier = Modifier.fillMaxWidth(.9f),
            horizontalArrangement = Arrangement.SpaceBetween, // Separar el texto y los botones
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                text = title, //Aca va el titulo de la pantalla
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif
            )
            if (isSearchAndAddVisible) {


                //Hacemos una fila dentro de la otra fila
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End // Alinear el icono a la derecha
                ) { // Campo de búsqueda
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            onSearchQueryChange(it) // Notificar el cambio a la pantalla principal
                        },
                        label = { Text("Buscar productos...") },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black, // Sin línea cuando tiene foco
                            unfocusedIndicatorColor = Color.Black,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.3f), // Puedes ajustar el tamaño según prefieras
                        leadingIcon = {

                            Icon(Icons.Filled.Search, contentDescription = "Buscar producto")
                        }
                    )

                    Spacer(modifier = Modifier.width(25.dp))
                    IconButton(onClick = { ShowAddDialog() }) {
                        Icon(Icons.Filled.Add, contentDescription = "Agregar producto")

                    }
                    IconButton(onClick = {
                        if (areOpenEyes) {
                            toggleOpenEyes() // Cierra los ojos directamente
                        } else {
                            ShowNeedPassword() // Pide la contraseña
                        }
                    }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (areOpenEyes) Res.drawable.open_eye else Res.drawable.close_eye
                            ),
                            contentDescription = "Toggle Eyes",
                            modifier = Modifier.size(24.dp)
                        )
                    }


                }
            }

        }
    }
}


//ESTILO COMUN PARA LAS TABLAS DE LAS 3 PANTALLAS


//Estilo de encabezado de la Tabla
@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = modifier.padding(8.dp),
        fontWeight = FontWeight.SemiBold,
        color = Color.Gray,
        textAlign = TextAlign.Center
    )
}

// Estilo fila de la tabla de productos
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductRowHome(product: Productos, onClick: () -> Unit,  showRealPrice: Boolean // Nuevo parámetro
) {
    var isHovered by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            )
            // Dibuja las líneas solo arriba y abajo
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val color = Color.LightGray
                // Línea superior
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
                // Línea inferior
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(8.dp)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ProductCellHome(product.codigo, Modifier.weight(0.7f))
            ProductCellHome(product.nombre, Modifier.weight(2f))
            ProductCellHome(product.categoria, Modifier.weight(1f))
            ProductCellHome(product.comprasActuales.toString(), Modifier.weight(1f))
            ProductCellHome(product.ventas.toString(), Modifier.weight(1f))
            ProductCellStock(product.stock.toString(), Modifier.weight(1f))
            ProductCellHome(
                if (showRealPrice) "$ ${product.precioCompra}" else "******",
                Modifier.weight(1f)
            )
            ProductCellHome(product.estado, Modifier.weight(1f))
        }
    }

}// Estilo fila de la tabla de compras

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductRowPurchases(product: Compras, onClick: () -> Unit) {
    var isHovered by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            )
            // Dibuja las líneas solo arriba y abajo
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val color = Color.LightGray
                // Línea superior
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
                // Línea inferior
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(8.dp)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            var precioOriginal = product.precioCompra
            var localeCO = java.util.Locale("es", "CO")
            val formatter = NumberFormat.getInstance(localeCO)
            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0


            var total = product.cantidad * numero


            val totalModificado = formatter.format(total)

            // Formatear el resultado nuevamente con puntos
            ProductCell(product.productoId, Modifier.weight(0.7f))
            ProductCell(product.nombre, Modifier.weight(2f))
            ProductCell(product.categoria, Modifier.weight(1f))
            ProductCell(product.cantidad.toString(), Modifier.weight(1f))
            ProductCell("$ ${product.precioCompra}", Modifier.weight(1f))
            ProductCell("$ ${totalModificado}", Modifier.weight(1f))
            ProductCell(product.fecha, Modifier.weight(1f))
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProductRowSales(product: Ventas, onClick: () -> Unit) {

    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    false
                },
                onExit = {
                    isHovered = false
                    false
                }
            )
            // Dibuja las líneas solo arriba y abajo
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val color = Color.LightGray
                // Línea superior
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
                // Línea inferior
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(8.dp)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {


            var precioOriginal = product.precioSeleccionado
            var localeCO = java.util.Locale("es", "CO")
            val formatter = NumberFormat.getInstance(localeCO)
            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0


            var total = product.cantidad * numero


            val totalModificado = formatter.format(total)


            ProductCell(product.productoId, Modifier.weight(0.7f))
            ProductCell(product.nombre, Modifier.weight(2f))
            ProductCell(product.categoria, Modifier.weight(1f))
            ProductCell(product.cantidad.toString(), Modifier.weight(1f))
            ProductCell("$ ${product.precioSeleccionado}", Modifier.weight(1f))
            ProductCell("$ ${totalModificado}", Modifier.weight(1f))

            ProductCell(product.fecha, Modifier.weight(1f))
        }
    }
}


//Estilo Celda de cada producto

@Composable
fun ProductCellHome(text: String, modifier: Modifier = Modifier) {
    // Determina el color basado en el texto
    val textColor = when (text) {
        "Inexistente" -> Color.Red
        "Suficiente" -> Color(120, 255, 53)
        "Requerido" -> Color(255, 239, 38)
        "Insuficiente" -> Color(255, 162, 0)
        else -> Color(70, 70, 70)
    }
    val textSize = when (text) {
        "Inexistente" -> 16.sp
        "Suficiente" -> 16.sp
        "Requerido" -> 16.sp
        "Insuficiente" -> 16.sp
        else -> 14.sp
    }

    Text(
        text = text,
        fontSize = textSize,
        fontWeight = if (text == "Inexistente" || text == "Suficiente" || text == "Requerido" || text == "Insuficiente") FontWeight.ExtraBold else FontWeight.Medium,
        color = textColor, // Color principal del texto
        modifier = modifier.padding(8.dp), // Ajusta el padding si es necesario
        textAlign = TextAlign.Center
    )
}@Composable
fun ProductCellStock(text: String, modifier: Modifier = Modifier) {
    // Determina el color basado en el texto

    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight =  FontWeight.Bold,
        color = Color(70, 70, 70), // Color principal del texto
        modifier = modifier.padding(8.dp), // Ajusta el padding si es necesario
        textAlign = TextAlign.Center
    )
}

@Composable
fun ProductCell(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.Medium,
        modifier = modifier.padding(8.dp),
        textAlign = TextAlign.Center,
        color = Color(70, 70, 70),
        fontSize = 14.sp
    )
}

@Composable
fun ShowProductDetails(
    product: Productos,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    productRepository: ProductRepository,
    onDelete: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,

        text = {
            Column(
                modifier = Modifier.padding(16.dp) // Espaciado general dentro del diálogo
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Detalles del Producto",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                            .fillMaxWidth(),// Espacio debajo del título
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.height(40.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nombre:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(20.dp))

                    Text(
                        text = product.nombre,
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre cada fila

                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(20.dp))

                    Text(
                        text = product.categoria,
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio de venta publico:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(20.dp))

                    Text(
                        text = "$ ${product.precioVenta}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio de venta publico para llevar :",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(20.dp))

                    Text(
                        text = "$ ${product.precioVentallevar}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio de venta al por mayor normal:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(20.dp))

                    Text(
                        text = "$ ${product.precioVentaPorMayorClienteNormal}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio de venta al por mayor frecuente:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.width(20.dp))

                    Text(
                        text = "$ ${product.precioVentaPorMayorClienteFrecuente}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Se requiere en Stock:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.width(20.dp))


                    Text(
                        text = "${product.desde} - ${product.hasta}",
                        style = MaterialTheme.typography.body2
                    )
                }


                Spacer(modifier = Modifier.height(60.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Bottom

                    ) {
                        TextButton(
                            onClick = onDelete,
                            modifier = Modifier.padding(8.dp), // Añadir padding al botón
                        ) {
                            Text(
                                "Eliminar",
                                color = Color.Red
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End
                    ) {

                        TextButton(
                            onClick = onEdit,
                            modifier = Modifier.padding(8.dp) // Añadir padding al botón
                        ) {
                            Text("Editar")
                        }
                        TextButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(8.dp) // Añadir padding al botón
                        ) {
                            Text("Cerrar")
                        }


                    }
                }
            }
        },
        confirmButton = { null },
        dismissButton = { null }
    )

}

@Composable
fun ShowProductPurchasesDetails(product: Compras, onDismiss: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Detalles de la Compra",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp) // Espacio debajo del título
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp) // Espaciado general dentro del diálogo
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nombre:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.nombre,
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre cada fila

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.categoria,
                        style = MaterialTheme.typography.body2
                    )
                }



                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio de compra:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$ ${product.precioCompra}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Cantidad de compras:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$ ${product.cantidad}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp) // Añadir padding al botón
            ) {
                Text("Cerrar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDelete()

                },
                modifier = Modifier.padding(8.dp) // Añadir padding al botón
            ) {
                Text(
                    "Eliminar",
                    color = Color.Red
                )
            }
        }


    )

}

@Composable
fun ShowProductSalesDetails(product: Ventas, onDismiss: () -> Unit, onDelete: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Detalles de la Venta",
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(bottom = 8.dp) // Espacio debajo del título
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(16.dp) // Espaciado general dentro del diálogo
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Nombre:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.nombre,
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Espacio entre cada fila

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.categoria,
                        style = MaterialTheme.typography.body2
                    )
                }



                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Precio de venta:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$ ${product.precioSeleccionado}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Cantidad de ventas:",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${product.cantidad}",
                        style = MaterialTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(8.dp) // Añadir padding al botón
            ) {
                Text("Cerrar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDelete,
                modifier = Modifier.padding(8.dp) // Añadir padding al botón
            ) {
                Text(
                    "Eliminar",
                    color = Color.Red
                )
            }
        }
    )

}

@Composable
fun capitalizeFirstLetter(text: String): String {
    return text.replaceFirstChar { it.uppercase() }
}