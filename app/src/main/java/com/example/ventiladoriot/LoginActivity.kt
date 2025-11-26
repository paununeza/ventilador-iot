package com.example.ventiladoriot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ventiladoriot.database.DBHelper
import com.example.ventiladoriot.databinding.ActivityLoginBinding
import com.example.ventiladoriot.utils.HashUtils

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DBHelper(this)

        binding.btnLogin.setOnClickListener {
            val user = binding.editUser.text.toString().trim()
            val pass = binding.editPass.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validamos con el hash
            val inputHash = HashUtils.sha256(pass)

            if (dbHelper.validarUsuario(user, inputHash)) {
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()


                val prefs = getSharedPreferences("VentiladorPrefs", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.putString("username", user)
                editor.apply()


                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
