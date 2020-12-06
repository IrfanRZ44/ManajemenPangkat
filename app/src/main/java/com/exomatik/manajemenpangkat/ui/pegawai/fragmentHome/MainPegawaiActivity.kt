package com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
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
import com.exomatik.manajemenpangkat.utils.BackgroundService
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_pegawai.*
import kotlinx.android.synthetic.main.activity_main_pegawai.view.*
import kotlinx.android.synthetic.main.nav_header_pegawai.view.*
import java.text.SimpleDateFormat
import java.util.*

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

        setAlarm()
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

    @Suppress("DEPRECATION")
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

    @SuppressLint("SimpleDateFormat")
    private fun setAlarm() {
        val myDate = "${savedData.getDataUser()?.tmtPangkat} 07:00:00"
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val date = sdf.parse(myDate)

        if (date != null){
            alarm1(date)
            alarm2(date)
            alarm3(date)
        }
    }

    //memunculkan notifikasi 4 tahun setelah TMT
    @SuppressLint("SimpleDateFormat")
    private fun alarm1(date: Date){
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, 4)
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val dueDate = sdf.format(cal.time)
        val dateTimeStamp = sdf.parse(dueDate)

        if (dateTimeStamp != null){
            createAlarmManager(dateTimeStamp.time)
        }
    }

    //memunculkan notifikasi 4 tahun, 3 bulan setelah TMT
    @SuppressLint("SimpleDateFormat")
    private fun alarm2(date: Date){
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, 4)
        cal.add(Calendar.MONTH, 3)
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val dueDate = sdf.format(cal.time)
        val dateTimeStamp = sdf.parse(dueDate)

        if (dateTimeStamp != null){
            createAlarmManager(dateTimeStamp.time)
        }
    }

    //memunculkan notifikasi 4 tahun, 6 bulan setelah TMT
    @SuppressLint("SimpleDateFormat")
    private fun alarm3(date: Date){
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.YEAR, 4)
        cal.add(Calendar.MONTH, 6)
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        val dueDate = sdf.format(cal.time)
        val dateTimeStamp = sdf.parse(dueDate)

        if (dateTimeStamp != null){
            createAlarmManager(dateTimeStamp.time)
        }
    }

    private fun createAlarmManager(randomTime: Long){
        createChannel()

        val backgroundService = Intent(this, BackgroundService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, backgroundService, 0)
        val taskManager = getSystemService(ALARM_SERVICE) as AlarmManager?

        taskManager?.set(AlarmManager.RTC_WAKEUP, randomTime, pendingIntent)
    }

    private fun createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val description = "Channel for Manajemen Pangkat"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("notify Manajemen Pangkat", description, importance)
            channel.description = description

            channel.lightColor = Color.GREEN
            channel.enableLights(true)
            val uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            channel.setSound(uriSound, Notification.AUDIO_ATTRIBUTES_DEFAULT)
            channel.enableVibration(true)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}