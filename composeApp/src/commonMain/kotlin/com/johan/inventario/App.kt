// Importa todas las librerías necesarias
import  androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import appDataBase.AppDatabase
import appDataBase.DBBackupHelper
import appDataBase.DBFactory
import com.johan.inventario.AddHomeProducts
import com.johan.inventario.AddPurchasesProducts
import com.johan.inventario.AddSalesProducts
import com.johan.inventario.CellText
import com.johan.inventario.ComunScreen
import com.johan.inventario.CustomDatePicker
import com.johan.inventario.EditProduct
import com.johan.inventario.HeaderText
import com.johan.inventario.HeaderTextt
import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.ProductPurchasesRepository
import com.johan.inventario.Model.ProductRepository
import com.johan.inventario.Model.ProductSalesRepository
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas
import com.johan.inventario.ProductRowHome
import com.johan.inventario.ProductRowPurchases
import com.johan.inventario.ProductRowSales
import com.johan.inventario.ProductStateHolder
import com.johan.inventario.ShowProductDetails
import com.johan.inventario.ShowProductPurchasesDetails
import com.johan.inventario.ShowProductSalesDetails
import com.johan.inventario.calcularVentasDia
import inventario.composeapp.generated.resources.Res
import inventario.composeapp.generated.resources.backup4
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.text.NumberFormat


//Desde aca vamos  a navegar a  las tres distintas pantallas
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun App(db: AppDatabase, dbFactory: DBFactory) {
    
    val navController = rememberNavController()
    // Crear el repositorio de productos
    val productRepository = remember { ProductRepository(db.productDao()) }
    val purchasesRepository = remember { ProductPurchasesRepository(db.purchasesDao()) }
    val salesRepository = remember { ProductSalesRepository(db.salesDao()) }

    // Crear los ProductStateHolders con el repositorio
    val productStateHolder = remember { ProductStateHolder() }
    val productStateHolderPurchases = remember { ProductStateHolder() }
    val productStateHolderSales = remember { ProductStateHolder() }

    val productosInventario by productRepository.obtenerProductos()
        .collectAsState(initial = emptyList())

    val compras by purchasesRepository.obtenerCompras().collectAsState(initial = emptyList())

    val ventas by salesRepository.obtenerVentas().collectAsState(initial = emptyList())

    productStateHolder.apply { replaceProducts(productosInventario) }
    productStateHolderPurchases.apply { replacePurchases(compras) }
    productStateHolderSales.apply { replaceSales(ventas) }

    val dbBackupHelper = DBBackupHelper(dbFactory)


    Row(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        horizontalArrangement = Arrangement.Start
    ) {


        Image(
            painterResource(Res.drawable.backup4),
            contentDescription = "Copia de seguridad",
            modifier = Modifier
                .size(width = 40.dp, height = 45.dp).onClick {
                    dbBackupHelper.backupDatabase()
                }
        )
    }


    NavHost(navController, startDestination = "home") {
        composable("home",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { -it })
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { -it })
            }) {
            HomeScreen(
                navController,
                productStateHolderPurchases,
                productStateHolderSales,
                productStateHolder,
                productRepository,
                purchasesRepository,
                salesRepository
            )
        }
        composable("purchases",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { it })
            }) {
            PurchasesScreen(
                navController,
                productStateHolder,
                productStateHolderPurchases,
                purchasesRepository,
                productRepository
            )
        }
        composable("sales",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { it })
            }) {
            SalesScreen(
                navController,
                productStateHolder,
                productStateHolderSales,
                salesRepository,
                productRepository
            )
        }
        composable("resume",
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(500),
                    initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(500),
                    targetOffsetX = { it })
            }) {
            ResumeScreen(
                navController,
                productStateHolder,
                productStateHolderSales,
                productStateHolderPurchases,
                salesRepository,
                productRepository
            )
        }

    }
}

