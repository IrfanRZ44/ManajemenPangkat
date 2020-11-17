package com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.ui.pegawai.BiodataActivity
import com.exomatik.manajemenpangkat.ui.pegawai.PangkatActivity
import com.exomatik.manajemenpangkat.ui.pegawai.StatusPengajuanActivity
import com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan.UsulKPActivity
import com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan.UsulanPelaksanaActivity
import com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan.UsulanStrukturalActivity
import com.exomatik.manajemenpangkat.ui.pegawai.fragmentArsip.ArsipPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home_pegawai.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomePegawaiFragment : Fragment() {
    private lateinit var savedData : DataSave
    private lateinit var v : View
    @SuppressLint("SimpleDateFormat")
    private val tglPengajuan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy"))
    } else {
        SimpleDateFormat("dd-M-yyyy").format(Date())
    }

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_home_pegawai, paramViewGroup, false)

        savedData = DataSave(context)
        myCodeHere()
        return v
    }

    private fun myCodeHere(){
        savedData.getDataUser()?.let { setData(it) }
        onClick()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(dataUser: ModelUser){
        v.textNama.text = dataUser.nama
        v.textNip.text = dataUser.nip
        v.textJabatan.text = "Jabatan"
        v.textValueJabatan.text = dataUser.gol
    }

    private fun onClick() {
        v.btnBiodata.setOnClickListener {
            val intent = Intent(activity, BiodataActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

        v.btnPangkat.setOnClickListener {
            val intent = Intent(activity, PangkatActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

        v.btnStatus.setOnClickListener {
            val intent = Intent(activity, StatusPengajuanActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

        v.btnUsulan.setOnClickListener {
            val dataUser = savedData.getDataUser()
            dataUser?.let { it1 -> checkUsulanPelaksana(it1) }
        }

        v.btnArsip.setOnClickListener {
            val intent = Intent(activity, ArsipPegawaiActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }
    }

    private fun checkUsulanPelaksana(dataUser: ModelUser){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE

                checkUsulanStruktural(dataUser)
            }

            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    var action = false
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUsulanPelaksana::class.java)

                        if (data != null && data.statusPengajuan.isNotEmpty() && data.pangkatAwal.isNotEmpty()
                            && data.pangkatUsulan.isNotEmpty() && data.tglPengajuan == tglPengajuan){

                            action = if (data.statusDitolak) {
                                val intent = Intent(activity, UsulanPelaksanaActivity::class.java)
                                intent.putExtra("awal", data.pangkatAwal)
                                intent.putExtra("usul", data.pangkatUsulan)
                                activity?.startActivity(intent)
                                activity?.finish()
                                true
                            } else{
                                Toast.makeText(activity, "Maaf, Anda sudah mengajukan berkas.. Harap cek status pengajuan Anda", Toast.LENGTH_LONG).show()
                                true
                            }
                        }
                    }

                    if(!action){
                        checkUsulanStruktural(dataUser)
                    }
                }
                else{
                    checkUsulanStruktural(dataUser)
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanPelaksana")
            .orderByChild("nip")
            .equalTo(dataUser.nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun checkUsulanStruktural(dataUser: ModelUser){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE

                val intent = Intent(activity, UsulKPActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }

            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    var action = false
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUsulanStruktural::class.java)

                        if (data != null && data.statusPengajuan.isNotEmpty() && data.pangkatAwal.isNotEmpty()
                            && data.pangkatUsulan.isNotEmpty() && data.tglPengajuan == tglPengajuan){

                            action = if (data.statusDitolak){
                                val intent = Intent(activity, UsulanStrukturalActivity::class.java)
                                intent.putExtra("awal", data.pangkatAwal)
                                intent.putExtra("usul", data.pangkatUsulan)
                                activity?.startActivity(intent)
                                activity?.finish()
                                true
                            } else{
                                Toast.makeText(activity, "Maaf, Anda sudah mengajukan berkas.. Harap cek status pengajuan Anda", Toast.LENGTH_LONG).show()
                                true
                            }
                        }
                    }

                    if(!action){
                        val intent = Intent(activity, UsulKPActivity::class.java)
                        activity?.startActivity(intent)
                        activity?.finish()
                    }
                }
                else{
                    val intent = Intent(activity, UsulKPActivity::class.java)
                    activity?.startActivity(intent)
                    activity?.finish()
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