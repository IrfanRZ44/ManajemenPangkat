package com.exomatik.manajemenpangkat.ui.pegawai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_biodata.*

class BiodataActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_biodata)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        savedData.getDataUser()?.let { setData(it) }
        onClick()
    }

    @SuppressLint("SimpleDateFormat")
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

    @SuppressLint("SetTextI18n")
    private fun setData(dataUser: ModelUser){
        etNip.editText?.setText(dataUser.nip)
        etNama.editText?.setText(dataUser.nama)
        etTempatLahir.editText?.setText("${dataUser.tempatLahir}, ${dataUser.tanggalLahir}")
        etJK.editText?.setText(dataUser.jenisKelamin)
        etPendidikanLast.editText?.setText(dataUser.pendidikanLast)
        etPangkat.editText?.setText(dataUser.gol)
        etTMTPangkat.editText?.setText(dataUser.tmtPangkat)
        etNoSKPangkat.editText?.setText(dataUser.nomorSKPangkat)
        etTglSkPangkat.editText?.setText(dataUser.tglSKPangkat)
    }
}