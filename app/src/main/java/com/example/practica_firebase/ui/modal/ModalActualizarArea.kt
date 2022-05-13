package com.example.practica_firebase.ui.modal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_CANT_EMPLEADOS
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DESCRIPCION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DIVISION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.databinding.ActivityModalActualizarBinding
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.model.enum.Operation
import com.example.practica_firebase.services.AreaService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModalActualizarArea : AppCompatActivity() {

    lateinit var binding: ActivityModalActualizarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModalActualizarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtenemos los extras
        val extras = this.intent.extras!!

        // Declaración de componentes
        val etDescripcion = binding.descripcion
        val etDivision = binding.sDivision
        val etCantEmpleados = binding.cantidadEmpleados
        val btnActualizar = binding.actualizar
        val btnVolver = binding.volver

        // Inicializamos el  Servicio
        val dashboardFragmentService =
            AreaService(this)

        // Obtenemos los valores originales del area
        val idArea = extras[FIELD_ID_AREA].toString().toLong()
        val descripcion = extras[FIELD_DESCRIPCION].toString().uppercase()
        val division = extras[FIELD_DIVISION].toString().uppercase().uppercase()
        val cantEmpleados = extras[FIELD_CANT_EMPLEADOS].toString().toLong()

        etDescripcion.setText(descripcion)
        etDivision.setText(division)
        etCantEmpleados.setText(cantEmpleados.toString())

        btnActualizar.setOnClickListener {

            val fieldDescripcion = if (etDescripcion.text.isNullOrBlank()){
                descripcion
            }else{
                etDescripcion.text.toString().uppercase()
            }
            val fieldDivision = if (etDivision.text.isNullOrBlank()){
                division
            }else{
                etDivision.text.toString().uppercase()
            }
            val fieldCantEmpleados = if (etCantEmpleados.text.isNullOrBlank()){
                cantEmpleados
            }else{
                etCantEmpleados.text.toString().toLong()
            }

            val area = AreaModel(
                idArea,
                fieldDescripcion,
                fieldDivision,
                fieldCantEmpleados
            )

            CoroutineScope(Dispatchers.IO).launch {
                dashboardFragmentService.processAlertResponse(area, Operation.UPDATE)
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}