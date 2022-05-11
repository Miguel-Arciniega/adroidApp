package com.example.practica_firebase.util

import android.R
import android.content.Context
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.practica_firebase.dao.DataBaseHelper
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.model.AreaModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.RuntimeException

class FormUtils {
    companion object{

        /*
         *   Actualiza el list view
         *
         *   @param lvAreaView
         *   @param areas
         *   @param context
         */
        fun updateListView(
            lvAreaView: ListView,
            areas : List<AreaModel>,
            context: Context
        ) {
            val areaArrayAdapter: ArrayAdapter<AreaModel> =
                ArrayAdapter(context, R.layout.simple_list_item_1, areas)
            lvAreaView.adapter = areaArrayAdapter
        }

        /*
         *   Valida que los edit text no esten vacios
         *
         *   @param fields
         *   @throws ValidationException
         */
         fun validateTextFields(fields: List<Editable>){
            if (fields.isNullOrEmpty()){
                return
            }

            for (field in fields){
                if (field.isBlank()) {
                    throw ValidationException();
                }
            }
        }

        /*
         *   Limpia los edit text
         *
         *   @param fields
         */
         fun clearFields(fields: List<Editable>){
            if (fields.isNullOrEmpty()){
                return
            }

            fields.forEach{field -> field.clear()}
        }
    }
}