//Pantalla Principal
@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    productStateHolderPurchases: ProductStateHolder,
    productStateHolderSales: ProductStateHolder,
    productStateHolder: ProductStateHolder,
    productRepository: ProductRepository,
    purchasesRepository: ProductPurchasesRepository,
    salesRepository: ProductSalesRepository


) {
    val visiblePrices = remember { mutableStateMapOf<Int, Boolean>() }


    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showNeedPassword by remember { mutableStateOf(false) }
    var showNeedPasswordPurchases by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") } // Estado para la búsqueda
    var openEyes by remember { mutableStateOf(false) } // Estado para la búsqueda

    //para editar
    var showEditDialog by remember { mutableStateOf(false) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val products =  productStateHolder.productList



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar la pantalla común en la parte superior
        ComunScreen(navController,
            "Inventario",
            Color.White,
            Color.White,
            Color.White,
            false,
            true,
            { showAddDialog = true },
            ShowNeedPassword = { showNeedPassword = true },
            toggleOpenEyes = { openEyes = !openEyes },
            areOpenEyes = openEyes,
            onSearchQueryChange = { searchQuery = it },
            ShowNeedPasswordPurchases = {showNeedPasswordPurchases = true}
        )

        // Mostrar el diálogo para agregar productos
        AddHomeProducts(
            showAddProductDialog = showAddDialog,
            onDismiss = { showAddDialog = false },
            productStateHolder,
            productRepository


        )

        if(!openEyes){
            products.forEach { product ->
                visiblePrices[product.id] = false
            }
            }
        // Mostrar la tabla de productos debajo de los demás elementos
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .background(Color(240, 240, 240))
                .padding(vertical = 8.dp)
        ) {
            // Encabezados de la tabla
            val tituloColumnas = listOf(
                HeaderText("ID", Modifier.weight(0.7f)),
                HeaderText("Producto", Modifier.weight(2f)),
                HeaderText("Categoria", Modifier.weight(1f)),
                HeaderText("Compras", Modifier.weight(1f)),
                HeaderText("Ventas", Modifier.weight(1f)),
                HeaderText("Stock", Modifier.weight(1f)),
                HeaderText("Precio", Modifier.weight(1f)),
                HeaderText("Estado", Modifier.weight(1f))
            )
        }

        var password by remember { mutableStateOf("") }

        var selectedProduct by remember { mutableStateOf<Productos?>(null) }


        selectedProduct?.let { product ->
            showDetailsDialog = true
            if (showDetailsDialog) {
                ShowProductDetails(
                    product = product,
                    onDismiss = {
                        showDetailsDialog = false
                        selectedProduct = null
                    },
                    onEdit = {
                        // Cerramos los detalles
                        showEditDialog = true // Abrimos el diálogo de edición
                    },
                    productRepository,
                    onDelete = {
                        showDeleteDialog = true
                    },


                    )
            }
            if (showEditDialog) {
                EditProduct(
                    producto = product,
                    productStateHolder = productStateHolder,
                    showAddDialog = showEditDialog,
                    onDismiss = {
                        selectedProduct = null
                        showEditDialog = false
                    },
                    productRepository = productRepository
                )
            }
            if (showDeleteDialog) {
                AlertDialog(

                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(text = "Advertencia")
                    },
                    text = {
                        Text("¿Estas seguro de que deseas eliminar el producto del inventario?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                coroutineScope.launch {
                                    productRepository.eliminarProducto(product).collect {}
                                }
                                productStateHolder.productList.forEach { producto ->
                                    if (producto.codigo.toInt() > product.codigo.toInt()) {
                                        val newCodigo = producto.codigo.toInt() - 1
                                        producto.codigo = newCodigo.toString()
                                    }
                                    productStateHolder.updateProduct(producto)
                                    coroutineScope.launch {
                                        productRepository.actualizarProducto(producto).collect {}
                                    }
                                    productStateHolderPurchases.purchasesList.forEach { purchase ->
                                        if (purchase.nombre == producto.nombre) {
                                            purchase.productoId = producto.codigo
                                        }
                                        productStateHolderPurchases.updatePurchase(purchase)
                                        coroutineScope.launch {
                                            purchasesRepository.actualizarCompra(purchase)
                                                .collect {}
                                        }

                                        productStateHolderSales.salesList.forEach { sale ->
                                            if (sale.nombre == producto.nombre) {
                                                sale.productoId = producto.codigo
                                            }
                                            productStateHolderSales.updateSales(sale)
                                            coroutineScope.launch {
                                                salesRepository.actualizarVenta(sale).collect {}
                                            }

                                        }
                                    }
                                }

                                selectedProduct = null

                            }

                        ) {
                            Text("Si")
                        }

                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false

                            }
                        ) {

                            Text("Cancelar")
                        }
                    }
                )
            }


        }

        // Si el estado indica que debemos mostrar el diálogo de edición

        val priorityMap = mapOf(
            "Inexistente" to 1,
            "Insuficiente" to 2,
            "Requerido" to 3,
            "Suficiente" to 4
        )
        val sortedProducts = products.sortedWith(compareBy {
            priorityMap[it.estado] ?: Int.MAX_VALUE // Int.MAX_VALUE para estados no definidos
        })
