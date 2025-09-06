package com.johan.inventario.Model


import appDataBase.SalesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ProductSalesRepository(private val salesDao: SalesDao) {

    // Guardar una venta y retornar un Flow
    fun guardarVenta(venta: Ventas): Flow<Unit> = flow {
        salesDao.insertSale(venta)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Guardar varias ventas y retornar un Flow
    fun guardarVentas(ventas: List<Ventas>): Flow<Unit> = flow {
        salesDao.insertSales(ventas)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Obtener ventas como Flow
    fun obtenerVentas(): Flow<List<Ventas>> {
        return salesDao.getAllAsFlow()
    }

    // Obtener ventas por producto y retornar un Flow
    fun obtenerVentasPorProducto(productoId: Int): Flow<List<Ventas>> = flow {
        val ventas = salesDao.getVentasByProductoId(productoId)
        emit(ventas) // Emitimos la lista de ventas
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    fun actualizarVenta(venta: Ventas): Flow<Unit> = flow {
        salesDao.updateVenta(venta ) // Actualiza el producto existente
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    fun eliminarVenta(venta: Ventas): Flow<Unit> = flow {
        salesDao.deleteVenta(venta)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    // Eliminar todas las ventas y retornar un Flow
    fun eliminarTodasLasVentas(): Flow<Unit> = flow {
        salesDao.deleteAllVentas()
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO
}
