package com.exomatik.manajemenpangkat.ui.pegawai

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome.MainPegawaiActivity
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
                        val data = snapshot.getValue(ModelUsulanPelaksana::class.java)
                        if (data != null && data.nip == dataUser.nip){
                            if (data.statusPengajuan == "AdminFakultas"){
                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Pelaksana)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiAdminFakultas.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Pelaksana)", data.disposisiAdminFakultas, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Pelaksana)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "Rektor"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Pelaksana)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiRektor.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", data.disposisiRektor, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Rektor (Pelaksana)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "BagianUmum"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Pelaksana)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", data.disposisiRektor, "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiBagianUmum.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", data.disposisiBagianUmum, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Bagian Umum (Pelaksana)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "BagianKepegawaian"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Pelaksana)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", data.disposisiRektor, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", data.disposisiBagianUmum, "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Pelaksana)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiBagianKepegawaian.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Pelaksana)", data.disposisiBagianKepegawaian, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Kepegawaian (Pelaksana)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "BKN"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Pelaksana)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Pelaksana)", data.disposisiRektor, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Pelaksana)", data.disposisiBagianUmum, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Pelaksana)", data.disposisiBagianKepegawaian, "", 1))
                                adapter?.notifyDataSetChanged()
                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Persetujuan BKN (Pelaksana)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiBKN.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Persetujuan BKN (Pelaksana)", data.disposisiBKN, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Persetujuan BKN (Pelaksana)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
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
                            if (data.statusPengajuan == "AdminFakultas"){
                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Struktural)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiAdminFakultas.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Struktural)", data.disposisiAdminFakultas, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglPengajuan, "Pengajuan Berkas (Struktural)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "Rektor"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Struktural)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiRektor.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", data.disposisiRektor, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Disposisi Rektor (Struktural)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "BagianUmum"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Struktural)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", data.disposisiRektor, "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiBagianUmum.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", data.disposisiBagianUmum, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Bagian Umum (Struktural)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "BagianKepegawaian"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Struktural)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", data.disposisiRektor, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", data.disposisiBagianUmum, "", 1))
                                adapter?.notifyDataSetChanged()

                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Struktural)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiBagianKepegawaian.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Struktural)", data.disposisiBagianKepegawaian, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Kepegawaian (Struktural)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            else if (data.statusPengajuan == "BKN"){
                                listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Pengajuan Berkas (Struktural)", data.disposisiAdminFakultas, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor (Struktural)", data.disposisiRektor, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum (Struktural)", data.disposisiBagianUmum, "", 1))
                                adapter?.notifyDataSetChanged()

                                listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian (Struktural)", data.disposisiBagianKepegawaian, "", 1))
                                adapter?.notifyDataSetChanged()
                                if (data.statusDitolak){
                                    listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Persetujuan BKN (Struktural)", "", data.catatanDitolak, 2))
                                    adapter?.notifyDataSetChanged()
                                }
                                else {
                                    if (data.disposisiBKN.isNotEmpty()){
                                        listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Persetujuan BKN (Struktural)", data.disposisiBKN, "", 1))
                                        adapter?.notifyDataSetChanged()
                                    }
                                    else{
                                        listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Persetujuan BKN (Struktural)", "", "", 3))
                                        adapter?.notifyDataSetChanged()
                                    }
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