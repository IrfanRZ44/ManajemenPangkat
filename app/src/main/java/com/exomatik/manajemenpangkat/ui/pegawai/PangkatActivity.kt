package com.exomatik.manajemenpangkat.ui.pegawai

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_pangkat.*

class PangkatActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pangkat)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        savedData.getDataUser()?.let { setData(it) }
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

    @SuppressLint("SetTextI18n")
    private fun setData(dataUser: ModelUser){
        etPangkatOld.editText?.setText(dataUser.pangkatOld)
        etGolOld.editText?.setText(dataUser.golOld)
        etTglSkPangkatOld.editText?.setText(dataUser.tglSKPangkatOld)
        etTMTPangkatOld.editText?.setText(dataUser.tmtPangkatOld)

        etPangkat.editText?.setText(dataUser.pangkat)
        etGol.editText?.setText(dataUser.gol)
        etTglSkPangkat.editText?.setText(dataUser.tglSKPangkat)
        etTMTPangkat.editText?.setText(dataUser.tmtPangkat)
    }
}