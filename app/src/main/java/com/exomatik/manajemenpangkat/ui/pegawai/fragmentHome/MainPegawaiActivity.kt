package com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import coil.api.load
import coil.transform.BlurTransformation
import coil.transform.CircleCropTransformation
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.auth.SplashActivity
import com.exomatik.manajemenpangkat.ui.pegawai.NotifikasiPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_pegawai.*
import kotlinx.android.synthetic.main.activity_main_pegawai.view.*
import kotlinx.android.synthetic.main.nav_header_pegawai.view.*

class MainPegawaiActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var savedData : DataSave
    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main_pegawai)

        savedData = DataSave(this)

        setNavigation()
        setHeader()
        onClick()
    }

    private fun setNavigation(){
        toolbar.title = ""
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container
            , HomePegawaiFragment()
        ).commit()
    }

    @SuppressLint("SetTextI18n")
    private fun setHeader(){
        val localView = nav_view.getHeaderView(0)
        val context = this
        localView.imgBackgrond.load(R.drawable.ic_logo_uin){
            transformations(BlurTransformation(context))
        }

        localView.imagePerson.load(R.drawable.ic_logo_uin){
            transformations(CircleCropTransformation())
        }

        localView.textNama.text = savedData.getDataUser()?.nama
        localView.textEmail.text = "${savedData.getDataUser()?.nip}@uin-alauddin.ac.id"
    }

    private fun onClick() {
        toolbar.btnNotif.setOnClickListener {
            val intent = Intent(this, NotifikasiPegawaiActivity::class.java)
            startActivity(intent)
            finish()
        }
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

    @SuppressLint("SetTextI18n")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_beranda -> {
                textHeader.text = "Halaman Utama"
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container
                    ,
                    HomePegawaiFragment()
                ).commit()
            }
            R.id.nav_profile -> {
                textHeader.text = "Edit Profil"
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container
                    ,
                    EditProfilFragment()
                ).commit()
            }
            R.id.nav_logout -> {
                savedData.setDataObject(ModelUser(), "Users")
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        return true
    }
}