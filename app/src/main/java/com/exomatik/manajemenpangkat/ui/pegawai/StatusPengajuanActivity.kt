package com.exomatik.manajemenpangkat.ui.pegawai

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_status_pengajuan.*

class StatusPengajuanActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_status_pengajuan)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        savedData.getDataUser()?.let { setDataPelaksana(it) }
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

    @Suppress("DEPRECATION")
    private fun setDataPelaksana(dataUser: ModelUser){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
                setDataStruktural(dataUser)
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanPelaksana::class.java)
                    if (data != null){
                        when (data.statusPengajuan) {
                            "AdminFakultas" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus2.text = ""
                                    textStatus2.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status2.setImageResource(R.color.gray1)
                                }
                                else{
                                    status2.setImageResource(R.color.green5)
                                }
                            }
                            "Rektor" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus3.text = ""
                                    textStatus3.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status3.setImageResource(R.color.gray1)
                                }
                                else{
                                    status3.setImageResource(R.color.green5)
                                }
                            }
                            "BagianUmum" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                textStatus3.text = ""
                                textStatus3.background = resources.getDrawable(R.drawable.ic_true_white)
                                status3.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus4.text = ""
                                    textStatus4.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status4.setImageResource(R.color.gray1)
                                }
                                else{
                                    status4.setImageResource(R.color.green5)
                                }
                            }
                            "BagianKepegawaian" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                textStatus3.text = ""
                                textStatus3.background = resources.getDrawable(R.drawable.ic_true_white)
                                status3.setImageResource(R.color.blue4)

                                textStatus4.text = ""
                                textStatus4.background = resources.getDrawable(R.drawable.ic_true_white)
                                status4.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus5.text = ""
                                    textStatus5.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status5.setImageResource(R.color.gray1)
                                }
                                else{
                                    status5.setImageResource(R.color.green5)
                                }
                            }
                            "BKN" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                textStatus3.text = ""
                                textStatus3.background = resources.getDrawable(R.drawable.ic_true_white)
                                status3.setImageResource(R.color.blue4)

                                textStatus4.text = ""
                                textStatus4.background = resources.getDrawable(R.drawable.ic_true_white)
                                status4.setImageResource(R.color.blue4)

                                textStatus5.text = ""
                                textStatus5.background = resources.getDrawable(R.drawable.ic_true_white)
                                status5.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus6.text = ""
                                    textStatus6.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status6.setImageResource(R.color.gray1)
                                }
                                else{
                                    if (data.disposisiBKN.isEmpty()){
                                        status6.setImageResource(R.color.green5)
                                    }
                                    else{
                                        textStatus6.text = ""
                                        textStatus6.background = resources.getDrawable(R.drawable.ic_true_white)
                                        status6.setImageResource(R.color.blue4)
                                    }
                                }
                            }
                        }
                    }
                }
                else{
                    setDataStruktural(dataUser)
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanPelaksana")
            .child("${dataUser.nip}__${dataUser.tglPengajuan}")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    @Suppress("DEPRECATION")
    private fun setDataStruktural(dataUser: ModelUser){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanStruktural::class.java)
                    if (data != null){
                        when (data.statusPengajuan) {
                            "AdminFakultas" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus2.text = ""
                                    textStatus2.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status2.setImageResource(R.color.gray1)
                                }
                                else{
                                    status2.setImageResource(R.color.green5)
                                }
                            }
                            "Rektor" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus3.text = ""
                                    textStatus3.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status3.setImageResource(R.color.gray1)
                                }
                                else{
                                    status3.setImageResource(R.color.green5)
                                }
                            }
                            "BagianUmum" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                textStatus3.text = ""
                                textStatus3.background = resources.getDrawable(R.drawable.ic_true_white)
                                status3.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus4.text = ""
                                    textStatus4.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status4.setImageResource(R.color.gray1)
                                }
                                else{
                                    status4.setImageResource(R.color.green5)
                                }
                            }
                            "BagianKepegawaian" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                textStatus3.text = ""
                                textStatus3.background = resources.getDrawable(R.drawable.ic_true_white)
                                status3.setImageResource(R.color.blue4)

                                textStatus4.text = ""
                                textStatus4.background = resources.getDrawable(R.drawable.ic_true_white)
                                status4.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus5.text = ""
                                    textStatus5.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status5.setImageResource(R.color.gray1)
                                }
                                else{
                                    status5.setImageResource(R.color.green5)
                                }
                            }
                            "BKN" -> {
                                textStatus1.text = ""
                                textStatus1.background = resources.getDrawable(R.drawable.ic_true_white)
                                status1.setImageResource(R.color.blue4)

                                textStatus2.text = ""
                                textStatus2.background = resources.getDrawable(R.drawable.ic_true_white)
                                status2.setImageResource(R.color.blue4)

                                textStatus3.text = ""
                                textStatus3.background = resources.getDrawable(R.drawable.ic_true_white)
                                status3.setImageResource(R.color.blue4)

                                textStatus4.text = ""
                                textStatus4.background = resources.getDrawable(R.drawable.ic_true_white)
                                status4.setImageResource(R.color.blue4)

                                textStatus5.text = ""
                                textStatus5.background = resources.getDrawable(R.drawable.ic_true_white)
                                status5.setImageResource(R.color.blue4)

                                if (data.statusDitolak){
                                    textStatus6.text = ""
                                    textStatus6.background = resources.getDrawable(R.drawable.ic_close_red)
                                    status6.setImageResource(R.color.gray1)
                                }
                                else{
                                    if (data.disposisiBKN.isEmpty()){
                                        status6.setImageResource(R.color.green5)
                                    }
                                    else{
                                        textStatus6.text = ""
                                        textStatus6.background = resources.getDrawable(R.drawable.ic_true_white)
                                        status6.setImageResource(R.color.blue4)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .child("${dataUser.nip}__${dataUser.tglPengajuan}")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}