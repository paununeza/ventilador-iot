package com.example.ventiladoriot

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ventiladoriot.database.DBHelper
import com.example.ventiladoriot.databinding.ActivityRegisterBinding
import com.example.ventiladoriot.utils.HashUtils

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = DBHelper(this)

        binding.btnCreateUser.setOnClickListener {
            val user = binding.editUserR.text.toString().trim()
            val pass = binding.editPassR.text.toString().trim()
            val confirmPass = binding.editPassConfirmR.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "No dejes campos vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPass = HashUtils.sha256(pass)
            val ok = db.registrarUsuario(user, hashedPass)

            if (ok) {
                Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Error: usuario ya existe", Toast.LENGTH_LONG).show()
            }
        }
    }
}