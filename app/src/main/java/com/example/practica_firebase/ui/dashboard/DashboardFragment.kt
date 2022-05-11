package com.example.practica_firebase.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.databinding.FragmentDashboardBinding
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.util.ConstantsUtils.Companion.AREA_SUCCESSFULLY_ADDED
import com.example.practica_firebase.util.ConstantsUtils.Companion.INSERT_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.SEARCH_VALIDATION_MESSAGE
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
        val dataBaseHelper = DataBaseHelper()

        // Inicializar Servicio
        val dashboardFragmentService =
            DashboardFragmentService(dataBaseHelper, requireContext())

        // Declaración de componentes
        val lvAreaView = binding.lvLista
        val btnBuscar = binding.btnBuscar
        val btnInsertar = binding.btnInsertar
        val btnMostrar = binding.btnMostrar
        val etCantEmpleados = binding.etCantidadEmpleados.text
        val etDescripcion = binding.etDescripcion.text
        val etDivision = binding.etDivision.text
        val etBuscar = binding.etBuscar.text

        // TODO: Arreglar, no actualiza al iniciar
        // Actualiza el list view de areas al iniciar
        FormUtils.updateListView(lvAreaView, dataBaseHelper.getAllAreas(), requireContext())

        // Muestra mensaje de información
        MessagesUtils.showMessage(requireContext(), dashboardFragmentService.buildMessage())

        /**
         *  Actualiza el list view de areas al hacer click al boton mostar
         *  TODO: Arreglar, funciona pero no como debería
         */
        btnMostrar.setOnClickListener {
            FormUtils.updateListView(lvAreaView, dataBaseHelper.getAllAreas(), requireContext())
        }

        /**
         *  Inserta una nueva area en la base de datos al hacer
         *  click al boton insertar
         */
        btnInsertar.setOnClickListener{
            try {
                val fieldMap = hashMapOf<String, Editable>()

                fieldMap["etCantEmpleados"] = etCantEmpleados
                fieldMap["etDescripcion"] = etDescripcion
                fieldMap["etDivision"] = etDivision

                // Agregar documento a la bd
                dashboardFragmentService.addDocumentWithFormValues(fieldMap)

                // Actualizar listView
                FormUtils.updateListView(lvAreaView, dataBaseHelper.getAllAreas(), requireContext())
                Toast.makeText(requireContext(), AREA_SUCCESSFULLY_ADDED, Toast.LENGTH_SHORT).show()
            } catch (exception : ValidationException){
                Toast.makeText(requireContext(), INSERT_VALIDATION_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }

        /**
         *  Regresa todas las areas que concidan con el valor buscado
         *  dependiendo del filtro seleccionado
         */
        btnBuscar.setOnClickListener{
            val spinner = binding.spinner

            try {
                // Recuperar las areas resultantes según el criterio
                val area = dashboardFragmentService.searchDocumentWithFormValues(etBuscar, spinner)

                // Actualizar listView
                FormUtils.updateListView(lvAreaView, area, requireContext())

            } catch (exception : ValidationException){
                Toast.makeText(context, SEARCH_VALIDATION_MESSAGE, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}