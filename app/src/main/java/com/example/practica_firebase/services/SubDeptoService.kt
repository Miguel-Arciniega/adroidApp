package com.example.practica_firebase.services

import android.content.Context
import android.text.Editable
import android.widget.Spinner
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.exception.AreaNotSelectedException
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.model.SubDeptoModel
import com.example.practica_firebase.model.enum.Operation
import com.example.practica_firebase.util.ConstantsUtils.Companion.AREA_NOT_SELECTED_FOUND
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_ID_AREA
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_ID_EDIFICIO
import com.example.practica_firebase.util.ConstantsUtils.Companion.ET_PISO
import com.example.practica_firebase.util.ConstantsUtils.Companion.FAIL_WHEN_TRYING_TO_GET_AREAS
import com.example.practica_firebase.util.ConstantsUtils.Companion.FAIL_WHEN_TRYING_TO_INSERT_SUB_DEPTO
import com.example.practica_firebase.util.ConstantsUtils.Companion.INSERT_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.NO_RESULTS_FOUND_SUB_DEPTO
import com.example.practica_firebase.util.ConstantsUtils.Companion.SEARCH_VALIDATION_MESSAGE
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DESCRIPCION
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_DIVISION
import com.example.practica_firebase.util.ConstantsUtils.Companion.SPINNER_VALUE_ID_EDIFICIO
import com.example.practica_firebase.util.ConstantsUtils.Companion.SUB_DEPTO_SUCCESSFULLY_ADDED
import com.example.practica_firebase.util.FormUtils.Companion.clearField
import com.example.practica_firebase.util.FormUtils.Companion.makeMessage
import com.example.practica_firebase.util.FormUtils.Companion.validateTextField

class SubDeptoService(context: Context) {

    private val dataBaseHelper = DataBaseHelper()
    private val subDeptoFragmentContext = context

    suspend fun getSubDeptosFromDataBase () : List<SubDeptoModel> {
        return try {
            dataBaseHelper.getAllSubDeptos()
        }catch (exception: IllegalStateException){
            makeMessage(FAIL_WHEN_TRYING_TO_GET_AREAS, subDeptoFragmentContext)
            listOf()
        }
    }

