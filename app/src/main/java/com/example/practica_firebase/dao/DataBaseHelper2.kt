package com.example.practica_firebase.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.model.SubDeptoModel


class DataBaseHelper2(context: Context?) :
    SQLiteOpenHelper(context, "area.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableAreaStatement =
            "CREATE TABLE ${DbConstants.TABLE_AREA} (" +
                "${DbConstants.COLUMN_ID_AREA} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${DbConstants.COLUMN_DESCRIPTION} TEXT, " +
                "${DbConstants.COLUMN_DIVISION} TEXT, " +
                "${DbConstants.COLUMN_CANT_EMPLEADOS} INT " +
            ")"

        val createTableSubDepartamentoStatement =
            "CREATE TABLE ${DbConstants.TABLE_SUBDEPARTAMENTO} (" +
                    "${DbConstants.COLUMN_ID_SUBDEPTO} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${DbConstants.COLUMN_ID_EDIFICIO} TEXT, " +
                    "${DbConstants.COLUMN_PISO} TEXT, " +
                    "${DbConstants.COLUMN_ID_AREA} INT, " +
                    "FOREIGN KEY (${DbConstants.COLUMN_ID_AREA})" +
                    "REFERENCES ${DbConstants.TABLE_AREA} (${DbConstants.COLUMN_ID_AREA})" +
            ")"

        db.execSQL(createTableAreaStatement)
        db.execSQL(createTableSubDepartamentoStatement)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {}

    // ---- Area

    fun addOneArea(areaModel : AreaModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(DbConstants.COLUMN_DESCRIPTION, areaModel.descripcion)
        cv.put(DbConstants.COLUMN_DIVISION, areaModel.division)
        cv.put(DbConstants.COLUMN_CANT_EMPLEADOS, areaModel.cantEmpleados)

        val insert = db.insert(DbConstants.TABLE_AREA, null, cv)

        return insert.compareTo(-1) != 0
    }

    fun getAllAreas() : List<AreaModel>{
        val returnList = ArrayList<AreaModel>()
        val queryString =
            "SELECT * " +
            "FROM ${DbConstants.TABLE_AREA}"

        val db = this.readableDatabase

        val cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()){
            do {
                val areaId = cursor.getLong(0)
                val description = cursor.getString(1)
                val division = cursor.getString(2)
                val cantidadEmpleados = cursor.getLong(3)

                val nuevaArea = AreaModel(areaId, description, division, cantidadEmpleados)
                returnList.add(nuevaArea)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return  returnList
    }

    fun getAreasByDescripcion(descripcion: String) : List<AreaModel> {
        val returnList = ArrayList<AreaModel>()
        val queryString =
            "SELECT * " +
            "FROM ${DbConstants.TABLE_AREA} " +
            "WHERE ${DbConstants.COLUMN_DESCRIPTION} = '$descripcion'"

        val db = this.readableDatabase

        val cursor = db.rawQuery(queryString, null)

        var nuevaArea: AreaModel?

        if (cursor.moveToFirst()){
            do {
                val areaId = cursor.getLong(0)
                val description = cursor.getString(1)
                val division = cursor.getString(2)
                val cantidadEmpleados = cursor.getLong(3)

                nuevaArea = AreaModel(areaId, description, division, cantidadEmpleados)
                returnList.add(nuevaArea)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return returnList
    }

    fun getAreasByDivision(division: String) : List<AreaModel>{
        val returnList = ArrayList<AreaModel>()
        val queryString =
            "SELECT * " +
            "FROM ${DbConstants.TABLE_AREA} " +
            "WHERE ${DbConstants.COLUMN_DIVISION} = '$division'"

        val db = this.readableDatabase

        val cursor = db.rawQuery(queryString, null)

        if (cursor.moveToFirst()){
            do {
                val areaId = cursor.getLong(0)
                val description = cursor.getString(1)
                val division = cursor.getString(2)
                val cantidadEmpleados = cursor.getLong(3)

                val nuevaArea = AreaModel(areaId, description, division, cantidadEmpleados)
                returnList.add(nuevaArea)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return  returnList
    }

    fun deleteOneArea(areaModel: AreaModel): Boolean {
        val db = this.writableDatabase
        val queryString =
            "DELETE FROM ${DbConstants.TABLE_AREA} " +
            "WHERE ${DbConstants.COLUMN_ID_AREA} = ${areaModel.idArea}"

        val cursor = db.rawQuery(queryString, null)

        return cursor.moveToFirst()
    }

    // ---- SubDepto

    fun addOneSubDepto(subDeptoModel : SubDeptoModel): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(DbConstants.COLUMN_ID_SUBDEPTO, subDeptoModel.idSubDepto)
        cv.put(DbConstants.COLUMN_ID_EDIFICIO, subDeptoModel.idEdificio)
        cv.put(DbConstants.COLUMN_PISO, subDeptoModel.piso)
        // cv.put(DbConstants.COLUMN_ID_AREA, subDeptoModel.idArea)

        val insert = db.insert(DbConstants.TABLE_SUBDEPARTAMENTO, null, cv)

        return insert.compareTo(-1) != 0
    }

    fun getAllSubDepto() : List<SubDeptoModel>{
        val returnList = ArrayList<SubDeptoModel>()
        val queryString =
            "SELECT * " +
            "FROM ${DbConstants.TABLE_SUBDEPARTAMENTO}"

        val db = this.readableDatabase

        val cursor = db.rawQuery(queryString, null)

        var nuevoSubDepto: SubDeptoModel? = null

        if (cursor.moveToFirst()){
            do {
                val subDeptoId = cursor.getLong(0)
                val idEdificio = cursor.getString(1)
                val piso = cursor.getString(2)
                val idArea = cursor.getLong(3)

                nuevoSubDepto = SubDeptoModel(subDeptoId, idEdificio, piso)
                returnList.add(nuevoSubDepto)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return  returnList
    }

    fun getOneSubDepto(id: Int) : SubDeptoModel? {
        val queryString =
            "SELECT * " +
            "FROM ${DbConstants.TABLE_SUBDEPARTAMENTO}" +
            "WHERE ${DbConstants.COLUMN_ID_SUBDEPTO} = $id"

        val db = this.readableDatabase

        val cursor = db.rawQuery(queryString, null)

        var nuevoSubDepto: SubDeptoModel? = null

        if (cursor.moveToFirst()){
            val subDeptoId = cursor.getLong(0)
            val idEdificio = cursor.getString(1)
            val piso = cursor.getString(2)
            val idArea = cursor.getLong(3)

            nuevoSubDepto = SubDeptoModel(subDeptoId, idEdificio, piso)
        }

        cursor.close()
        db.close()
        return nuevoSubDepto
    }

    fun deleteOneSubDepto(subDeptoModel: SubDeptoModel): Boolean {
        val db = this.writableDatabase
        val queryString =
            "DELETE FROM ${DbConstants.TABLE_SUBDEPARTAMENTO} " +
            "WHERE ${DbConstants.COLUMN_ID_SUBDEPTO} = ${subDeptoModel.idSubDepto}"

        val cursor = db.rawQuery(queryString, null)

        return cursor.moveToFirst()
    }


    object DbConstants {

        const val TABLE_AREA = "AREA"
        const val COLUMN_ID_AREA = "IDAREA"
        const val COLUMN_DESCRIPTION = "DESCRIPCION"
        const val COLUMN_DIVISION = "DIVISION"
        const val COLUMN_CANT_EMPLEADOS = "CANTIDAD_EMPLEADOS"

        const val TABLE_SUBDEPARTAMENTO = "SUBDEPARTAMENTO"
        const val COLUMN_ID_SUBDEPTO = "IDSUBDEPTO"
        const val COLUMN_ID_EDIFICIO = "IDEDIFICIO"
        const val COLUMN_PISO = "PISO"
    }
}