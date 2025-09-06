package com.johan.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun obtenerFechaActual(): Date {
    return Date()
}

fun calcularVentasDia(productStateHolderSales: ProductStateHolder): String? {
    val hoy = SimpleDateFormat("yyyy-MM-dd").format(obtenerFechaActual())
    var totalSalesDay = 0
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    for (producto in productStateHolderSales.salesList) {
        if (producto.fecha == hoy) {
            val precioOriginal = producto.precioSeleccionado

            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesDay += venta
        }
    }
    val total = formatter.format(totalSalesDay)
    return total
}

fun calcularVentasSemana(productStateHolderSales: ProductStateHolder): String? {
    val calendar = Calendar.getInstance()
    calendar.time = obtenerFechaActual()

    // Ajustar el calendario al inicio de la semana (lunes)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startOfWeek = calendar.time

    calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMaximum(Calendar.DAY_OF_WEEK))

    val endOfWeek = calendar.time

    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    var totalSalesWeek = 0
    for (producto in productStateHolderSales.salesList) {
        val productDate = SimpleDateFormat("yyyy-MM-dd").parse(producto.fecha)
        if (productDate != null && productDate in startOfWeek..endOfWeek) {
            val precioOriginal = producto.precioSeleccionado

            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesWeek += venta
        }
    }
    val total = formatter.format(totalSalesWeek)
    return total
}


fun calcularVentasMes(productStateHolderSales: ProductStateHolder): String? {
    val calendar = Calendar.getInstance()
    calendar.time = obtenerFechaActual()

    // Ajusta al primer y último día del mes
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startOfMonth = calendar.time
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    val endOfMonth = calendar.time
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)

    var totalSalesMonth = 0
    for (producto in productStateHolderSales.salesList) {
        val productDate = SimpleDateFormat("yyyy-MM-dd").parse(producto.fecha)
        if (productDate != null && productDate in startOfMonth..endOfMonth) {
            val precioOriginal = producto.precioSeleccionado
            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesMonth += venta
        }
    }
    val total = formatter.format(totalSalesMonth)
    return total
}


fun calcularVentasAno(productStateHolderSales: ProductStateHolder): String? {
    val calendar = Calendar.getInstance()
    calendar.time = obtenerFechaActual()

    // Ajusta al primer y último día del año
    calendar.set(Calendar.DAY_OF_YEAR, 1)
    val startOfYear = calendar.time
    calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
    val endOfYear = calendar.time
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    var totalSalesYear = 0

    for (producto in productStateHolderSales.salesList) {
        val productDate = SimpleDateFormat("yyyy-MM-dd").parse(producto.fecha)
        if (productDate != null && productDate in startOfYear..endOfYear) {
            val precioOriginal = producto.precioSeleccionado

            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesYear += venta
        }
    }
    val total = formatter.format(totalSalesYear)
    return total
}

fun calcularComprasDia(productStateHolderPurchases: ProductStateHolder): String? {
    val hoy = SimpleDateFormat("yyyy-MM-dd").format(obtenerFechaActual())
    var totalSalesDay = 0
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    for (producto in productStateHolderPurchases.purchasesList) {
        if (producto.fecha == hoy) {
            val precioOriginal = producto.precioCompra

            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesDay += venta
        }
    }
    val total = formatter.format(totalSalesDay)
    return total
}

fun calcularComprasSemana(productStateHolderPurchases: ProductStateHolder): String? {
    val calendar = Calendar.getInstance()
    calendar.time = obtenerFechaActual()

    // Ajustar el calendario al inicio de la semana (lunes)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    val startOfWeek = calendar.time

    // Ajustar el calendario al final de la semana (domingo)
    calendar.add(Calendar.DAY_OF_WEEK, 6)
    val endOfWeek = calendar.time
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    var totalSalesWeek = 0

    for (producto in productStateHolderPurchases.purchasesList) {

            val productDate = SimpleDateFormat("yyyy-MM-dd").parse(producto.fecha)
            if (productDate != null && productDate.after(startOfWeek) && productDate.before(endOfWeek)) {
                val precioOriginal = producto.precioCompra

                val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
                val venta = producto.cantidad * numero
                totalSalesWeek += venta
            }
    }

    return formatter.format(totalSalesWeek)
}



