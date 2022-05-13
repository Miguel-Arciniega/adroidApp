package com.example.practica_firebase.ui.modal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_EDIFICIO
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_SUBDEPTO
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_PISO
import com.example.practica_firebase.databinding.ModalActualizarSubDeptosBinding
import com.example.practica_firebase.model.SubDeptoModel
import com.example.practica_firebase.model.enum.Operation
import com.example.practica_firebase.services.SubDeptoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModalActualizarSubDepto : AppCompatActivity() {

    lateinit var binding: ModalActualizarSubDeptosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ModalActualizarSubDeptosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtenemos los extras
        val extras = this.intent.extras!!

        // Declaración de componentes
        val etIdEdificio = binding.idedificio
        val etPiso = binding.piso
        val btnActualizar = binding.actualizar
        val btnVolver = binding.volver

        // Inicializamos el  Servicio
        val subDeptoService =
            SubDeptoService(this)

        // Obtenemos los valores originales del area
        val idSubdepto = extras[FIELD_ID_SUBDEPTO].toString().toLong()
        val idEdificio = extras[FIELD_ID_EDIFICIO].toString().uppercase()
        val piso = extras[FIELD_PISO].toString().uppercase()
        val idArea = extras[FIELD_ID_AREA].toString().toLong()

        etIdEdificio.setText(idEdificio)
        etPiso.setText(piso)

        btnActualizar.setOnClickListener {

            val fieldIdEdificio = if (etIdEdificio.text.isNullOrBlank()){
                idEdificio
            }else{
                etIdEdificio.text.toString().uppercase()
            }
            val fieldPiso = if (etPiso.text.isNullOrBlank()){
                piso
            } else {
                etPiso.text.toString().uppercase()
            }

            val subDepto = SubDeptoModel(
                idSubdepto,
                fieldIdEdificio,
                fieldPiso,
                idArea
            )

            CoroutineScope(Dispatchers.IO).launch {
                subDeptoService.processAlertResponse(subDepto, Operation.UPDATE)
            }
        }

        btnVolver.setOnClickListener {
            finish()
        }
    }
}