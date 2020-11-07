package com.exomatik.manajemenpangkat.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.ui.pegawai.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave

class SplashActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private lateinit var timerCekKoneksi: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)

        timerCekKoneksi = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (savedData.getDataUser()?.nip.isNullOrEmpty()){
                    moveToLogin()
                }
                else{
                    moveToMainUser()
                }
            }
        }.start()
    }

    private fun moveToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToMainUser(){
        val intent = Intent(this, MainPegawaiActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerCekKoneksi.cancel()
    }
}