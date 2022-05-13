package com.example.practica_firebase.model

class AreaModel
    (var idArea: Long? = null,
     var descripcion: String? = null,
     var division: String? = null,
     var cantEmpleados: Long? = null) {

    // toString
    override fun toString(): String {
        return "ID AREA: $idArea\n" +
                "Descripcion: $descripcion\n" +
                "Division: $division\n" +
                "# Empleados: $cantEmpleados\n"
    }
}