package com.exomatik.manajemenpangkat.ui.bagianKepegawaian

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.auth.SplashActivity
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.pelaksana.UsulPelaksanaBagianKepegawaianFragment
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.struktural.UsulStrukturalBagianKepegawaianFragment
import com.exomatik.manajemenpangkat.utils.DataSave
import com.exomatik.manajemenpangkat.utils.SectionsPagerAdapter
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import kotlinx.android.synthetic.main.activity_main_rektor.*

class MainBagianKepegawaianActivity : AppCompatActivity(),
    RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any?> {
    private lateinit var savedData : DataSave
    private var exit = false
    private var rfabHelper: RapidFloatingActionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_bagian_umum)

        savedData = DataSave(this)

        myCodeHere()
        setFAB()
    }

    private fun myCodeHere(){
        val adapter = SectionsPagerAdapter(supportFragmentManager)
        adapter.addFragment(UsulPelaksanaBagianKepegawaianFragment(), "Usulan Pelaksana")
        adapter.addFragment(UsulStrukturalBagianKepegawaianFragment(), "Usulan Struktural")
        view_pagerr.adapter = adapter
        tabs.setupWithViewPager(view_pagerr)
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

    @Suppress("DEPRECATION")
    private fun setFAB(){
        val rfaContent = RapidFloatingActionContentLabelList(this)

        val items: MutableList<RFACLabelItem<*>> = ArrayList()
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Riwayat")
                .setResId(R.drawable.ic_riwayat_white)
                .setIconNormalColor(resources.getColor(R.color.colorPrimaryDark))
                .setIconPressedColor(resources.getColor(R.color.colorPrimary))
                .setLabelColor(resources.getColor(R.color.white))
                .setLabelBackgroundDrawable(resources.getDrawable(R.drawable.gradient_green))
                .setWrapper(2)
        )
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Logout")
                .setResId(R.drawable.ic_logout_white)
                .setIconNormalColor(resources.getColor(R.color.colorPrimaryDark))
                .setIconPressedColor(resources.getColor(R.color.colorPrimary))
                .setLabelColor(resources.getColor(R.color.white))
                .setLabelBackgroundDrawable(resources.getDrawable(R.drawable.gradient_green))
                .setWrapper(0)
        )

        rfaContent.items = items

        rfabHelper = RapidFloatingActionHelper(
            this,
            fabLayout,
            fabButton,
            rfaContent
        ).build()

        rfaContent.setOnRapidFloatingActionContentLabelListListener(this)
    }

    override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any?>?) {
        if (position == 0){
            val intent = Intent(this, RiwayatBagianKepegawaianActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            savedData.setDataObject(ModelUser(), "Users")
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
        rfabHelper?.toggleContent()
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any?>?) {
        if (position == 0){
            val intent = Intent(this, RiwayatBagianKepegawaianActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            savedData.setDataObject(ModelUser(), "Users")
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
        rfabHelper?.toggleContent()
    }
}