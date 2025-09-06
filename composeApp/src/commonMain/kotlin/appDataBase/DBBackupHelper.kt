package appDataBase

import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

val appDataDir = File(System.getProperty("user.dir"), "Base de datos")
class DBBackupHelper(private val dbFactory: DBFactory) {

    private val dbFile = File(appDataDir, dbFileName)

    fun seleccionarCarpeta(): File? {
        val fileChooser = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
        fileChooser.dialogTitle = "Selecciona una carpeta para la copia de seguridad"
        fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY

        val seleccion = fileChooser.showSaveDialog(null)

        return if (seleccion == JFileChooser.APPROVE_OPTION) {
            fileChooser.selectedFile
        } else {
            null
        }
    }

    fun backupDatabase() {
        // Cierra la base de datos antes de copiar


        // Solicita al usuario la ubicación de respaldo
        val backupLocation = seleccionarCarpeta()

        if (backupLocation != null) {
            // Lista de archivos asociados a la base de datos principal
            val dbFiles = listOf(
                dbFile,
                File(appDataDir, "${dbFileName}-wal"),
                File(appDataDir, "${dbFileName}-shm"),
                File(appDataDir, "${dbFileName}.lock")
            )

            // Realizar la copia solo de los archivos que existen
            dbFiles.forEach { file ->
                if (file.exists()) {
                    val backupFile = File(backupLocation, file.name)
                    file.copyTo(backupFile, overwrite = true)
                    println("Archivo copiado: ${file.name}")
                }
            }
            println("Copia de seguridad creada en: ${backupLocation.absolutePath}")
        } else {
            println("Operación cancelada por el usuario.")
        }
    }


}