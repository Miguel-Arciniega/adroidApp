package com.example.practica_firebase.model

class SubDeptoModel
    (var idSubDepto: Long? = null,
     var idEdificio: String? = null,
     var piso: String? = null,
     var idArea: Long? = null) {

    // toString
    override fun toString(): String {
        return "ID AREA: $idArea -> " +
                "ID SubDepto: $idSubDepto -> " +
                "ID Edificio: $idEdificio -> " +
                "Piso: $piso"
    }
}

