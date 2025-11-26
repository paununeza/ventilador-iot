package com.example.ventiladoriot.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.ventiladoriot.R
import com.example.ventiladoriot.databinding.FragmentActivadorBinding

class ActivadorFragment : Fragment() {

    private var _binding: FragmentActivadorBinding? = null
    private val binding get() = _binding!!

    private var isManual = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActivadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Switch On/Off
        binding.switchPower.setOnCheckedChangeListener { _, isChecked ->
            val state = if (isChecked) "Ventilador ENCENDIDO" else "Ventilador APAGADO"
            addLog(state)
        }

        // Auto / Manual
        binding.radioMode.setOnCheckedChangeListener { _, checkedId ->
            isManual = checkedId == R.id.radioManual
            val mode = if (isManual) "Modo MANUAL" else "Modo AUTOMÁTICO"
            addLog(mode)

            binding.switchPower.isEnabled = isManual
            binding.sliderSpeed.isEnabled = isManual
        }

        // Slider velocidad
        binding.sliderSpeed.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.txtSpeedValue.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (isManual) {
                    addLog("Velocidad ajustada a ${binding.sliderSpeed.progress}%")
                }
            }
        })

        binding.switchPower.isEnabled = isManual
        binding.sliderSpeed.isEnabled = isManual
    }

    private fun addLog(message: String) {
        binding.txtLogs.append("$message\n")
        binding.logScrollView.post {
            binding.logScrollView.fullScroll(View.FOCUS_DOWN)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
