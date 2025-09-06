package com.johan.inventario.Model
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

    @Entity(tableName = "productos")
    data class Productos(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        var codigo: String ,
        var nombre: String,
        var categoria: String,
        var comprasActuales: Int,
        var ventas: Int,
        var stock: Int,
        var precioCompra: String,
        var precioSeleccionado: String,
        var precioVenta: String,
        var precioVentallevar: String,
        var precioVentaPorMayorClienteNormal: String,
        var precioVentaPorMayorClienteFrecuente: String,
        var desde: String,
        var hasta: String,
        var estado: String
    )

@Entity(
    tableName = "ventas",

)
data class Ventas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var productoId: String,            // ID del producto, que será la clave foránea
    var nombre: String,             // Nombre del producto
    var categoria: String,          // Categoría del producto
    var cantidad: Int,              // Cantidad vendida
    var precioSeleccionado: String, // Precio al que se vendió
    var fecha: String                // Fecha de la venta
)


@Entity(
    tableName = "compras",
)
data class Compras(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var productoId: String,            // ID del producto, que será la clave foránea
    var nombre: String,             // Nombre del producto
    var categoria: String,          // Categoría del producto
    var cantidad: Int,              // Cantidad comprada
    var precioCompra: String,       // Precio por el que se compró
    var fecha: String                // Fecha de la compra
)