// Filtrar productos si hay búsqueda
        val filteredProducts = if (searchQuery.isBlank()) {
            sortedProducts // Si la búsqueda está vacía, mostrar la lista ordenada por estado
        } else {
            // Dividir el searchQuery en palabras individuales y convertir todo a minúsculas
            val searchWords = searchQuery.split(" ").map { it.trim().lowercase() }

            sortedProducts.filter { product ->
                val productName = product.nombre.lowercase()
                val productCode = product.codigo

                // Verifica si todas las palabras del searchQuery están contenidas en el nombre del producto
                searchWords.all { searchWord ->
                    productName.contains(searchWord) || productCode.contains(searchWord)
                }
            }
        }

// Lista de productos
        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.9f),
            contentPadding = PaddingValues(1.dp),
        ) {
            items(filteredProducts) { product ->
                ProductRowHome(
                    product,
                    onClick = { selectedProduct = product },
                    showRealPrice = visiblePrices[product.id] == true // Controlar visibilidad
                )
            }
        }



        if (products.isEmpty()) {
            Text("No hay productos agregados", modifier = Modifier.padding(16.dp))
        }

        if (showNeedPassword) {
            AlertDialog(
                onDismissRequest = { showNeedPassword = false },
                title = {  Text(
                    text = "Escribir contraseña",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
                )},

                text = {
                    // Variable para la contraseña ingresadat

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Contraseña") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(), // Oculta el texto ingresado
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Contraseña"
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.Transparent,
                                cursorColor = Color.Black,
                                focusedIndicatorColor = Color.Black,
                                unfocusedIndicatorColor = Color.Gray
                            ),
                            modifier = Modifier
                                .fillMaxWidth(0.8f) // Ajustar el tamaño del TextField
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (password == "1983") {
                                openEyes = true
                                products.forEach { product ->
                                    visiblePrices[product.id] = true
                                }
                            }
                            password = ""
                            showNeedPassword = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.2f).align(Alignment.CenterHorizontally) // Ajustar tamaño del botón
                    ) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showNeedPassword = false
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.2f) // Ajustar tamaño del botón
                    ) {
                        Text("Cancelar")
                    }
                }
            )


        }
        PasswordDialog( showDialog = showNeedPasswordPurchases,
            onDismiss = { showNeedPasswordPurchases = false },
            onConfirm = { password ->
                if (password == "1983") {
                    navController.navigate("purchases")
                }
                showNeedPasswordPurchases = false
            }
        )


    }

}

