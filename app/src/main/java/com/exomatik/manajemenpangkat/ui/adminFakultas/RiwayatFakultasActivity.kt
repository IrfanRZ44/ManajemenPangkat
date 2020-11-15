package com.exomatik.manajemenpangkat.ui.adminFakultas

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_notifikasi_pegawai.*

class RiwayatFakultasActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private var listProgress = ArrayList<ModelNotifikasiPegawai>()
    private var adapter: AdapterRiwayatFakultas? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notifikasi_pegawai)
        myCodeHere()
    }

    @SuppressLint("SetTextI18n")
    private fun myCodeHere(){
        savedData = DataSave(this)
        adapter = AdapterRiwayatFakultas(listProgress, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        rcProgress.layoutManager = layoutManager
        rcProgress.adapter = adapter
        textTitle.text = "Riwayat"

        getDataPelaksana()
        getDataStruktural()
        onClick()
    }

    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainFakultasActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainFakultasActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getDataPelaksana(){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE

                if (listProgress.size == 0){
                    textStatus.visibility = View.VISIBLE
                }
                else{
                    textStatus.visibility = View.GONE
                }
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUsulanStruktural::class.java)
                        if (data != null){
                            if (data.statusAdminFakultas){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas ${data.nip}", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()
                            }
                            else{
                                if (data.memoAdminFakultas.isNotEmpty()){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas ${data.nip}", "", data.memoAdminFakultas, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }

                if (listProgress.size == 0){
                    textStatus.visibility = View.VISIBLE
                }
                else{
                    textStatus.visibility = View.GONE
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("DataAdminFakultas/UsulanPelaksana")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getDataStruktural(){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
                if (listProgress.size == 0){
                    textStatus.visibility = View.VISIBLE
                }
                else{
                    textStatus.visibility = View.GONE
                }
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUsulanStruktural::class.java)
                        if (data != null){
                            if (data.statusAdminFakultas){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas ${data.nip}", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()
                            }
                            else{
                                if (data.memoAdminFakultas.isNotEmpty()){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas ${data.nip}", "", data.memoAdminFakultas, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                            }
                        }
                    }
                }

                if (listProgress.size == 0){
                    textStatus.visibility = View.VISIBLE
                }
                else{
                    textStatus.visibility = View.GONE
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("DataAdminFakultas/UsulanStruktural")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}