package com.exomatik.manajemenpangkat.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_biodata.*

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
            val intent = Intent(this, MainUserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(dataUser: ModelUser){
        Log.e("Pangkat", dataUser.newNip)
    }
}