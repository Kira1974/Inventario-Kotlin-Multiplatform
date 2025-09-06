package appDataBase

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File


class DBFactory(private val context : Context) {
    actual fun createDataBase(): AppDatabase {
        val dbFile =context.getDatabasePath(dbFileName)
        return Room.databaseBuilder<AppDatabase>(context, dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun reopenDatabase(): AppDatabase {
        TODO("Not yet implemented")
    }

    actual fun closeDatabase() {
    }
}