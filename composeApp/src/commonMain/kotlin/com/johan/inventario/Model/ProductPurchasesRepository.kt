package com.johan.inventario.Model

import appDataBase.PurchasesDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ProductPurchasesRepository(private val purchasesDao: PurchasesDao) {

    // Guardar una compra y retornar un Flow
    fun guardarCompra(compra: Compras): Flow<Unit> = flow {
        purchasesDao.insertPurchase(compra)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Guardar varias compras y retornar un Flow
    fun guardarCompras(compras: List<Compras>): Flow<Unit> = flow {
        purchasesDao.insertPurchases(compras)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Obtener compras como Flow
    fun obtenerCompras(): Flow<List<Compras>> {
        return purchasesDao.getAllAsFlow()
    }

    // Obtener compras por producto y retornar un Flow
    fun obtenerComprasPorProducto(productoId: Int): Flow<List<Compras>> = flow {
        val compras = purchasesDao.getComprasByProductoId(productoId)
        emit(compras) // Emitimos la lista de compras
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    fun eliminarCompra(compra:Compras): Flow<Unit> = flow {
        purchasesDao.deleteCompra(compra)
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    // Actualizar una compra y retornar un Flow
    fun actualizarCompra(compra: Compras): Flow<Unit> = flow {
        purchasesDao.updateCompra(compra) // Actualiza la compra existente
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO
}
