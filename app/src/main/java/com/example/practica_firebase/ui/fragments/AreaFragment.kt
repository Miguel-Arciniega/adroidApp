package com.example.practica_firebase.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.databinding.FragmentAreaBinding
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.model.enum.Operation
import com.example.practica_firebase.services.AreaService
import com.example.practica_firebase.ui.modal.ModalActualizarArea
import com.example.practica_firebase.util.FormUtils.Companion.updateListView
import com.example.practica_firebase.util.MessagesUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_CANT_EMPLEADOS
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DESCRIPCION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DIVISION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.util.ConstantsUtils.Companion.ALERT_DIALOG_OPERATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.ALERT_DIALOG_OPERATION_ACTUALIZAR
import com.example.practica_firebase.util.ConstantsUtils.Companion.ALERT_DIALOG_OPERATION_CANCELAR
import com.example.practica_firebase.util.ConstantsUtils.Companion.ALERT_DIALOG_OPERATION_ELIMINAR
import com.example.practica_firebase.util.ConstantsUtils.Companion.ALERT_DIALOG_TITLE_AVISO
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_CANT_EMPLEADOS
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_DESCRIPCION
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_DIVISION
import com.example.practica_firebase.util.ConstantsUtils.Companion.TYPE_AREA

class AreaFragment : Fragment() {

    private var _binding: FragmentAreaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAreaBinding.inflate(inflater, container, false)

        // Inicializar Servicio
        val areaService =
            AreaService(requireContext())

        // Declaración de componentes
        val lvAreaView = binding.lvLista
        val btnBuscar = binding.btnBuscar
        val btnInsertar = binding.btnInsertar
        val btnMostrar = binding.btnMostrar
        val etCantEmpleados = binding.etCantidadEmpleados.text
        val etDescripcion = binding.etDescripcion.text
        val etDivision = binding.etDivision.text
        val etBuscar = binding.etBuscar.text

        // Actualiza el list view de areas al iniciar
        CoroutineScope(IO).launch {
            updateListView(lvAreaView, areaService.getAreasFromDataBase(), TYPE_AREA, requireContext())
        }

        // Muestra mensaje de información
        MessagesUtils.showMessage(requireContext(), areaService.buildMessage())

        /**
         *  Actualiza el list view de areas al hacer click al boton mostrar
         */
        btnMostrar.setOnClickListener {
            CoroutineScope(IO).launch {
                updateListView(lvAreaView, areaService.getAreasFromDataBase(), TYPE_AREA, requireContext())
            }
        }

        /**
         *  Inserta una nueva area en la base de datos al hacer
         *  click al boton insertar
         */
        btnInsertar.setOnClickListener{
            CoroutineScope(IO).launch {
                val fieldMap = mapOf(
                    ET_DESCRIPCION to etDescripcion,
                    ET_DIVISION to etDivision,
                    ET_CANT_EMPLEADOS to etCantEmpleados,
                )

                // Agregar documento a la bd
                areaService.addAreaWithGivenValues(fieldMap)

                // Actualizar listView
                updateListView(lvAreaView, areaService.getAreasFromDataBase(), TYPE_AREA, requireContext())
            }
        }

        /**
         *  Regresa todas las areas que concidan con el valor buscado
         *  dependiendo del filtro seleccionado al hacer
         *  click al boton buscar
         */
        btnBuscar.setOnClickListener{
            CoroutineScope(IO).launch {
                val spinner = binding.spinner

                // Recuperar las areas resultantes según el criterio
                val area = areaService.getAreasByGivenValues(etBuscar, spinner)

                // Actualizar listView
                updateListView(lvAreaView, area, TYPE_AREA, requireContext())
            }
        }

        /**
         *  Obtiene el valor del item de la lista al que se le dió click
         *  y despliega un AlertDialog para elegir la operación
         *  una vez elegida la operación es procesada
         */
        lvAreaView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

            val area = lvAreaView.getItemAtPosition(position) as AreaModel

            // Construimos un AlertDialog
            AlertDialog.Builder(context)

                // Añadimos el titulo y el mensaje al AlertDialog
                .setTitle(ALERT_DIALOG_TITLE_AVISO)
                .setMessage(ALERT_DIALOG_OPERATION_MESSAGE)

                // Si se hace click en el boton de ELIMINAR, processamos la acción
                .setNegativeButton(ALERT_DIALOG_OPERATION_ELIMINAR) { _, _ ->
                    CoroutineScope(IO).launch {
                        areaService.processAlertResponse(area, Operation.DELETE)
                        updateListView(lvAreaView, areaService.getAreasFromDataBase(), TYPE_AREA, requireContext())
                    }
                }

                // Si se hace click en el boton de ACTUALIZAR, mostramos el modal para actualizar
                .setPositiveButton(ALERT_DIALOG_OPERATION_ACTUALIZAR) { _, _ ->
                    showUpdateAreaModal(area)
                }

                // Si se hace click en el boton de CANCELAR, processamos la acción
                .setNeutralButton(ALERT_DIALOG_OPERATION_CANCELAR) { _, _ ->
                    CoroutineScope(IO).launch {
                        areaService.processAlertResponse(area, Operation.CANCEL)
                    }
                }.show()
        }

        return binding.root
    }

    /**
     *  Inicia el activity ModalActualizar y le pasa el area
     *  como parametro
     *
     *  @param area
     */
    private fun showUpdateAreaModal(area: AreaModel){
        val modal = Intent(requireContext(), ModalActualizarArea::class.java)
        modal.putExtra(FIELD_ID_AREA, area.idArea)
        modal.putExtra(FIELD_DESCRIPCION, area.descripcion)
        modal.putExtra(FIELD_DIVISION, area.division)
        modal.putExtra(FIELD_CANT_EMPLEADOS, area.cantEmpleados)
        startActivity(modal)
    }

    override fun onResume(){
        val dataBaseHelper = DataBaseHelper()
        CoroutineScope(IO).launch {
            updateListView(
                binding.lvLista,
                dataBaseHelper.getAllAreas(),
                TYPE_AREA,
                requireContext()
            )
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

