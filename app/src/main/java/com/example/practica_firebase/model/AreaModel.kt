package com.example.practica_firebase.model

class AreaModel
    (var idArea: Long = -1,
     var descripcion: String = "error",
     var division: String = "error",
     var cantEmpleados: Long = -1) {

    // toString
    override fun toString(): String {
        return "ID AREA: $idArea\n" +
                "Descripcion: $descripcion\n" +
                "Division: $division\n" +
                "# Empleados: $cantEmpleados"
    }
}