fun calcularComprasMes(productStateHolderPurchases: ProductStateHolder): String? {
    val calendar = Calendar.getInstance()
    calendar.time = obtenerFechaActual()

    // Ajusta al primer y último día del mes
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startOfMonth = calendar.time
    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    val endOfMonth = calendar.time
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)

    var totalSalesMonth = 0
    for (producto in productStateHolderPurchases.purchasesList) {
        val productDate = SimpleDateFormat("yyyy-MM-dd").parse(producto.fecha)
        if (productDate != null && productDate in startOfMonth..endOfMonth) {
            val precioOriginal = producto.precioCompra
            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesMonth += venta
        }
    }
    val total = formatter.format(totalSalesMonth)
    return total
}


fun calcularComprasAno(productStateHolderPurchases: ProductStateHolder): String? {
    val calendar = Calendar.getInstance()
    calendar.time = obtenerFechaActual()

    // Ajusta al primer y último día del año
    calendar.set(Calendar.DAY_OF_YEAR, 1)
    val startOfYear = calendar.time
    calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR))
    val endOfYear = calendar.time
    val localeCO = Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    var totalSalesYear = 0

    for (producto in productStateHolderPurchases.purchasesList) {
        val productDate = SimpleDateFormat("yyyy-MM-dd").parse(producto.fecha)
        if (productDate != null && productDate in startOfYear..endOfYear) {
            val precioOriginal = producto.precioCompra

            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            val venta = producto.cantidad * numero
            totalSalesYear += venta
        }
    }
    val total = formatter.format(totalSalesYear)
    return total
}


@Composable
fun CustomDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedYear by remember { mutableStateOf(currentYear) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedDay by remember { mutableStateOf(String.format("%02d", currentDay)) }

    val days = (1..31).map { String.format("%02d", it) }
    val months = listOf(
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    )
    val years = (2024..2040).toList()

    // Estado de LazyColumn para permitir el desplazamiento infinito
    val yearState = rememberLazyListState(initialFirstVisibleItemIndex = years.indexOf(currentYear))
    val monthState = rememberLazyListState(initialFirstVisibleItemIndex = currentMonth - 1)
    val dayState = rememberLazyListState(initialFirstVisibleItemIndex = currentDay - 1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                // Año
                DatePickerColumn(
                    items = years,
                    selectedItem = selectedYear,
                    onItemSelected = { selectedYear = it },
                    label = "Año",
                    listState = yearState
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Mes
                DatePickerColumn(
                    items = months,
                    selectedItem = months[selectedMonth - 1],
                    onItemSelected = { selectedMonth = months.indexOf(it) + 1 },
                    label = "Mes",
                    listState = monthState
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Día
                DatePickerColumn(
                    items = days,
                    selectedItem = selectedDay,
                    onItemSelected = { selectedDay = it },
                    label = "Dia",
                    listState = dayState
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val selectedDate = "$selectedYear-$selectedMonth-$selectedDay"
                    onDateSelected(selectedDate)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF007AFF))
            ) {
                Text("Listo", color = Color.White)
            }
        }
    }
}

// Función para el estilo del encabezado
@Composable
fun HeaderTextt(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 14.sp,
        modifier = modifier.padding(8.dp),
        fontWeight = FontWeight.SemiBold,
        color = Color.Black,
        textAlign = TextAlign.Center
    )
}

// Función para el contenido de cada celda
@Composable
fun CellText(value: String, modifier: Modifier = Modifier) {
    Text(
        text = value,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(8.dp),
        color = Color.DarkGray,
        textAlign = TextAlign.Center
    )
}
@Composable
fun <T> DatePickerColumn(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    label: String,
    listState: LazyListState
) {
    Column(
        horizontalAlignment = Alignment.Start, // Asegura que la columna esté centrada
        modifier = Modifier.width(150.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,

            color = Color.DarkGray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            textAlign = TextAlign.Start // Centra el texto
        )
        LazyColumn(
            modifier = Modifier
                .height(120.dp) // Ajusta la altura para hacerlo más compacto
                .background(Color.Transparent),
            state = listState,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center, // Centra los elementos en el eje vertical
        ) {
            itemsIndexed(items) { index, item ->
                Text(
                    text = item.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item == selectedItem) Color(0xFF007AFF) else Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            onItemSelected(item)
                        }
                        .align(Alignment.Start)
                        .wrapContentWidth(Alignment.Start) // Centra los elementos dentro de la lista
                )
            }
        }
    }
}