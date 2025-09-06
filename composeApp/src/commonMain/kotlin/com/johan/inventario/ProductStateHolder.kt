package com.johan.inventario

import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas

class ProductStateHolder {

    // Lista mutable que será compartida entre todas las pantallas
    private val _productList = mutableListOf<Productos>()
    val productList: List<Productos> get() = _productList

   private val _purchasesList = mutableListOf<Compras>()
    val purchasesList: List<Compras> get() = _purchasesList

   private val _salesList = mutableListOf<Ventas>()
    val salesList: List<Ventas> get() = _salesList


    // Función para agregar un producto
    fun addProduct(product: Productos) {
        _productList.add(product)
    }
   fun addPurchase(compra: Compras) {
        _purchasesList.add(compra)
    }
   fun addSale(venta: Ventas) {
        _salesList.add(venta)
    }
    fun deletePurchase(compra: Compras) {
        _purchasesList.remove(compra)
    }
    fun deleteSale(venta: Ventas){
        _salesList.remove(venta)
    }
    // Función para reemplazar toda la lista
    fun replaceProducts(newProducts: List<Productos>) {
        _productList.clear() // Limpiamos la lista actual
        _productList.addAll(newProducts) // Agregamos todos los productos nuevos
    }
    fun replacePurchases(newPurchases: List<Compras>) {
        _purchasesList.clear() // Limpiamos la lista actual
        _purchasesList.addAll(newPurchases) // Agregamos todos los productos nuevos
    }
    fun replaceSales(newSales: List<Ventas>) {
        _salesList.clear() // Limpiamos la lista actual
        _salesList.addAll(newSales) // Agregamos todos los productos nuevos
    }
    // Otras funciones para modificar productos
    fun updateProduct(updatedProduct: Productos) {
        val index = _productList.indexOfFirst { it.codigo == updatedProduct.codigo }
        if (index != -1) {
            _productList[index] = updatedProduct
        }
    }
    fun updatePurchase(updatedPurchase: Compras) {
        val index = _purchasesList.indexOfFirst { it.id == updatedPurchase.id }
        if (index != -1) {
            _purchasesList[index] = updatedPurchase
        }
    }
    fun updateSales(updatedSales: Ventas) {
        val index = _salesList.indexOfFirst { it.id == updatedSales.id }
        if (index != -1) {
            _salesList[index] = updatedSales
        }
    }


}
