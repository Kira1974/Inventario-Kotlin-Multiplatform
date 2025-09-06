package appDataBase

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

actual object DBFactory {
    private var db: AppDatabase? = null

    actual fun createDataBase(): AppDatabase {
        if (db == null) {
            // Crear el directorio si no existe
            if (!appDataDir.exists()) {
                appDataDir.mkdirs()
            }
            val dbFile = File(appDataDir, dbFileName)
            db = Room.databaseBuilder<AppDatabase>(dbFile.absolutePath)
                .setDriver(BundledSQLiteDriver())
                .build()
        }
        return db!!
    }

    actual fun closeDatabase() {
        db?.close()
        db = null
    }

    actual fun reopenDatabase(): AppDatabase {
        closeDatabase() // Cierra la base de datos existente
        return createDataBase() // Crea una nueva instancia
    }
}