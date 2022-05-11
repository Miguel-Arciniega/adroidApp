package com.example.practica_firebase.ui.dashboard

import android.content.Context
import android.text.Editable
import android.widget.Spinner
import android.widget.Toast
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.util.ConstantsUtils.Companion.NO_RESULTS_FOUND
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DESCRIPCION
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DIVISION
import com.example.practica_firebase.util.FormUtils.Companion.clearField
import com.example.practica_firebase.util.FormUtils.Companion.validateTextField

class DashboardFragmentService(databaseHelper : DataBaseHelper, context: Context) {

    private val dataBaseHelper = databaseHelper
    private val dashboardContext = context

    /**
     *  Valida los campos del formulario y agrega un nuevo documento
     */
    fun addDocumentWithFormValues(fieldMap : HashMap<String, Editable>){
            val fieldList = listOf(
                fieldMap["etDescripcion"],
                fieldMap["etDivision"],
                fieldMap["etCantEmpleados"]
            )

            // Validar los campos de texto
            fieldList.map { field -> validateTextField(field) }

            val etCantEmpleadosVal = fieldMap["etDescripcion"].toString().toLong()
            val etDescripcionVal = fieldMap["etDivision"].toString()
            val etDivisionVal = fieldMap["etCantEmpleados"].toString()

            // Crea y agrega una nueva area a la base de datos
            val newArea = AreaModel(-1, etDescripcionVal, etDivisionVal, etCantEmpleadosVal)
            dataBaseHelper.addOneArea(newArea)

            // Limpiar campos
            fieldList.map{ field -> clearField(field) }
    }


    /**
     *  Valida el contenido de los campos
     *  Regresa todas las areas que concidan con el valor buscado
     *  dependiendo del filtro seleccionado en el spinner
     *  TODO: descomentar cuando se migren las funciones para usar firestore
     */
    fun searchDocumentWithFormValues(editText: Editable, spinner: Spinner) : List<AreaModel>{

            // Valida que los campos no esten vacios
            validateTextField(editText)

            val etBuscarValue = editText.toString()
            val areas : List<AreaModel> = listOf()
            val selectedItem = spinner.selectedItem

            /* Invoca al dataBaseHelper para buscar areas dependiendo del
                criterio de busqueda */
            if (selectedItem.equals(SPINNER_VALUE_DESCRIPCION)) {
                // areas = dataBaseHelper.getAreasByDescripcion(etBuscarValue)
            }
            if (selectedItem.equals(SPINNER_VALUE_DIVISION)){
                // areas = dataBaseHelper.getAreasByDivision(etBuscarValue)
            }

            if (areas.isNullOrEmpty()) {
                Toast.makeText(dashboardContext, NO_RESULTS_FOUND, Toast.LENGTH_SHORT).show()
            }

            // Limpiar campo
            clearField(editText)

        return areas
    }

    /**
     * Construye un mensaje para mostrar en pantalla
     */
    fun buildMessage(): String {
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