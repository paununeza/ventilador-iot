package com.example.ventiladoriot.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val query = """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent()

        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }


    fun registrarUsuario(username: String, hash: String): Boolean {

        if (usuarioExiste(username)) {
            return false // El usuario ya existe
        }

        val db = writableDatabase
        val valores = ContentValues()
        valores.put("username", username)
        valores.put("password", hash) // Guarda el hash

        val result = db.insert("usuarios", null, valores)
        db.close()

        return result != -1L
    }


    fun validarUsuario(user: String, hash: String): Boolean {
        val db = readableDatabase
        val query = "SELECT 1 FROM usuarios WHERE username = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(user, hash))
        val existe = cursor.count > 0

        cursor.close()
        db.close()

        return existe
    }

    fun obtenerHashUsuario(username: String): String? {
        val db = readableDatabase
        val query = "SELECT password FROM usuarios WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        var hash: String? = null
        if (cursor.moveToFirst()) {
            hash = cursor.getString(0)
        }
        cursor.close()
        return hash
    }

    fun actualizarPassword(username: String, newHash: String): Boolean {
        val db = writableDatabase
        val valores = ContentValues()
        valores.put("password", newHash)

        val filasAfectadas = db.update("usuarios", valores, "username=?", arrayOf(username))
        return filasAfectadas > 0
    }

    private fun usuarioExiste(username: String): Boolean {
        val db = readableDatabase
        val query = "SELECT 1 FROM usuarios WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val existe = cursor.count > 0
        cursor.close()


        val dbReadable = this.readableDatabase
        val cursorCheck = dbReadable.rawQuery("SELECT 1 FROM usuarios WHERE username = ?", arrayOf(username))
        val existeCheck = cursorCheck.count > 0
        cursorCheck.close()
        dbReadable.close()
        return existeCheck
    }

    fun eliminarUsuario(username: String): Boolean{
        val db = writableDatabase
        val filasBorradas = db.delete("usuarios", "username = ?", arrayOf(username))
        return filasBorradas > 0
    }
}