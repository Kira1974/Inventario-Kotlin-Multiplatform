package appDataBase

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import java.io.File

expect object DBFactory {
      fun createDataBase(): AppDatabase
      fun reopenDatabase(): AppDatabase
      fun closeDatabase()
}