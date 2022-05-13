package com.example.practica_firebase.util

import android.R
import android.content.Context
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.util.ConstantsUtils.Companion.TYPE_AREA
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext

class FormUtils {
    companion object{

        /**
         *   Actualiza el list view
         *
         *   @param lvAreaView
         *   @param areas
         *   @param context
         */
        suspend fun updateListView(
            lvAreaView: ListView,
            list : List<Any>,
            type : String,
            context: Context
        ) {
            withContext(Main) {
                if (list.isNotEmpty()) {
                    lvAreaView.isClickable = true
                    lvAreaView.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, list)
                } else if (type == TYPE_AREA){
                    lvAreaView.isClickable = false
                    lvAreaView.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, listOf("No hay $type registradas"))
                } else {
                    lvAreaView.isClickable = false
                    lvAreaView.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, listOf("No hay $type registrados"))
                }
            }
        }

        /**
         *   Valida que el edit text no este vacios
         *
         *   @param field
         *   @throws ValidationException
         */
        fun validateTextField(field: Editable?){
            if (field.isNullOrBlank()) {
                throw ValidationException()
            }
        }


        /**
         *   Limpia un edit text
         *
         *   @param field
         */
        fun clearField(field: Editable?){
            if (field.isNullOrEmpty()){
                return
            }

            field.clear()
        }

        suspend fun makeMessage(message: String, context: Context){
            withContext(Main) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}