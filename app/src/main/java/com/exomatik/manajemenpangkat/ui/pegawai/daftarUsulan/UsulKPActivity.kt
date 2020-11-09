package com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.pegawai.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.exomatik.manajemenpangkat.utils.SpinnerAdapter
import kotlinx.android.synthetic.main.activity_usul_kp.*

class UsulKPActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private val listJenisKenaikan = ArrayList<String>()
    private val listAwal = ArrayList<String>()
    private val listUsul = ArrayList<String>()
    private lateinit var adapterJenisKenaikan : SpinnerAdapter
    private lateinit var adapterAwal : SpinnerAdapter
    private lateinit var adapterUsul : SpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_usul_kp)
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

        btnNext.setOnClickListener {
            if (spinnerAwal.selectedItemPosition > 0 && spinnerUsul.selectedItemPosition > 0){
                when (spinnerJenisKenaikan.selectedItemPosition) {
                    1 -> {
                        val intent = Intent(this, UsulanPelaksanaActivity::class.java)
                        intent.putExtra("awal", listAwal[spinnerAwal.selectedItemPosition])
                        intent.putExtra("usul", listUsul[spinnerUsul.selectedItemPosition])
                        startActivity(intent)
                        finish()
                    }
                    2 -> {
                        val intent = Intent(this, UsulanStrukturalActivity::class.java)
                        intent.putExtra("awal", listAwal[spinnerAwal.selectedItemPosition])
                        intent.putExtra("usul", listUsul[spinnerUsul.selectedItemPosition])
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, "Mohon pilih salah satu jenis kenaikan pangkat", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else {
                Toast.makeText(this, "Mohon pilih salah satu awal dan usulan pangkat", Toast.LENGTH_LONG).show()
            }
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
        etPangkat.editText?.setText(dataUser.pangkat)
        setSpinnerAdapterJenisKenaikan()
        setSpinnerAdapterAwal()
        setSpinnerAdapterUsul()
    }

    private fun setSpinnerAdapterJenisKenaikan() {
        listJenisKenaikan.add("Jenis")
        listJenisKenaikan.add("Jabatan Pelaksana")
        listJenisKenaikan.add("Jabatan Struktural")

        adapterJenisKenaikan = SpinnerAdapter(this, listJenisKenaikan)
        spinnerJenisKenaikan.adapter = adapterJenisKenaikan
    }

    private fun setSpinnerAdapterAwal() {
        listAwal.add("Awal")
        listAwal.add("Juru Muda Gol. I A")
        listAwal.add("Juru Muda Tk. I Gol. I B")
        listAwal.add("Juru Gol. I C")
        listAwal.add("Juru Tk. I Gol. I D")
        listAwal.add("Pengatur Muda Gol. II A")
        listAwal.add("Pengatur Muda Tk. I Pengatur Gol. II B")
        listAwal.add("Pengatur Gol. II C")
        listAwal.add("Pengatur Tk. I Gol. II D")
        listAwal.add("Penata Muda Gol. III A")
        listAwal.add("Penata Muda Tk. I Penata Gol. II")
        listAwal.add("Penata Gol. III C")
        listAwal.add("Penata Tk. I Gol. III D")
        listAwal.add("Pembina Gol. IV A")
        listAwal.add("Pembina Tk. I Gol. IV B")
        listAwal.add("Pembina Utama Muda Gol. IV C")
        listAwal.add("Pembina Utama Madya Gol. IV D")
        listAwal.add("Pembina Utama Gol. IV E")

        adapterAwal = SpinnerAdapter(this, listAwal)
        spinnerAwal.adapter = adapterAwal
    }

    private fun setSpinnerAdapterUsul() {
        listUsul.add("Usul")
        listUsul.add("Juru Muda Gol. I A")
        listUsul.add("Juru Muda Tk. I Gol. I B")
        listUsul.add("Juru Gol. I C")
        listUsul.add("Juru Tk. I Gol. I D")
        listUsul.add("Pengatur Muda Gol. II A")
        listUsul.add("Pengatur Muda Tk. I Pengatur Gol. II B")
        listUsul.add("Pengatur Gol. II C")
        listUsul.add("Pengatur Tk. I Gol. II D")
        listUsul.add("Penata Muda Gol. III A")
        listUsul.add("Penata Muda Tk. I Penata Gol. II")
        listUsul.add("Penata Gol. III C")
        listUsul.add("Penata Tk. I Gol. III D")
        listUsul.add("Pembina Gol. IV A")
        listUsul.add("Pembina Tk. I Gol. IV B")
        listUsul.add("Pembina Utama Muda Gol. IV C")
        listUsul.add("Pembina Utama Madya Gol. IV D")
        listUsul.add("Pembina Utama Gol. IV E")

        adapterUsul = SpinnerAdapter(this, listUsul)
        spinnerUsul.adapter = adapterUsul
    }
}