package com.example.practica_firebase.model

class AreaModel
    (var idArea: Int = -1, var descripcion: String? = null, var division: String? = null, var cantEmpleados: Int? = null, var subDepartamento: SubDeptoModel? = null) {

    // toString
    override fun toString(): String {
        return "ID AREA: $idArea\n" +
                "Descripcion: $descripcion\n" +
                "Division: $division\n" +
                "# Empleados: $cantEmpleados"
    }
}