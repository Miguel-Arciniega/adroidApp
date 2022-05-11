package com.example.practica_firebase.dao

import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.COLLECTION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_CANT_EMPLEADOS
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DESCRIPTION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DIVISION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.model.AreaModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class DataBaseHelper {

    private val db = FirebaseFirestore.getInstance()

    // ---- Añadir nueva
    fun addOneArea(areaModel : AreaModel) {
        db.collection(COLLECTION).document().set(
            hashMapOf(
                FIELD_ID_AREA to getRandomID(),
                FIELD_DESCRIPTION to areaModel.descripcion,
                FIELD_DIVISION to areaModel.division,
                FIELD_CANT_EMPLEADOS to areaModel.cantEmpleados
            )
        )
    }

    // ---- Obtener todas areas
    fun getAllAreas() : ArrayList<AreaModel> = runBlocking {
        val returnList =   ArrayList<AreaModel>()

        try {
            withContext(Dispatchers.Default) {
                db.collection(COLLECTION).addSnapshotListener { documentos, error ->
                    if (error != null) {
                        throw RuntimeException()
                    }
                    for (documento in documentos!!) {
                        documento.getLong(FIELD_ID_AREA)
                        documento.getString(FIELD_DESCRIPTION)
                        documento.getString(FIELD_DIVISION)
                        val newArea = documento.toObject(AreaModel::class.java)
                        returnList.add(newArea)

                        println(newArea)
                    }
                }
            }
        } catch (e: RuntimeException) {
            println(e)
        }

        return@runBlocking returnList
    }

    // buscar por document(id)
    // ---- Obtener areas por id
    fun getAreasByAreaId(areaId : Int) : List<AreaModel> {
        var areas : List<AreaModel> = listOf()
        db.collection(COLLECTION).whereEqualTo("areaId", areaId).get()
            .addOnCompleteListener { document ->
                areas = document.result.toObjects(AreaModel::class.java)
                areas.forEach{area ->  println("---$area\n\n")}
            }
            .addOnFailureListener {
                throw RuntimeException()
            }
        return areas
    }

    private fun getRandomID(): Long {
        return (0..1000).random().toLong()
    }

    object DbConstants {

        const val COLLECTION = "mapeo_empresas"
        const val FIELD_ID_AREA = "idArea"
        const val FIELD_DESCRIPTION = "descripcion"
        const val FIELD_DIVISION = "division"
        const val FIELD_CANT_EMPLEADOS = "cantEmpleados"
        const val FIELD_ID_SUBDEPTO = "idSubDepto"
        const val FIELD_ID_EDIFICIO = "idEdificio"
        const val FIELD_PISO = "piso"
    }
}