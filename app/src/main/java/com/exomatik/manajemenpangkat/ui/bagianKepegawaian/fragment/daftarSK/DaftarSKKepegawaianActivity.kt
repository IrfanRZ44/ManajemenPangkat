package com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.MainBagianKepegawaianActivity
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK.pelaksana.DaftarSKPelaksanaKepegawaianFragment
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK.struktural.DaftarSKStrukturalKepegawaianFragment
import com.exomatik.manajemenpangkat.utils.DataSave
import com.exomatik.manajemenpangkat.utils.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_daftar_sk_kepegawaian.*

class DaftarSKKepegawaianActivity : AppCompatActivity(){
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_daftar_sk_kepegawaian)

        savedData = DataSave(this)

        myCodeHere()
    }

    private fun myCodeHere(){
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(DaftarSKPelaksanaKepegawaianFragment(), "Usulan Pelaksana")
        adapter.addFragment(DaftarSKStrukturalKepegawaianFragment(), "Usulan Struktural")
        view_pagerr.adapter = adapter
        tabs.setupWithViewPager(view_pagerr)

        btnBack.setOnClickListener {
            val intent = Intent(this, MainBagianKepegawaianActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainBagianKepegawaianActivity::class.java)
        startActivity(intent)
        finish()
    }
}