// Pantalla de Compras
@Composable
fun PurchasesScreen(
    navController: NavHostController,
    productStateHolder: ProductStateHolder,
    productStateHolderPurchases: ProductStateHolder,
    purchasesRepository: ProductPurchasesRepository,
    productRepository: ProductRepository

) {

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showNeedPasswordPurchases by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var purchases = productStateHolderPurchases.purchasesList
    var searchQuery by remember { mutableStateOf("") } // Estado para la búsqueda
    var selectedProduct by remember { mutableStateOf<Compras?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComunScreen(navController,
            "Compras",
            Color(249, 109, 109),
            Color.White,
            Color.White,
            true,
            true,
            { showAddDialog = true },
            onSearchQueryChange = { searchQuery = it }, ShowNeedPassword = {}, toggleOpenEyes = {false} , areOpenEyes = false , ShowNeedPasswordPurchases = {showNeedPasswordPurchases = true})


                    //Funcion del boton de agregar productos
        AddPurchasesProducts(
            showAddProductDialog = showAddDialog,
            onDismiss = { showAddDialog = false },
            productStateHolder,
            productStateHolderPurchases,
            purchasesRepository,
            productRepository

        )

        // Mostrar la tabla de productos debajo de los demás elementos
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .background(Color(240, 240, 240))
                .padding(vertical = 8.dp)
        ) {
            // Encabezados de la tabla
            val tituloColumnas = listOf(
                HeaderText("Código", Modifier.weight(0.7f)),
                HeaderText("Producto", Modifier.weight(2f)),
                HeaderText("Categoria", Modifier.weight(1f)),
                HeaderText("Compras", Modifier.weight(1f)),
                HeaderText("Precio", Modifier.weight(1f)),
                HeaderText("Total", Modifier.weight(1f)),
                HeaderText("Fecha", Modifier.weight(1f))
            )
        }

        // Mostrar el diálogo si hay un producto seleccionado
        selectedProduct?.let { purchase ->
            ShowProductPurchasesDetails(purchase,
                { selectedProduct = null },
                { showDeleteDialog = true })

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(text = "Advertencia")
                    },
                    text = {
                        Text("¿Estas seguro de que deseas eliminar el producto del inventario?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false
                                productStateHolder.productList.forEach { producto ->
                                    if (producto.codigo == purchase.productoId) {
                                        producto.comprasActuales -= purchase.cantidad
                                        producto.stock = producto.comprasActuales - producto.ventas
                                        val currentEntries = producto.stock
                                        val minValue = producto.desde.toIntOrNull()
                                        val maxValue = producto.hasta.toIntOrNull()

                                        // Verificamos que currentEntries, minValue y maxValue no sean nulos
                                        if (currentEntries != null && minValue != null && maxValue != null) {
                                            producto.estado = when {
                                                currentEntries in minValue..maxValue -> "Requerido"
                                                currentEntries < minValue && currentEntries > 0 -> "Insuficiente"
                                                currentEntries == 0 -> "Inexistente"
                                                else -> "Suficiente"
                                            }
                                        } else {
                                            // Manejo de error si alguna de las entradas es nula
                                            producto.estado = "N/P"
                                        }
                                        coroutineScope.launch {
                                            productRepository.actualizarProducto(producto)
                                                .collect {}

                                        }
                                    }
                                }
                                coroutineScope.launch {
                                    purchasesRepository.eliminarCompra(purchase).collect {}

                                }
                                selectedProduct = null

                            }

                        ) {
                            Text("Si")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false

                            }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
        val displayedProducts = purchases.reversed()

        // Filtrar productos si hay búsqueda
        val filteredProducts = if (searchQuery.isBlank()) {
            displayedProducts // Si la búsqueda está vacía, mostrar la lista ordenada por estado
        } else {
            // Dividir el searchQuery en palabras individuales y convertir todo a minúsculas
            val searchWords = searchQuery.split(" ").map { it.trim().lowercase() }

            displayedProducts.filter { product ->
                val productName = product.nombre.lowercase()
                val productDate = product.fecha.lowercase()

                // Verifica si todas las palabras del searchQuery están contenidas en el nombre o fecha del producto
                searchWords.all { searchWord ->
                    productName.contains(searchWord) || productDate.contains(searchWord)
                }
            }
        }

// Lista de productos
        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.9f),
            contentPadding = PaddingValues(1.dp),
        ) {
            items(filteredProducts) { purchase ->
                ProductRowPurchases(purchase) { selectedProduct = purchase }
            }
        }


        // Mostrar un mensaje si no hay productos agregados
        if (purchases.isEmpty()) {
            Text("No hay productos agregados", modifier = Modifier.padding(16.dp))
        }
    }
    PasswordDialog( showDialog = showNeedPasswordPurchases,
        onDismiss = { showNeedPasswordPurchases = false },
        onConfirm = { password ->
            if (password == "1983") {
                navController.navigate("purchases")
            }
            showNeedPasswordPurchases = false
        }
    )
}

