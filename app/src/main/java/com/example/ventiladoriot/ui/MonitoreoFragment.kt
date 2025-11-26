package com.example.ventiladoriot.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ventiladoriot.EditarBorrarUsuario
import com.example.ventiladoriot.databinding.FragmentMonitoreoBinding

class MonitoreoFragment : Fragment() {

    private var _binding: FragmentMonitoreoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonitoreoBinding.inflate(inflater, container, false)
        // Solo para probar - borrar despues la siguiente lineaa!!
        binding.textTemperatura.text = "25 °C"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnIrPerfil.setOnClickListener {

            val intent = Intent(requireContext(), EditarBorrarUsuario::class.java)

            startActivity(intent)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}