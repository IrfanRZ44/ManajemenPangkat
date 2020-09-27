package com.exomatik.manajemenpangkat.ui.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.user.daftarUsulan.UsulanBiodataActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_main_user.*


class MainUserActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_user)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        savedData.getDataUser()?.let { setData(it) }
        onClick()
    }

    private fun setData(dataUser: ModelUser){
        textNama.text = dataUser.nama
        textNip.text = dataUser.newNip
        textJabatan.text = dataUser.jabatan
        textGol.text = dataUser.gol
    }

    private fun onClick() {
        btnBiodata.setOnClickListener {
            val intent = Intent(this, BiodataActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnGol.setOnClickListener {
            val intent = Intent(this, PangkatActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnJabatan.setOnClickListener {
            val intent = Intent(this, JabatanActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnUsulan.setOnClickListener {
            val intent = Intent(this, UsulanBiodataActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        if (exit) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(this, "Tekan Cepat 2 Kali untuk Keluar", Toast.LENGTH_SHORT).show()
            exit = true
            Handler().postDelayed({ exit = false }, 2000)
        }
    }
}