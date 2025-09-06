package appDataBase
import androidx.room.*
import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas
import kotlinx.coroutines.flow.Flow


@Dao

interface SalesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(venta: Ventas)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSales(ventas: List<Ventas>)

    @Query("SELECT * FROM ventas WHERE productoId = :productoId")
    suspend fun getVentasByProductoId(productoId: Int): List<Ventas>

    @Query("SELECT * FROM ventas")
    suspend fun getAllVentas(): List<Ventas>

    @Query("SELECT * FROM ventas")
    fun getAllAsFlow(): Flow<List<Ventas>>


    @Update
    suspend fun updateVenta(venta: Ventas)

    @Delete
    suspend fun deleteVenta(venta: Ventas)

    @Query("DELETE FROM ventas")
    suspend fun deleteAllVentas()
}