// Pantalla de Ventas
@Composable
fun SalesScreen(
    navController: NavHostController,
    productStateHolder: ProductStateHolder,
    productStateHolderSales: ProductStateHolder,
    salesRepository: ProductSalesRepository,
    productRepository: ProductRepository

) {
    var showNeedPasswordPurchases by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showAddDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") } // Estado para la búsqueda
    val sales = productStateHolderSales.salesList
    var selectedProduct by remember { mutableStateOf<Ventas?>(null) }

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ComunScreen(navController,
            "Ventas",
            Color.White,
            Color(127, 182, 198),
            Color.White,
            true,
            true,
            { showAddDialog = true },
            onSearchQueryChange = { searchQuery = it },ShowNeedPassword = {}, toggleOpenEyes = {false} , areOpenEyes = false, ShowNeedPasswordPurchases = {showNeedPasswordPurchases = true} )

        AddSalesProducts(
            showAddProductDialog = showAddDialog,
            onDismiss = { showAddDialog = false },
            productStateHolder,
            productStateHolderSales,
            salesRepository,
            productRepository

        )
        // Mostrar la tabla de productos debajo de los demás elementos
        Row(
            Modifier
                .fillMaxWidth(0.9f)
                .background(Color(240, 240, 240))
                .padding(vertical = 8.dp)
        ) {
            // Encabezados de la tabla
            val tituloColumnas = listOf(
                HeaderText("Código", Modifier.weight(0.7f)),
                HeaderText("Producto", Modifier.weight(2f)),
                HeaderText("Categoria", Modifier.weight(1f)),
                HeaderText("Ventas", Modifier.weight(1f)),
                HeaderText("Precio", Modifier.weight(1f)),
                HeaderText("Total", Modifier.weight(1f)),
                HeaderText("Fecha", Modifier.weight(1f)),

                )
        }

        // Mostrar el diálogo si hay un producto seleccionado
        selectedProduct?.let { sale ->
            ShowProductSalesDetails(sale,
                { selectedProduct = null },
                { showDeleteDialog = true })
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(text = "Advertencia")
                    },
                    text = {
                        Text("¿Estas seguro de que deseas eliminar el producto del inventario?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false

                                productStateHolder.productList.forEach { producto ->
                                    if (producto.codigo == sale.productoId) {
                                        producto.ventas -= sale.cantidad
                                        producto.stock = producto.comprasActuales - producto.ventas
                                        val currentEntries = producto.stock
                                        val minValue = producto.desde.toIntOrNull()
                                        val maxValue = producto.hasta.toIntOrNull()

                                        // Verificamos que currentEntries, minValue y maxValue no sean nulos
                                        if (currentEntries != null && minValue != null && maxValue != null) {
                                            producto.estado = when {
                                                currentEntries in minValue..maxValue -> "Requerido"
                                                currentEntries < minValue && currentEntries > 0 -> "Insuficiente"
                                                currentEntries == 0 -> "Inexistente"
                                                else -> "Suficiente"
                                            }
                                        } else {
                                            // Manejo de error si alguna de las entradas es nula
                                            producto.estado = "N/P"
                                        }
                                        coroutineScope.launch {
                                            productRepository.actualizarProducto(
                                                producto
                                            ).collect {}
                                        }
                                    }
                                }
                                coroutineScope.launch {
                                    salesRepository.eliminarVenta(sale).collect {

                                    }
                                }
                                selectedProduct = null
                            }

                        ) {
                            Text("Si")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDeleteDialog = false

                            }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }

        }
        val displayedProducts = sales.reversed()
// Filtrar productos si hay búsqueda
        val filteredProducts = if (searchQuery.isBlank()) {
            displayedProducts // Si la búsqueda está vacía, mostrar la lista ordenada por estado
        } else {
            // Dividir el searchQuery en palabras individuales y convertir todo a minúsculas
            val searchWords = searchQuery.split(" ").map { it.trim().lowercase() }

            displayedProducts.filter { product ->
                val productName = product.nombre.lowercase()
                val productDate = product.fecha.lowercase()

                // Verifica si todas las palabras del searchQuery están contenidas en el nombre o la fecha del producto
                searchWords.all { searchWord ->
                    productName.contains(searchWord) || productDate.contains(searchWord)
                }
            }
        }