    /**
     *  Valida los campos del formulario y agrega un nuevo documento
     *
     *  @param fieldMap
     */
    suspend fun addSubDeptoWithGivenValues(fieldMap : Map<String, Editable>){

        val fieldList = listOf(
            fieldMap[ET_ID_AREA],
            fieldMap[ET_ID_EDIFICIO],
            fieldMap[ET_PISO]
        )

        try{
            // Validar que se haya seleccionado un area
            if (fieldMap[ET_ID_AREA].isNullOrBlank()){
                throw AreaNotSelectedException()
            }

            // Validar los campos de texto
            fieldList.map { field -> validateTextField(field) }

            val etIdEdificioVal = fieldMap[ET_ID_EDIFICIO].toString()
            val etPisoVal = fieldMap[ET_PISO].toString()
            val etIdAreaVal = fieldMap[ET_ID_AREA].toString().toLong()

            // Crea y agrega una nueva area a la base de datos
            val newSubDepto = SubDeptoModel(-1L,
                etIdEdificioVal,
                etPisoVal,
                etIdAreaVal
            )

            // Agrega la nueva area a la base de datos
            dataBaseHelper.addOneSubDepto(newSubDepto)

            // Limpiar campos
            fieldList.map{ field -> clearField(field) }

            makeMessage(SUB_DEPTO_SUCCESSFULLY_ADDED, subDeptoFragmentContext)
        } catch (exception : ValidationException){
            makeMessage(INSERT_VALIDATION_MESSAGE, subDeptoFragmentContext)
        } catch (exception : AreaNotSelectedException){
            makeMessage(AREA_NOT_SELECTED_FOUND, subDeptoFragmentContext)
        } catch (exception : IllegalStateException) {
            makeMessage(FAIL_WHEN_TRYING_TO_INSERT_SUB_DEPTO, subDeptoFragmentContext)
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
    suspend fun getSubDeptoByGivenValues(editText: Editable, spinner: Spinner) : List<SubDeptoModel>{

            // Valida que los campos no esten vacios
            try {
                validateTextField(editText)
            } catch (exception: ValidationException) {
                makeMessage(SEARCH_VALIDATION_MESSAGE, subDeptoFragmentContext)
            }

            val etBuscarValue = editText.toString().uppercase()
            var subDepto : List<SubDeptoModel> = listOf()
            val selectedItem = spinner.selectedItem

            /* Invoca al dataBaseHelper para buscar areas dependiendo del
                criterio de busqueda */
            try {
                if (selectedItem.equals(SPINNER_VALUE_DESCRIPCION)) {
                    subDepto = dataBaseHelper.getSubDeptoByDescription(etBuscarValue)
                }
                if (selectedItem.equals(SPINNER_VALUE_DIVISION)){
                    subDepto = dataBaseHelper.getSubDeptoByDivision(etBuscarValue)
                }
                if (selectedItem.equals(SPINNER_VALUE_ID_EDIFICIO)){
                    subDepto = dataBaseHelper.getSubDeptoByIdEdificio(etBuscarValue)
                }
                if (subDepto.isNullOrEmpty()) {
                    makeMessage(NO_RESULTS_FOUND_SUB_DEPTO, subDeptoFragmentContext)
                }
            } catch (exception : Exception) {
                makeMessage(FAIL_WHEN_TRYING_TO_INSERT_SUB_DEPTO, subDeptoFragmentContext)
            }

            // Limpiar campo
            clearField(editText)

        return subDepto
    }

    /**
     *  Procesa la respuesta obtenida del AlertDialog y realiza
     *  la operación en base de datos correspondiente
     *
     *  @param subDepto
     *  @param operation
     */
    suspend fun processAlertResponse(subDepto: SubDeptoModel, operation : Operation) {

        if (operation == Operation.CANCEL){
            makeMessage("Acción cancelada", subDeptoFragmentContext)
            return
        }

        if (operation == Operation.DELETE){
            try {
                dataBaseHelper.deleteSubDeptoById(subDepto.idSubDepto!!)
                makeMessage("Subdepartamento eliminado con éxito", subDeptoFragmentContext)
            } catch (exception : Exception) {
                makeMessage("Error al eliminar", subDeptoFragmentContext)
            }
        }

        if (operation == Operation.UPDATE){
            try {
                dataBaseHelper.updateOneSubDepartamento(subDepto)
                makeMessage("SubDepto actualizado con éxito", subDeptoFragmentContext)
            } catch (exception : Exception) {
                makeMessage("Error al actualizar", subDeptoFragmentContext)
            }
        }
    }

    /**
     * Construye un mensaje para mostrar en pantalla
     */
    fun buildMessage(): String {
        val stringBuilder = StringBuilder()

        stringBuilder
            .appendLine("PARA BUSCAR UN SUBDEPARTAMENTO:\n")
            .appendLine("SE SELECCIONA SI ES POR <IDEFICIO O POR DESCRIPCION O POR DIVISIÓN>, Y DESPUES SE ANOTA EL NOMBRE EN EL CAMPO DE TEXTO, DAR CLICK EN BOTON DE BUSCAR Y SE MOSTRARAN LOS RESULTADOS.\n")
            .appendLine("PARA INSERTAR:\n")
            .appendLine("SE DEBE DE DAR CLIC A LA AREA EN EL LISTVIEW, YA SELECCIONADA AGREGAR LOS DATOS DEL SUBDEPARTAMENTO")
            .appendLine("\nPARA ELIMINAR Y ACTUALIZAR:")
            .appendLine("\nSE PRESIONA EL SUBDEPARTAMENTO(EN EL LISTVIEW) Y TE DARÁ LAS OPCIONES AUTOMATICAMENTE")

        return stringBuilder.toString()
    }
}