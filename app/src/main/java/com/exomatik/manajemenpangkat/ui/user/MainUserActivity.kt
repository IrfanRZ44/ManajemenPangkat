package com.exomatik.manajemenpangkat.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_main_user.*

class MainUserActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave

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
    }
}