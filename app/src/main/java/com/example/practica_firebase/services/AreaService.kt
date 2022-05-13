package com.example.practica_firebase.services

import android.content.Context
import android.text.Editable
import android.widget.Spinner
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.model.enum.Operation
import com.example.practica_firebase.util.ConstantsUtils.Companion.AREA_SUCCESSFULLY_ADDED
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_CANT_EMPLEADOS
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_DESCRIPCION
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_DIVISION
import com.example.practica_firebase.util.ConstantsUtils.Companion.FAIL_WHEN_TRYING_TO_GET_AREAS
import com.example.practica_firebase.util.ConstantsUtils.Companion.FAIL_WHEN_TRYING_TO_INSERT_AREA
import com.example.practica_firebase.util.ConstantsUtils.Companion.INSERT_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.NO_RESULTS_FOUND_AREAS
import com.example.practica_firebase.util.ConstantsUtils.Companion.SEARCH_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DESCRIPCION
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DIVISION
import com.example.practica_firebase.util.FormUtils.Companion.clearField
import com.example.practica_firebase.util.FormUtils.Companion.makeMessage
import com.example.practica_firebase.util.FormUtils.Companion.validateTextField

class AreaService(context: Context) {

    private val dataBaseHelper = DataBaseHelper()
    private val dashboardContext = context

    suspend fun getAreasFromDataBase () : List<AreaModel>{
        return dataBaseHelper.getAllAreas()
    }

    /**
     *  Valida los campos del formulario y agrega un nuevo documento
     *
     *  @param fieldMap
     */
    suspend fun addAreaWithGivenValues(fieldMap : Map<String, Editable>){

            val fieldList = listOf(
                fieldMap[ET_DESCRIPCION],
                fieldMap[ET_DIVISION],
                fieldMap[ET_CANT_EMPLEADOS]
            )

            try {
                // Validar los campos de texto
                fieldList.map { field -> validateTextField(field) }

                val etDescripcionVal = fieldMap[ET_DESCRIPCION].toString().uppercase()
                val etDivisionVal = fieldMap[ET_DIVISION].toString().uppercase()
                val etCantEmpleadosVal = fieldMap[ET_CANT_EMPLEADOS].toString().toLong()

                // Crea y agrega una nueva area a la base de datos
                val newArea = AreaModel(-1L,
                    etDescripcionVal,
                    etDivisionVal,
                    etCantEmpleadosVal
                )

                // Agrega la nueva area a la base de datos
                dataBaseHelper.addOneArea(newArea)

                // Limpia los campos de texto
                fieldList.map{ field -> clearField(field) }

                makeMessage(AREA_SUCCESSFULLY_ADDED, dashboardContext)
            } catch (exception : ValidationException){
                makeMessage(INSERT_VALIDATION_MESSAGE, dashboardContext)
            } catch (exception : Exception) {
                makeMessage(FAIL_WHEN_TRYING_TO_INSERT_AREA, dashboardContext)
            }
    }


    /**
     *  Valida el contenido de los campos
     *  Regresa todas las areas que concidan con el valor buscado
     *  dependiendo del filtro seleccionado en el spinner
     *
     *  @param editText
     *  @param spinner
     */
    suspend fun getAreasByGivenValues(editText: Editable, spinner: Spinner) : List<AreaModel>{

            // Valida que los campos no esten vacios
            try {
                validateTextField(editText)
            } catch (exception: ValidationException) {
                makeMessage(SEARCH_VALIDATION_MESSAGE, dashboardContext)
            }

            val etBuscarValue = editText.toString().uppercase()
            var areas : List<AreaModel> = listOf()
            val selectedItem = spinner.selectedItem

            /* Invoca el método dataBaseHelper para buscar areas dependiendo del
                criterio de busqueda */
            try {
                if (selectedItem.equals(SPINNER_VALUE_DESCRIPCION)) {
                    areas = dataBaseHelper.getAreasByDescription(etBuscarValue)
                }
                if (selectedItem.equals(SPINNER_VALUE_DIVISION)){
                    areas = dataBaseHelper.getAreasByDivision(etBuscarValue)
                }
                if (areas.isNullOrEmpty()) {
                    makeMessage(NO_RESULTS_FOUND_AREAS, dashboardContext)
                }

            // Limpia el campo de texto
            clearField(editText)

            } catch (exception : Exception) {
                makeMessage(FAIL_WHEN_TRYING_TO_GET_AREAS, dashboardContext)
            }

        return areas
    }

    /**
     *  Procesa la respuesta obtenida del AlertDialog y realiza
     *  la operación en base de datos correspondiente
     *
     *  @param area
     *  @param operation
     */
    suspend fun processAlertResponse(area: AreaModel, operation : Operation) {

        if (operation == Operation.CANCEL){
            makeMessage("Acción cancelada", dashboardContext)
            return
        }

        if (operation == Operation.DELETE){
            try {
                dataBaseHelper.deleteAreaById(area.idArea!!)
            } catch (exception : Exception) {
                makeMessage("Error al eliminar", dashboardContext)
            }
            makeMessage("Area eliminada con éxito", dashboardContext)
        }

        if (operation == Operation.UPDATE){
            try {
                dataBaseHelper.updateOneArea(area)
            } catch (exception : Exception) {
                makeMessage("Error al actualizar", dashboardContext)
            }
            makeMessage("Area actualizada con éxito", dashboardContext)
        }
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