package com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.pegawai.fragment.HomePegawaiFragment
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.activity_usulan_administrasi_1.*

class UsulanAdministrasi1Activity : AppCompatActivity() {
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_usulan_administrasi_1)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        savedData.getDataUser()?.let { setData(it) }
        onClick()
    }

    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, HomePegawaiFragment::class.java)
            startActivity(intent)
            finish()
        }

        btnNext.setOnClickListener {
            val intent = Intent(this, UsulanAdministrasi2Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomePegawaiFragment::class.java)
        startActivity(intent)
        finish()
    }

    private fun setData(dataUser: ModelUser){
//        etNip.editText?.setText(dataUser.newNip)
//        textNewNip.text = "NIP Baru \t \t \t \t \t \t : ${dataUser.newNip}"
//        textOldNip.text = "NIP Lama \t \t \t \t \t \t : ${dataUser.oldNip}"
//        textFrontGelar.text = "Gelar Depan \t \t \t \t : ${dataUser.frontGelar}"
//        textNama.text = "Nama \t \t \t \t \t \t \t : ${dataUser.nama}"
//        textBackGelar.text = "Gelar Belakang \t \t \t : ${dataUser.backGelar}"
//        textTempatLahir.text = "Tempat Lahir \t \t \t \t : ${dataUser.tempatLahir}"
//        textTglLahir.text = "Tanggal Lahir \t \t \t \t : ${dataUser.tanggalLahir}"
//        textJenisKelamin.text = "Jenis Kelamin \t \t \t \t : ${dataUser.jenisKelamin}"
//        textPendTertinggi.text = "Pendidikan Tertinggi \t : ${dataUser.pendTertinggi}"
//        textGol.text = "Gol / Pangkat \t \t \t \t : ${dataUser.gol}"
//        textTMTPangkat.text = "TMT Pangkat \t \t \t \t : ${dataUser.tmtPangkat}"
//        textNomorSKPangkat.text = "No. SK Pangkat \t \t \t : ${dataUser.nomorSKPangkat}"
//        textTglSK.text = "Tanggal SK Pangkat \t : ${dataUser.tglSKPangkat}"
//        textJabatan.text = "Jabatan \t \t \t \t \t \t : ${dataUser.jabatan}"
//        textTMTJabatan.text = "TMT Jabatan \t \t \t \t : ${dataUser.tmtJabatan}"
//        textNomorSKJabatan.text = "No. SK Jabatan \t \t \t : ${dataUser.nomorSKJabatan}"
//        textTglSKJabatan.text = "Tanggal SK Jabatan \t : ${dataUser.tglSKJabatan}"
    }
}