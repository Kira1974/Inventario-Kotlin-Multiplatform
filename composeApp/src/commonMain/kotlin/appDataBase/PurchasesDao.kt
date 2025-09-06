package appDataBase
import androidx.room.*
import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchasesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(compra: Compras)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchases(compras: List<Compras>)

    @Query("SELECT * FROM compras")
    fun getAllAsFlow(): Flow<List<Compras>>

    @Query("SELECT * FROM compras WHERE productoId = :productoId")
    suspend fun getComprasByProductoId(productoId: Int): List<Compras>

    @Query("SELECT * FROM compras")
    suspend fun getAllCompras(): List<Compras>

    @Update
    suspend fun updateCompra(compra: Compras)

    @Delete
    suspend fun deleteCompra(compra: Compras)
}
