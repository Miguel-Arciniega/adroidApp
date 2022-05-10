package com.example.practica_firebase.dao

import android.content.Context
import android.widget.Toast
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.COLLECTION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_CANT_EMPLEADOS
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DESCRIPTION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_DIVISION
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.util.ConstantsUtils
import com.google.firebase.firestore.FirebaseFirestore


class DataBaseHelper(context: Context) {

    private val db = FirebaseFirestore.getInstance()
    private val classContext = context

    // ---- Area

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

    fun getAllAreas() : List<AreaModel> {
        val returnList = ArrayList<AreaModel>()

        db.collection(COLLECTION).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    returnList.add(document.toObject(AreaModel::class.java))
                }
            }
            .addOnFailureListener {
                Toast.makeText(classContext, ConstantsUtils.FAILURE_TO_GET_AREAS, Toast.LENGTH_SHORT).show()
            }
        return returnList
    }

    private fun getRandomID(): Int {
        return (0..1000).random()
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