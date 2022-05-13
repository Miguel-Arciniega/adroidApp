package com.example.practica_firebase.ui.modal

import android.app.AlertDialog
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_firebase.R
import com.example.practica_firebase.dao.DataBaseHelper.DbConstants.FIELD_ID_AREA
import com.example.practica_firebase.databinding.ModalMostrarAreasBinding
import com.example.practica_firebase.model.AreaModel
import com.example.practica_firebase.services.AreaService
import com.example.practica_firebase.ui.fragments.SubDeptoFragment
import com.example.practica_firebase.ui.fragments.SubDeptoFragment.Companion.getInstance
import com.example.practica_firebase.util.ConstantsUtils
import com.example.practica_firebase.util.ConstantsUtils.Companion.TYPE_AREA
import com.example.practica_firebase.util.FormUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ModalMostrarAreas : AppCompatActivity() {

    lateinit var binding: ModalMostrarAreasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ModalMostrarAreasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val context = this

        // Inicializar Servicio
        val areaService =
            AreaService(this)

        val lvAreaView = binding.lvLista
        val btnVolver = binding.btnVolver

        // Actualiza el list view de areas al iniciar
        CoroutineScope(Dispatchers.IO).launch {
            FormUtils.updateListView(
                lvAreaView,
                areaService.getAreasFromDataBase(),
                TYPE_AREA,
                context
            )
        }

        btnVolver.setOnClickListener {
            finish()
        }


    }

    private fun showUpdateModal(area: AreaModel) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, getInstance(area.idArea.toString())).commit()
    }
}