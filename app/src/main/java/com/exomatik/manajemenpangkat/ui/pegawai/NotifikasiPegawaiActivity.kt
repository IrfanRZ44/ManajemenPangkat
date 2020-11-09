package com.exomatik.manajemenpangkat.ui.pegawai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_notifikasi_pegawai.*
import kotlinx.android.synthetic.main.activity_notifikasi_pegawai.btnBack
import kotlinx.android.synthetic.main.activity_notifikasi_pegawai.progress

class NotifikasiPegawaiActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    var listProgress = ArrayList<ModelNotifikasiPegawai>()
    var adapter: AdapterNotifikasiPegawai? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notifikasi_pegawai)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        adapter = AdapterNotifikasiPegawai(listProgress, this)
        rcProgress.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcProgress.adapter = adapter

        savedData.getDataUser()?.let { getDataPelaksana(it) }
        onClick()
    }

    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainPegawaiActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainPegawaiActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    private fun getDataPelaksana(dataUser: ModelUser){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
                getDataStruktural(dataUser)
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanStruktural::class.java)
                    if (data != null){
                        if (data.statusPengajuan){
                            listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas", true))
                            adapter?.notifyDataSetChanged()
                            textStatus.visibility = View.GONE

                            if (data.statusAdminFakultas){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas", true))
                                adapter?.notifyDataSetChanged()
                                textStatus.visibility = View.GONE
                                if (data.statusRektor){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", true))
                                    adapter?.notifyDataSetChanged()
                                    textStatus.visibility = View.GONE
                                    if (data.statusBagianUmum){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", true))
                                        adapter?.notifyDataSetChanged()
                                        textStatus.visibility = View.GONE
                                        if (data.statusBagianKepegawaian){
                                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", true))
                                            adapter?.notifyDataSetChanged()
                                            textStatus.visibility = View.GONE
                                            if (data.statusBKN){
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN", true))
                                                adapter?.notifyDataSetChanged()
                                                textStatus.visibility = View.GONE
                                            }
                                            else{
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN", false))
                                                adapter?.notifyDataSetChanged()
                                                textStatus.visibility = View.GONE
                                            }
                                        }
                                        else{
                                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", false))
                                            adapter?.notifyDataSetChanged()
                                            textStatus.visibility = View.GONE
                                        }
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", false))
                                        adapter?.notifyDataSetChanged()
                                        textStatus.visibility = View.GONE
                                    }
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", false))
                                }
                            }
                            else{
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas", false))
                                adapter?.notifyDataSetChanged()
                                textStatus.visibility = View.GONE
                            }
                        }
                        else{
                            listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas", false))
                            adapter?.notifyDataSetChanged()
                            textStatus.visibility = View.GONE
                        }
                    }
                }
                else{
                    getDataStruktural(dataUser)
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanPelaksana")
            .child(dataUser.nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun getDataStruktural(dataUser: ModelUser){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
                textStatus.text = "Anda belum memiliki daftar pengajuan berkas"
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanStruktural::class.java)
                    if (data != null){
                        if (data.statusPengajuan){
                            listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas", true))
                            adapter?.notifyDataSetChanged()
                            textStatus.visibility = View.GONE
                            if (data.statusAdminFakultas){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas", true))
                                adapter?.notifyDataSetChanged()
                                textStatus.visibility = View.GONE
                                if (data.statusRektor){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", true))
                                    adapter?.notifyDataSetChanged()
                                    textStatus.visibility = View.GONE
                                    if (data.statusBagianUmum){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", true))
                                        adapter?.notifyDataSetChanged()
                                        textStatus.visibility = View.GONE
                                        if (data.statusBagianKepegawaian){
                                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", true))
                                            adapter?.notifyDataSetChanged()
                                            textStatus.visibility = View.GONE
                                            if (data.statusBKN){
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN", true))
                                                adapter?.notifyDataSetChanged()
                                                textStatus.visibility = View.GONE
                                            }
                                            else{
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN", false))
                                                adapter?.notifyDataSetChanged()
                                                textStatus.visibility = View.GONE
                                            }
                                        }
                                        else{
                                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", false))
                                            adapter?.notifyDataSetChanged()
                                            textStatus.visibility = View.GONE
                                        }
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", false))
                                        adapter?.notifyDataSetChanged()
                                        textStatus.visibility = View.GONE
                                    }
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", false))
                                }
                            }
                            else{
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas", false))
                                adapter?.notifyDataSetChanged()
                                textStatus.visibility = View.GONE
                            }
                        }
                        else{
                            listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas", false))
                            adapter?.notifyDataSetChanged()
                            textStatus.visibility = View.GONE
                        }
                    }
                }
                else{
                    textStatus.text = "Anda belum memiliki daftar pengajuan berkas"
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .child(dataUser.nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }
}