package com.example.practica_firebase.util

import android.R
import android.content.Context
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.practica_firebase.exception.ValidationException
import com.example.practica_firebase.model.AreaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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
            GlobalScope.launch(Dispatchers.IO) {
                val listAreas = async { areas }
                lvAreaView.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, listAreas.await())
            }
        }

        /*
         *   Valida que el edit text no este vacios
         *
         *   @param fields
         *   @throws ValidationException
         */
        fun validateTextField(field: Editable?){
            if (field.isNullOrEmpty()){
                return
            }

            if (field.isNullOrBlank()) {
                throw ValidationException();
            }
        }


        /*
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
    }
}