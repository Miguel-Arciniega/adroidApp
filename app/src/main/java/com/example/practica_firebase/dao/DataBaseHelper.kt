package com.example.practica_firebase.dao

import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.model.SubDeptoModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class DataBaseHelper {

    private val db = Firebase.firestore

    /**
     *  Añadir nueva area
     */
    fun addOneArea(areaModel : AreaModel) {
        val id = (0..100).random().toString()
        db.collection(COLLECTION_AREAS).document(id).set(
            mapOf(
                FIELD_ID_AREA to id.toLong(),
                FIELD_DESCRIPCION to areaModel.descripcion,
                FIELD_DIVISION to areaModel.division,
                FIELD_CANT_EMPLEADOS to areaModel.cantEmpleados
            )
        )
    }

    /**
     *  Actualizar area
     */
    fun updateOneArea(area : AreaModel) {
        db.collection(COLLECTION_AREAS).document(area.idArea.toString()).update(
            mapOf(
                FIELD_ID_AREA to area.idArea,
                FIELD_DESCRIPCION to area.descripcion,
                FIELD_DIVISION to area.division,
                FIELD_CANT_EMPLEADOS to area.cantEmpleados
            )
        )
    }

    /**
     *  Obtener todas areas
     */
    suspend fun getAllAreas() : List<AreaModel>  {
        val returnList = ArrayList<AreaModel>()
        val query = db.collection(COLLECTION_AREAS).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                val newArea = document.toObject<AreaModel>()
                returnList.add(newArea!!)
            }
        }

        return returnList
    }


    /**
     *  Obtener areas por descripcion
     */
    suspend fun getAreasByDescription(description : String) : List<AreaModel> {

        val returnList = ArrayList<AreaModel>()
        val query = db.collection(COLLECTION_AREAS)
            .whereEqualTo(FIELD_DESCRIPCION, description).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                val newArea = document.toObject<AreaModel>()
                returnList.add(newArea!!)
            }
        }

        return returnList
    }

    /**
     *  Obtener areas por descripcion
     */
    suspend fun getAreasByDivision(division : String) : List<AreaModel> {

        val returnList = ArrayList<AreaModel>()
        val query = db.collection(COLLECTION_AREAS)
            .whereEqualTo(FIELD_DIVISION, division).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                val newArea = document.toObject<AreaModel>()
                returnList.add(newArea!!)
            }
        }

        return returnList
    }

    /**
     *  borrar area por idArea
     */
    suspend fun deleteAreaById (areaId : Long) {

        val idList = ArrayList<String>()
        val query = db.collection(COLLECTION_AREAS)
            .whereEqualTo(FIELD_ID_AREA, areaId).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                idList.add(document.id)
            }
        }

        for (id in idList){
            db.collection(COLLECTION_AREAS).document(id).delete()
            db.collection(COLLECTION_SUB_DEPTO).document(id).delete()
        }

        val querySubDepto = db.collection(COLLECTION_SUB_DEPTO)
            .whereEqualTo(FIELD_ID_AREA, areaId).get().await()

        val documentsSubDepto = querySubDepto.documents

        if (documentsSubDepto.isNotEmpty()) {
            for (document in documentsSubDepto) {
                idList.add(document.id)
            }
        }

        for (id in idList){
            db.collection(COLLECTION_SUB_DEPTO).document(id).delete()
        }
    }

    /**
     *  Añadir nuevo sub departamento
     */
    fun addOneSubDepto(subDeptoModel: SubDeptoModel) {
        val id = (0..100).random().toString()
        db.collection(COLLECTION_SUB_DEPTO).document(id).set(
            mapOf(
                FIELD_ID_SUBDEPTO to id.toLong(),
                FIELD_ID_EDIFICIO to subDeptoModel.idEdificio,
                FIELD_PISO to subDeptoModel.piso,
                FIELD_ID_AREA to subDeptoModel.idArea
            )
        )
    }

    /**
     *  Obtener todas las subdepartamentos
     */
    suspend fun getAllSubDeptos() : List<SubDeptoModel>  {
        val returnList = ArrayList<SubDeptoModel>()
        val query = db.collection(COLLECTION_SUB_DEPTO).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                val newSubDepto = document.toObject<SubDeptoModel>()
                returnList.add(newSubDepto!!)
            }
        }

        return returnList
    }

    /**
     *  Obtener subdepartamentos por descripcion
     */
    suspend fun getSubDeptoByDescription(description : String) : List<SubDeptoModel> {

        val returnList = ArrayList<SubDeptoModel>()
        val areas = getAreasByDescription(description)

        if (!areas.isNullOrEmpty()){

            val areaId = areas[0].idArea

            val query = db.collection(COLLECTION_SUB_DEPTO)
                .whereEqualTo(FIELD_ID_AREA, areaId).get().await()

            val documents = query.documents

            if (documents.isNotEmpty()) {
                for (document in documents) {
                    val newSubDepto = document.toObject<SubDeptoModel>()
                    returnList.add(newSubDepto!!)
                }
            }
        }

        return returnList
    }

    /**
     *  Obtener subdepartamentos por descripcion
     */
    suspend fun getSubDeptoByDivision(division : String) : List<SubDeptoModel> {

        val returnList = ArrayList<SubDeptoModel>()
        val areas = getAreasByDivision(division)

        if (!areas.isNullOrEmpty()){

            val areaId = areas[0].idArea

            val query = db.collection(COLLECTION_SUB_DEPTO)
                .whereEqualTo(FIELD_ID_AREA, areaId).get().await()

            val documents = query.documents

            if (documents.isNotEmpty()) {
                for (document in documents) {
                    val newSubDepto = document.toObject<SubDeptoModel>()
                    returnList.add(newSubDepto!!)
                }
            }
        }

        return returnList
    }

    /**
     *  Obtener subdepartamentos por descripcion
     */
    suspend fun getSubDeptoByIdEdificio(idEdificio : String) : List<SubDeptoModel> {

        val returnList = ArrayList<SubDeptoModel>()

        val query = db.collection(COLLECTION_SUB_DEPTO)
            .whereEqualTo(FIELD_ID_EDIFICIO, idEdificio).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                val newSubDepto = document.toObject<SubDeptoModel>()
                returnList.add(newSubDepto!!)
            }
        }

        return returnList
    }

    /**
     *  Borrar subDepartamento por idSubDepto
     */
    suspend fun deleteSubDeptoById(idSubDepto : Long) {

        val idList = ArrayList<String>()
        val query = db.collection(COLLECTION_SUB_DEPTO)
            .whereEqualTo(FIELD_ID_SUBDEPTO, idSubDepto).get().await()

        val documents = query.documents

        if (documents.isNotEmpty()) {
            for (document in documents) {
                idList.add(document.id)
            }
        }

        for (id in idList){
            db.collection(COLLECTION_SUB_DEPTO).document(id).delete()
        }
    }

    /**
     *  Actualizar subdepartamento
     */
    fun updateOneSubDepartamento(subDeptoModel : SubDeptoModel) {
        db.collection(COLLECTION_SUB_DEPTO).document(subDeptoModel.idSubDepto.toString()).update(
            mapOf(
                FIELD_ID_SUBDEPTO to subDeptoModel.idSubDepto,
                FIELD_ID_EDIFICIO to subDeptoModel.idEdificio,
                FIELD_PISO to subDeptoModel.piso,
                FIELD_ID_AREA to subDeptoModel.idArea
            )
        )
    }

    companion object DbConstants {

        const val COLLECTION_AREAS = "areas"
        const val COLLECTION_SUB_DEPTO = "sub_depto"

        const val FIELD_ID_AREA = "idArea"
        const val FIELD_DESCRIPCION = "descripcion"
        const val FIELD_DIVISION = "division"
        const val FIELD_CANT_EMPLEADOS = "cantEmpleados"

        const val FIELD_ID_SUBDEPTO = "idSubDepto"
        const val FIELD_ID_EDIFICIO = "idEdificio"
        const val FIELD_PISO = "piso"
    }
}