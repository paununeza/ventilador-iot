package com.example.ventiladoriot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // ¡Importante para que funcione el diálogo!
import androidx.appcompat.app.AppCompatActivity
import com.example.ventiladoriot.database.DBHelper
import com.example.ventiladoriot.databinding.EditarborrarUsuarioBinding
import com.example.ventiladoriot.utils.HashUtils

class EditarBorrarUsuario : AppCompatActivity() {

    private lateinit var binding: EditarborrarUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditarborrarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences("VentiladorPrefs", Context.MODE_PRIVATE)
        val currentUser = prefs.getString("username", "") ?: ""

        if (currentUser.isEmpty()) {
            binding.txtUsernameDisplay.text = "Error: Sesión perdida"
            Toast.makeText(this, "Error de sesión. Vuelve a ingresar.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.txtUsernameDisplay.text = "Usuario: $currentUser"

        binding.btnSaveProfile.setOnClickListener {
            val oldPass = binding.editOldPass.text.toString()
            val newPass = binding.editNewPass.text.toString()
            val confirm = binding.editNewPassConfirm.text.toString()

            if (oldPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPass.length < 8) {
                Toast.makeText(this, "La nueva contraseña debe tener 8 caracteres mínimo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPass != confirm) {
                Toast.makeText(this, "Las contraseñas nuevas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = DBHelper(this)
            val oldHash = HashUtils.sha256(oldPass)
            val dbHash = db.obtenerHashUsuario(currentUser)

            if (oldHash == dbHash) {
                val newHash = HashUtils.sha256(newPass)
                if (db.actualizarPassword(currentUser, newHash)) {
                    Toast.makeText(this, "¡Contraseña actualizada!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "La contraseña actual es incorrecta", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnDeleteAccount.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("¿Eliminar Cuenta?")
                .setMessage("Esta acción borrará tu usuario permanentemente. ¿Estás seguro?")
                .setPositiveButton("Sí, Eliminar") { _, _ ->

                    val db = DBHelper(this)
                    if (db.eliminarUsuario(currentUser)) {

                        prefs.edit().clear().apply()

                        Toast.makeText(this, "Cuenta eliminada correctamente", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error: No se pudo eliminar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}
