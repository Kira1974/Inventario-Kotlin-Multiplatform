package com.johan.inventario.Model

import appDataBase.ProductDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class ProductRepository(private val productDao: ProductDao) {

    // Guardar varios productos y retornar un Flow
    fun guardarProductos(productos: List<Productos>): Flow<Unit> = flow {
        productDao.insertProducts(productos)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Guardar un único producto y retornar un Flow
    fun guardarProducto(producto: Productos): Flow<Unit> = flow {
        productDao.insertProduct(producto)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Obtener productos como Flow
    fun obtenerProductos(): Flow<List<Productos>> {
        return productDao.getAllAsFlow()
    }

    // Eliminar producto por criterio y retornar un Flow
    fun eliminarProductoPorCriterio(nombre: String, precioVenta: String, ventas: Int): Flow<Unit> = flow {
        productDao.deleteProductByCriteria(nombre, precioVenta, ventas)
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Actualizar producto y retornar un Flow
    fun actualizarProducto(producto: Productos): Flow<Unit> = flow {
        productDao.updateProduct(producto) // Actualiza el producto existente
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

    // Eliminar un producto y retornar un Flow
    fun eliminarProducto(producto: Productos): Flow<Unit> = flow {
        productDao.deleteProduct(producto) // Elimina el producto existente
        emit(Unit) // Emitimos un valor vacío para indicar que la operación se completó
    }.flowOn(Dispatchers.IO) // Ejecutar la lógica en Dispatchers.IO

}