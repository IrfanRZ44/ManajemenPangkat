package com.exomatik.manajemenpangkat.ui.pegawai

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

class NotifikasiPegawaiActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private var listProgress = ArrayList<ModelNotifikasiPegawai>()
    private var adapter: AdapterNotifikasiPegawai? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_notifikasi_pegawai)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        adapter = AdapterNotifikasiPegawai(listProgress, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        rcProgress.layoutManager = layoutManager
        rcProgress.adapter = adapter

        savedData.getDataUser()?.let {
            getDataPelaksana(it)
            getDataStruktural(it)
        }
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

    private fun getDataPelaksana(dataUser: ModelUser){
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
                        if (data != null && data.nip == dataUser.nip){
                            if (data.statusPengajuan){
                                listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Pelaksana)", "", "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusAdminFakultas){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas (Pelaksana)", data.disposisiAdminFakultas, "", 1))
                                    adapter?.notifyDataSetChanged()
                                    if (data.statusRektor){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", data.disposisiRektor, "", 1))
                                        adapter?.notifyDataSetChanged()
                                        if (data.statusBagianUmum){
                                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", data.disposisiBagianUmum, "", 1))
                                            adapter?.notifyDataSetChanged()
                                            if (data.statusBagianKepegawaian){
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Pelaksana)", data.disposisiBagianKepegawaian, "", 1))
                                                adapter?.notifyDataSetChanged()
                                                if (data.statusBKN){
                                                    listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN (Pelaksana)", data.disposisiBKN, "", 1))
                                                    adapter?.notifyDataSetChanged()
                                                }
                                                else{
                                                    if (data.memoBKN.isNotEmpty()){
                                                        listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN (Pelaksana)", "", data.memoBKN, 2))
                                                        adapter?.notifyDataSetChanged()
                                                    }
                                                    else{
                                                        listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN (Pelaksana)", "", "", 3))
                                                        adapter?.notifyDataSetChanged()
                                                    }
                                                }
                                            }
                                            else{
                                                if (data.memoBagianKepegawaian.isNotEmpty()){
                                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Pelaksana)", "", data.memoBagianKepegawaian ,2))
                                                    adapter?.notifyDataSetChanged()
                                                }
                                                else{
                                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Pelaksana)", "", "", 3))
                                                    adapter?.notifyDataSetChanged()
                                                }
                                            }
                                        }
                                        else{
                                            if (data.memoBagianUmum.isNotEmpty()){
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", "", data.memoBagianUmum, 2))
                                                adapter?.notifyDataSetChanged()
                                            }
                                            else{
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", "", "", 3))
                                                adapter?.notifyDataSetChanged()
                                            }
                                        }
                                    }
                                    else{
                                        if (data.memoRektor.isNotEmpty()){
                                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", "", data.memoRektor, 2))
                                            adapter?.notifyDataSetChanged()
                                        }
                                        else{
                                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", "", "", 3))
                                            adapter?.notifyDataSetChanged()
                                        }
                                    }
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas (Pelaksana)", "", "", 3))
                                    adapter?.notifyDataSetChanged()
                                }
                            }
                            else{
                                if (data.memoAdminFakultas.isNotEmpty()){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Pelaksana)", "", data.memoAdminFakultas, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Pelaksana)", "", "", 3))
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
            .getReference("UsulanPelaksana")
            .orderByChild("nip")
            .equalTo(dataUser.nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getDataStruktural(dataUser: ModelUser){
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
                        if (data != null && data.nip == dataUser.nip){
                            if (data.statusPengajuan){
                                listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Struktural)", "", "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusAdminFakultas){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas (Struktural)", data.disposisiAdminFakultas, "", 1))
                                    adapter?.notifyDataSetChanged()
                                    if (data.statusRektor){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", data.disposisiRektor, "", 1))
                                        adapter?.notifyDataSetChanged()
                                        if (data.statusBagianUmum){
                                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", data.disposisiBagianUmum, "", 1))
                                            adapter?.notifyDataSetChanged()
                                            if (data.statusBagianKepegawaian){
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Struktural)", data.disposisiBagianKepegawaian, "", 1))
                                                adapter?.notifyDataSetChanged()
                                                if (data.statusBKN){
                                                    listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN (Struktural)", data.disposisiBKN, "", 1))
                                                    adapter?.notifyDataSetChanged()
                                                }
                                                else{
                                                    if (data.memoBKN.isNotEmpty()){
                                                        listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN (Struktural)", "", data.memoBKN, 2))
                                                        adapter?.notifyDataSetChanged()
                                                    }
                                                    else{
                                                        listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN (Struktural)", "", "", 3))
                                                        adapter?.notifyDataSetChanged()
                                                    }
                                                }
                                            }
                                            else{
                                                if (data.memoBagianKepegawaian.isNotEmpty()){
                                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Struktural)", "", data.memoBagianKepegawaian ,2))
                                                    adapter?.notifyDataSetChanged()
                                                }
                                                else{
                                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Struktural)", "", "", 3))
                                                    adapter?.notifyDataSetChanged()
                                                }
                                            }
                                        }
                                        else{
                                            if (data.memoBagianUmum.isNotEmpty()){
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", "", data.memoBagianUmum, 2))
                                                adapter?.notifyDataSetChanged()
                                            }
                                            else{
                                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", "", "", 3))
                                                adapter?.notifyDataSetChanged()
                                            }
                                        }
                                    }
                                    else{
                                        if (data.memoRektor.isNotEmpty()){
                                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", "", data.memoRektor, 2))
                                            adapter?.notifyDataSetChanged()
                                        }
                                        else{
                                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", "", "", 3))
                                            adapter?.notifyDataSetChanged()
                                        }
                                    }
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Admin Fakultas (Struktural)", "", "", 3))
                                    adapter?.notifyDataSetChanged()
                                }
                            }
                            else{
                                if (data.memoAdminFakultas.isNotEmpty()){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Struktural)", "", data.memoAdminFakultas, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else{
                                    listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Struktural)", "", "", 3))
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
            .getReference("UsulanStruktural")
            .orderByChild("nip")
            .equalTo(dataUser.nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }
}