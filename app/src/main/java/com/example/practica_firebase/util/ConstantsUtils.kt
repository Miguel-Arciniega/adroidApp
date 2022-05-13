package com.example.practica_firebase.util

class ConstantsUtils {
    companion object{

        const val BTN_ACCEPT = "ACEPTAR"
        const val SPINNER_VALUE_DIVISION = "DIVISION"
        const val SPINNER_VALUE_DESCRIPCION = "DESCRIPCION"
        const val SPINNER_VALUE_ID_EDIFICIO = "ID EDIFICIO"

        const val TYPE_AREA = "Areas"
        const val TYPE_SUB_DEPTO = "Subdepartamentos"

        // Mensajes
        const val TITLE = "INFORMACIÓN"
        const val INSERT_VALIDATION_MESSAGE = "Porfavor llena todos los campos antes de insertar"
        const val SEARCH_VALIDATION_MESSAGE = "Porfavor ingresa un valor para buscar"
        const val AREA_NOT_SELECTED_FOUND = "Porfavor, selecciona un area antes de insertar"

        // AlertDialog
        const val ALERT_DIALOG_TITLE_AVISO = "AVISO"
        const val ALERT_DIALOG_OPERATION_MESSAGE = "¿Que operación deseas realizar?"
        const val ALERT_DIALOG_SELECT_MESSAGE = "¿Esta seguro que quiere seleccionar esta area?"
        const val ALERT_DIALOG_OPERATION_ACTUALIZAR = "ACTUALIZAR"
        const val ALERT_DIALOG_OPERATION_ELIMINAR = "ELIMINAR"
        const val ALERT_DIALOG_OPERATION_CANCELAR = "CANCELAR"
        const val ALERT_DIALOG_OPERATION_ACEPTAR = "ACEPTAR"

        // Campos
        const val ET_ID_AREA = "etIdArea"
        const val ET_DESCRIPCION = "etDescripcion"
        const val ET_DIVISION = "etDivision"
        const val ET_CANT_EMPLEADOS= "etCantEmpleados"
        const val ET_ID_EDIFICIO = "etIdEdificio"
        const val ET_PISO = "etPiso"
        const val ET_ID_SUBDEPTO = "etIdSubDepto"

        // Constantes para la base de datos
        const val FAIL_WHEN_TRYING_TO_INSERT_AREA = "Hubo un problema al intentar agregar la nueva area"
        const val FAIL_WHEN_TRYING_TO_GET_AREAS = "Hubo un problema al obtener las area"
        const val FAIL_WHEN_TRYING_TO_INSERT_SUB_DEPTO = "Hubo un problema al intentar agregar el nuevo sub departamento"
        const val FAIL_WHEN_TRYING_TO_GET_SUB_DEPTO = "Hubo un problema al obtener los sub departamentos"
        const val AREA_SUCCESSFULLY_ADDED = "Area agregada con éxito"
        const val SUB_DEPTO_SUCCESSFULLY_ADDED = "Sub departamento agregado con éxito"
        const val NO_RESULTS_FOUND_AREAS = "No se encontraron areas que coincidan con su criterio de busqueda"
        const val NO_RESULTS_FOUND_SUB_DEPTO = "No se encontraron subdepartamentos que coincidan con su criterio de busqueda"

    }
}