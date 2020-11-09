package com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.ui.pegawai.MainPegawaiActivity
import com.exomatik.manajemenpangkat.ui.pegawai.StatusPengajuanActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_pengajuan_selesai.*

class PengajuanSelesaiActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pengajuan_selesai)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        onClick()
    }

    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainPegawaiActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnBack2.setOnClickListener {
            val intent = Intent(this, MainPegawaiActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnStatus.setOnClickListener {
            val intent = Intent(this, StatusPengajuanActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainPegawaiActivity::class.java)
        startActivity(intent)
        finish()
    }
}