package com.example.practica_firebase.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_EDIFICIO
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_SUBDEPTO
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_PISO
import com.example.practica_firebase.databinding.FragmentSubDeptosBinding
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.model.SubDeptoModel
import com.example.practica_firebase.model.enum.Operation
import com.example.practica_firebase.services.AreaService
import com.example.practica_firebase.services.SubDeptoService
import com.example.practica_firebase.ui.modal.ModalActualizarArea
import com.example.practica_firebase.ui.modal.ModalActualizarSubDepto
import com.example.practica_firebase.util.ConstantsUtils
import com.example.practica_firebase.util.ConstantsUtils.Companion.TYPE_AREA
import com.example.practica_firebase.util.ConstantsUtils.Companion.TYPE_SUB_DEPTO
import com.example.practica_firebase.util.FormUtils
import com.example.practica_firebase.util.MessagesUtils.Companion.showMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubDeptoFragment : Fragment() {

    private var _binding: FragmentSubDeptosBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSubDeptosBinding.inflate(inflater, container, false)

        // Inicializar Servicio
        val subDeptoService =
            SubDeptoService(requireContext())

        // Inicializar Servicio
        val areaService =
            AreaService(requireContext())

        // Declaración de componentes
        val lvAreaView = binding.lvLista
        val switch = binding.switch1
        val btnBuscar = binding.etBtnBuscar
        val btnInsertar = binding.btnInsertar
        val btnMostrar = binding.btnMostarTodos
        val etIdEdificio = binding.etIdEdificio
        val etIdPiso = binding.etPiso
        val etIdArea = binding.etIdArea
        val etBuscar = binding.etBuscar

        // Obtenemos los extras

        val extras = this.requireActivity().intent.extras
        if (extras != null){
            binding.etIdArea.
            setText(extras.get(FIELD_ID_AREA).toString())
        }else{
            // Muestra mensaje de información
            showMessage(requireContext(), subDeptoService.buildMessage())
        }

        switch.setOnClickListener {
            if (switch.isChecked){

                switch.text = "Areas"

                // Actualiza el list view de areas o subdepartamentos al iniciar
                CoroutineScope(Dispatchers.IO).launch {
                    FormUtils.updateListView(
                        lvAreaView,
                        areaService.getAreasFromDataBase(),
                        TYPE_AREA,
                        requireContext()
                    )
                }
            }else{

                switch.text = "Subdepartamentos"

                // Actualiza el list view de areas o subdepartamentos al iniciar
                CoroutineScope(Dispatchers.IO).launch {
                    FormUtils.updateListView(
                        lvAreaView,
                        subDeptoService.getSubDeptosFromDataBase(),
                        TYPE_SUB_DEPTO,
                        requireContext()
                    )
                }
            }
        }

        /**
         *  Actualiza el list view de areas o subdepartamentos al hacer click al boton mostrar
         */
        btnMostrar.setOnClickListener {
            if (switch.isChecked){
                // Actualiza el list view de areas o subdepartamentos al iniciar
                CoroutineScope(Dispatchers.IO).launch {
                    FormUtils.updateListView(
                        lvAreaView,
                        areaService.getAreasFromDataBase(),
                        TYPE_AREA,
                        requireContext()
                    )
                }
            }else{
                // Actualiza el list view de areas o subdepartamentos al iniciar
                CoroutineScope(Dispatchers.IO).launch {
                    FormUtils.updateListView(
                        lvAreaView,
                        subDeptoService.getSubDeptosFromDataBase(),
                        TYPE_SUB_DEPTO,
                        requireContext()
                    )
                }
            }
        }

        /**
         *  Inserta una nuevo subdepartamento en la base de datos al hacer
         *  click al boton insertar
         */
        btnInsertar.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val fieldMap = mapOf(
                    ConstantsUtils.ET_ID_AREA to etIdArea.text,
                    ConstantsUtils.ET_ID_EDIFICIO to etIdEdificio.text,
                    ConstantsUtils.ET_PISO to etIdPiso.text,
                )

                 // Agregar documento a la bd
                 subDeptoService.addSubDeptoWithGivenValues(fieldMap)

                withContext(Main) {
                    switch.isChecked = false
                    binding.switch1.text = "Subdepartamento"
                }

                 //Actualizar listView
                 FormUtils.updateListView(
                     lvAreaView,
                     subDeptoService.getSubDeptosFromDataBase(),
                     TYPE_SUB_DEPTO,
                     requireContext()
                 )
            }
        }

        /**
         *  Regresa todas los subdepartamentos que concidan con el valor buscado
         *  dependiendo del filtro seleccionado al hacer
         *  click al boton buscar
         */
        btnBuscar.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val spinner = binding.spinner2
                val etBuscarVal = etBuscar.text

                // Recuperar las areas o subdepartamentos resultantes según el criterio
                val subDepto = subDeptoService.getSubDeptoByGivenValues(etBuscarVal, spinner)

                withContext(Main) {
                    switch.isChecked = false
                    binding.switch1.text = "Subdepartamento"
                }

                // Actualizar listView
                FormUtils.updateListView(lvAreaView, subDepto, TYPE_SUB_DEPTO, requireContext())
            }
        }

        /**
         *  Obtiene el valor del item de la lista al que se le dió click
         *  y despliega un AlertDialog para elegir la operación
         *  una vez elegida la operación es procesada
         */
        lvAreaView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->

                if (switch.isChecked) {

                    val area = lvAreaView.getItemAtPosition(position) as AreaModel

                    // Construimos un AlertDialog
                    AlertDialog.Builder(context)

                        // Añadimos el titulo y el mensaje al AlertDialog
                        .setTitle(ConstantsUtils.ALERT_DIALOG_TITLE_AVISO)
                        .setMessage(ConstantsUtils.ALERT_DIALOG_SELECT_MESSAGE + "\n\n" + area)

                        // Si se hace click en el boton de ACTUALIZAR, mostramos el modal para actualizar
                        .setPositiveButton(ConstantsUtils.ALERT_DIALOG_OPERATION_ACEPTAR) { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                                etIdArea.setText(area.idArea.toString())
                            }
                        }

                        // Si se hace click en el boton de CANCELAR, processamos la acción
                        .setNeutralButton(ConstantsUtils.ALERT_DIALOG_OPERATION_CANCELAR) { _, _ ->
                        }.show()
                }else{
                    val subDepto = lvAreaView.getItemAtPosition(position) as SubDeptoModel

                    // Construimos un AlertDialog
                    AlertDialog.Builder(context)

                        // Añadimos el titulo y el mensaje al AlertDialog
                        .setTitle(ConstantsUtils.ALERT_DIALOG_TITLE_AVISO)
                        .setMessage(ConstantsUtils.ALERT_DIALOG_OPERATION_MESSAGE)

                        // Si se hace click en el boton de ELIMINAR, processamos la acción
                        .setNegativeButton(ConstantsUtils.ALERT_DIALOG_OPERATION_ELIMINAR) { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                                subDeptoService.processAlertResponse(subDepto, Operation.DELETE)
                                FormUtils.updateListView(
                                    lvAreaView,
                                    subDeptoService.getSubDeptosFromDataBase(),
                                    TYPE_AREA,
                                    requireContext()
                                )
                                withContext(Main) {
                                    switch.isChecked = false
                                    binding.switch1.text = "Subdepartamento"
                                }
                            }
                        }

                        // Si se hace click en el boton de ACTUALIZAR, mostramos el modal para actualizar
                        .setPositiveButton(ConstantsUtils.ALERT_DIALOG_OPERATION_ACTUALIZAR) { _, _ ->
                            showUpdateDeptoModal(subDepto)
                            switch.isChecked = false
                            binding.switch1.text = "Subdepartamento"
                        }

                        // Si se hace click en el boton de CANCELAR, processamos la acción
                        .setNeutralButton(ConstantsUtils.ALERT_DIALOG_OPERATION_CANCELAR) { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                                subDeptoService.processAlertResponse(subDepto, Operation.CANCEL)
                            }
                        }.show()
                }
            }

        return binding.root
    }

    override fun onResume(){
        // Inicializar Servicio
        val subDeptoService =
            SubDeptoService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            FormUtils.updateListView(
                binding.lvLista,
                subDeptoService.getSubDeptosFromDataBase(),
                TYPE_SUB_DEPTO,
                requireContext()
            )
        }

        binding.switch1.isChecked = false
        binding.switch1.text = "Subdepartamento"

        super.onResume()
    }

    /**
     *  Inicia el activity ModalActualizar y le pasa el area
     *  como parametro
     *
     *  @param subDepto
     */
    private fun showUpdateDeptoModal(subDepto: SubDeptoModel){
        val modal = Intent(requireContext(), ModalActualizarSubDepto::class.java)
        modal.putExtra(FIELD_ID_SUBDEPTO, subDepto.idSubDepto)
        modal.putExtra(FIELD_ID_EDIFICIO, subDepto.idEdificio)
        modal.putExtra(FIELD_PISO, subDepto.piso)
        modal.putExtra(FIELD_ID_AREA, subDepto.idArea)
        startActivity(modal)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}