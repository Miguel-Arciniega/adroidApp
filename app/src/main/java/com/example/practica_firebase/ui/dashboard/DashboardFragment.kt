package com.example.practica_firebase.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.dao.DataBaseHelper2
import com.example.practica_firebase.databinding.FragmentDashboardBinding
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.util.ConstantsUtils.Companion.AREA_SUCCESSFULLY_ADDED
import com.example.practica_firebase.util.ConstantsUtils.Companion.INSERT_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.NO_RESULTS_FOUND
import com.example.practica_firebase.util.ConstantsUtils.Companion.SEARCH_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DESCRIPCION
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DIVISION
import com.example.practica_firebase.util.FormUtils
import com.example.practica_firebase.util.MessagesUtils

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Inicializar Base de Datos
        val dataBaseHelper1  = DataBaseHelper2(requireContext())
        val dataBaseHelper = DataBaseHelper(requireContext())

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
        FormUtils.updateListView(lvAreaView, dataBaseHelper.getAllAreas(), requireContext())

        // Muestra mensaje de información
        MessagesUtils.showMessage(requireContext(), buildMessage())

        /*
         *   Actualiza el list view de areas
         *   al hacer click al boton mostar
         */
        btnMostrar.setOnClickListener {
            FormUtils.updateListView(lvAreaView, dataBaseHelper.getAllAreas(), requireContext())
        }

        /*
         *  Inserta una nueva area en la base de datos
         *  al hacer click al boton insertar
         */
        btnInsertar.setOnClickListener{
            try {
                FormUtils.validateTextFields(listOf(etDescripcion, etDivision, etCantEmpleados))

                val etCantEmpleadosVal = binding.etCantidadEmpleados.text.toString().toInt()
                val etDescripcionVal = binding.etDescripcion.text.toString()
                val etDivisionVal = binding.etDivision.text.toString()

                // Crea y agrega una nueva area a la base de datos
                val newArea = AreaModel(-1, etDescripcionVal, etDivisionVal, etCantEmpleadosVal)
                dataBaseHelper.addOneArea(newArea)

                // Actualizar listView
                FormUtils.updateListView(lvAreaView, dataBaseHelper.getAllAreas(), requireContext())

                // Limpiar campos
                FormUtils.clearFields(listOf(etDescripcion, etDivision, etCantEmpleados))

                Toast.makeText(requireContext(), AREA_SUCCESSFULLY_ADDED, Toast.LENGTH_SHORT).show()

            } catch (exception : ValidationException){
                Toast.makeText(requireContext(), INSERT_VALIDATION_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }

        /*
         *   Regresa todas las areas que concidan con el valor buscado
         *   dependiendo del filtro seleccionado en el spinner
         *   al hacer click al boton buscar
         */
        btnBuscar.setOnClickListener{
            val spinner = binding.spinner.selectedItem

            try {

                // Valida que los campos no esten vacios
                FormUtils.validateTextFields(listOf(etBuscar))

                val etBuscarValue = binding.etBuscar.text.toString()
                var areas : List<AreaModel> = listOf()

                /* Invoca al dataBaseHelper para buscar areas dependiendo del
                    criterio de busqueda */
                if (spinner.equals(SPINNER_VALUE_DESCRIPCION)) {
                    areas = dataBaseHelper1.getAreasByDescripcion(etBuscarValue)
                }
                if (spinner.equals(SPINNER_VALUE_DIVISION)){
                    areas = dataBaseHelper1.getAreasByDivision(etBuscarValue)
                }

                // Actualizar listView
                FormUtils.updateListView(lvAreaView, areas, requireContext())

                if (areas.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), NO_RESULTS_FOUND, Toast.LENGTH_SHORT).show()
                }

                // Limpiar campos
                FormUtils.clearFields(listOf(etBuscar))

            } catch (exception : ValidationException){
                Toast.makeText(requireContext(), SEARCH_VALIDATION_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buildMessage(): String {
        val stringBuilder = StringBuilder()

        stringBuilder
            .appendLine("PARA BUSCAR UN AREA:\n")
            .appendLine("SE SELECCIONA SI ES POR <DESCRIPCIÓN O POR DIVISIÓN>, Y DESPUES SE ANOTA")
            .append(" EL NOMBRE EN EL CAMPO DE TEXTO, DAR CLICK EN ")
            .appendLine("PARA MOSTRAR DATOS:\n")
            .appendLine("DAR CLICK A <MOSTRAR TODAS LAS AREAS> PARA VISUALIZARLAS.\n")
            .appendLine("PARA ELIMINAR Y ACTUALIZAR:\n")
            .appendLine("SE PRESIONA EL AREA(EN EL LISTVIEW) Y TE DARÁ LAS OPCIONES AUTOMATICAMENTE")

        return stringBuilder.toString()
    }
}