// Lista de productos
        LazyColumn(
            modifier = Modifier.fillMaxWidth(0.9f),
            contentPadding = PaddingValues(1.dp),
        ) {
            items(filteredProducts) { sale ->
                ProductRowSales(sale) { selectedProduct = sale }
            }
        }


        // Mostrar un mensaje si no hay productos agregados
        if (sales.isEmpty()) {
            Text("No hay productos agregados", modifier = Modifier.padding(16.dp))
        }


    }
    PasswordDialog( showDialog = showNeedPasswordPurchases,
        onDismiss = { showNeedPasswordPurchases = false },
        onConfirm = { password ->
            if (password == "1983") {
                navController.navigate("purchases")
            }
            showNeedPasswordPurchases = false
        }
    )

}

@Composable
fun ResumeScreen(
    navController: NavHostController,
    productStateHolder: ProductStateHolder,
    productStateHolderSales: ProductStateHolder,
    productStateHolderPurchases: ProductStateHolder,
    salesRepository: ProductSalesRepository,
    productRepository: ProductRepository
) {
    var searchQuery by remember { mutableStateOf("") } // Estado para la búsqueda
    var showAddDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var showNeedPasswordPurchases by remember { mutableStateOf(false) }

    var totalSales = 0

    for (producto in productStateHolderSales.salesList) {
        if (producto.fecha == selectedDate) {
            var precioOriginal = producto.precioSeleccionado
            var localeCO = java.util.Locale("es", "CO")
            val formatter = NumberFormat.getInstance(localeCO)
            val numero = formatter.parse(precioOriginal)?.toInt() ?: 0
            var venta = producto.cantidad * numero
            totalSales = totalSales + venta
        }

    }
    var localeCO = java.util.Locale("es", "CO")
    val formatter = NumberFormat.getInstance(localeCO)
    val totalModificado = formatter.format(totalSales)

    val totalVentasDia = calcularVentasDia(productStateHolderSales)
//    val totalVentasSemana = calcularVentasSemana(productStateHolderSales)
//    val totalVentasMes = calcularVentasMes(productStateHolderSales)
//    val totalVentasAño = calcularVentasAno(productStateHolderSales)
//
//    val totalComprasDia = calcularComprasDia(productStateHolderPurchases)
//    val totalComprasSemana = calcularComprasSemana(productStateHolderPurchases)
//    val totalComprasMes = calcularComprasMes(productStateHolderPurchases)
//    val totalComprasAño = calcularComprasAno(productStateHolderPurchases)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ComunScreen(navController,
            "Resumen de ventas",
            Color.White,
            Color.White,
            Color(255, 202, 51),
            true,
            false,
            { showAddDialog = true },
            onSearchQueryChange = { searchQuery = it },
            ShowNeedPassword = {},
            toggleOpenEyes = { false },
            areOpenEyes = false,
        ShowNeedPasswordPurchases = { showNeedPasswordPurchases = true})
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezados de la tabla
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(245, 245, 245)) // Fondo gris claro para los encabezados
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HeaderTextt("Ventas del día", Modifier.weight(0.5f))



            }

            // Fila de Ventas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                CellText("$${totalVentasDia}", Modifier.weight(0.5f))

            }


            // Ajuste de los elementos en el diseño para que todo quede bien alineado
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(horizontal = 45.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(Color(0xFF007AFF))
                ) {
                    Text(text = "Seleccionar fecha", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (selectedDate.isEmpty()) "Ninguna fecha seleccionada" else "Fecha seleccionada: $selectedDate",
                    fontSize = 16.sp
                )

                if (showDatePicker) {
                    CustomDatePicker(
                        onDateSelected = { date -> selectedDate = date },
                        onDismiss = { showDatePicker = false }
                    )
                }
                if (selectedDate.isNotEmpty()) {
                    Text(
                        text = "Total ventas del día: $${totalModificado}",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                }
            }

        }
    }
    PasswordDialog( showDialog = showNeedPasswordPurchases,
        onDismiss = { showNeedPasswordPurchases = false },
        onConfirm = { password ->
            if (password == "1983") {
                navController.navigate("purchases")
            }
            showNeedPasswordPurchases = false
        }
    )
}


@Composable
fun PasswordDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = {
                Text(
                    text = "Escribir contraseña",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            },
            text = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Contraseña"
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Gray
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(password)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
