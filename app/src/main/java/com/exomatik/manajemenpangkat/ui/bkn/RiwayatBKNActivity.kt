package com.exomatik.manajemenpangkat.ui.bkn

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_notifikasi_pegawai.*

class RiwayatBKNActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private var listProgress = ArrayList<ModelNotifikasiPegawai>()
    private var adapter: AdapterRiwayatBKN? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notifikasi_pegawai)
        myCodeHere()
    }

    @SuppressLint("SetTextI18n")
    private fun myCodeHere(){
        savedData = DataSave(this)
        adapter = AdapterRiwayatBKN(listProgress, this)
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
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = false
            listProgress.clear()
            adapter?.notifyDataSetChanged()
            getDataPelaksana()
            getDataStruktural()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, MainBKNActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainBKNActivity::class.java)
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
                        val data = snapshot.getValue(ModelUsulanPelaksana::class.java)
                        if (data != null){
                            if (data.statusPengajuan == "BKN"){
                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas ${data.nip} (Pelaksana)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Pengajuan Berkas ${data.nip} (Pelaksana)", data.disposisiBKN, "", 1))
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
            .getReference("DataBKN")
            .child("UsulanPelaksana")
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
                            if (data.statusPengajuan == "BKN"){
                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas ${data.nip} (Struktural)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Pengajuan Berkas ${data.nip} (Struktural)", data.disposisiBKN, "", 1))
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
            .getReference("DataBKN")
            .child("UsulanStruktural")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}