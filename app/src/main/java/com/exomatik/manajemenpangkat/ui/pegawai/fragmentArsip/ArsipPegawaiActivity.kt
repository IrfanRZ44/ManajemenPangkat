package com.exomatik.manajemenpangkat.ui.pegawai.fragmentArsip

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.exomatik.manajemenpangkat.utils.DetailPDFActivity
import com.exomatik.manajemenpangkat.utils.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_arsip_pegawai.*

class ArsipPegawaiActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_arsip_pegawai)

        savedData = DataSave(this)

        myCodeHere()
    }

    private fun myCodeHere(){
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(ArsipBerkasFragment { urlFile: String -> openPdf(urlFile) }, "Berkas")
        adapter.addFragment(ArsipNotaFragment { urlFile: String -> openPdf(urlFile) }, "Nota")
        view_pagerr.adapter = adapter
        tabs.setupWithViewPager(view_pagerr)
    }

    private fun openPdf(urlFile: String){
        val intent = Intent(this, DetailPDFActivity::class.java)
        intent.putExtra("urlFile", urlFile)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainPegawaiActivity::class.java)
        startActivity(intent)
        finish()
    }
}