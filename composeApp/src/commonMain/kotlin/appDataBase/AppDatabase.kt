package appDataBase



import androidx.room.Database
import androidx.room.RoomDatabase
import com.johan.inventario.Model.Compras
import com.johan.inventario.Model.Productos
import com.johan.inventario.Model.Ventas



@Database(entities = [Productos::class, Ventas::class, Compras::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun purchasesDao(): PurchasesDao
    abstract fun salesDao(): SalesDao



}
internal var dbFileName = "Base_de_datos_01.db"
