package appDataBase
import androidx.room.*
import com.johan.inventario.Model.Productos
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Productos)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Productos>)

    @Query("SELECT * FROM productos")
     fun getAllAsFlow(): Flow<List<Productos>>

    @Query("SELECT * FROM productos")
    suspend fun getAllProducts(): List<Productos>   

    @Update // Método para actualizar el producto existente
    suspend fun updateProduct(product: Productos)

    @Delete
    suspend fun deleteProduct(product: Productos)

    @Query("DELETE FROM productos WHERE nombre = :nombre AND precioVenta = :precioVenta AND ventas = :ventas")
    suspend fun deleteProductByCriteria(nombre: String, precioVenta: String, ventas: Int)
}