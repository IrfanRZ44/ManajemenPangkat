package com.exomatik.manajemenpangkat.ui.pegawai.daftarUsulan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.services.notification.APIService
import com.exomatik.manajemenpangkat.services.notification.Common
import com.exomatik.manajemenpangkat.services.notification.model.MyResponse
import com.exomatik.manajemenpangkat.services.notification.model.Notification
import com.exomatik.manajemenpangkat.services.notification.model.Sender
import com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_usulan_pelaksana.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UsulanPelaksanaActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private val getPDFCode = 86
    private var mStorageReference: StorageReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private lateinit var requestFile: String

    @SuppressLint("SimpleDateFormat")
    private val tglPengajuan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy"))
    } else {
        SimpleDateFormat("dd-M-yyyy").format(Date())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_usulan_pelaksana)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        savedData.getDataUser()?.let { setData(it) }
        mStorageReference = FirebaseStorage.getInstance().getReference("UsulanPelaksana")
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UsulanPelaksana")

        onClick()
    }

    @SuppressLint("SimpleDateFormat")
    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainPegawaiActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSend.setOnClickListener {
            if (btnKarpeg.text == "Change" && btnSKCPNS.text == "Change" && btnSKPNS.text == "Change" &&
                    btnSKPangkatTerakhir.text == "Change" && btnSKJabatanTerakhir.text == "Change" &&
                    btnPAKTerakhir.text == "Change" && btnFungsionalTerakhir.text == "Change" &&
                    btnSKP.text == "Change" && btnIjazah.text == "Change" && btnTranskrip.text == "Change"
                    ){
                val dataUser = savedData.getDataUser()
                if (dataUser != null){
                    val awal = intent.getStringExtra("awal")
                    val usul = intent.getStringExtra("usul")

                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("statusPengajuan")?.setValue("AdminFakultas")
                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("tglPengajuan")?.setValue(tglPengajuan)
                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("statusDitolak")?.setValue(false)
                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("pangkatAwal")?.setValue(awal)
                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("pangkatUsulan")?.setValue(usul)
                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("nip")?.setValue(dataUser.nip)
                    mDatabaseReference?.child("${dataUser.nip}__$tglPengajuan")?.child("catatanDitolak")?.setValue("")
                    FirebaseDatabase.getInstance().getReference("Users").child(dataUser.nip).child("tglPengajuan").setValue(tglPengajuan)

                    val tempSavedData = savedData.getDataUser()
                    tempSavedData?.tglPengajuan = tglPengajuan
                    savedData.setDataObject(tempSavedData, "Users")

                    val intent = Intent(this, PengajuanSelesaiActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else{
                Toast.makeText(this, "Mohon lengkapi berkas Anda", Toast.LENGTH_LONG).show()
            }
        }

        btnKarpeg.setOnClickListener {
            requestFile = "Karpeg"
            getPDF()
        }

        btnSKCPNS.setOnClickListener {
            requestFile = "SKCpns"
            getPDF()
        }

        btnSKPNS.setOnClickListener {
            requestFile = "SKPns"
            getPDF()
        }

        btnSKPangkatTerakhir.setOnClickListener {
            requestFile = "SKPangkatTerakhir"
            getPDF()
        }

        btnSKJabatanTerakhir.setOnClickListener {
            requestFile = "SKJabatanTerakhir"
            getPDF()
        }

        btnPAKTerakhir.setOnClickListener {
            requestFile = "PAKTerakhir"
            getPDF()
        }

        btnFungsionalTerakhir.setOnClickListener {
            requestFile = "FungsionalTerakhir"
            getPDF()
        }

        btnSKP.setOnClickListener {
            requestFile = "SKP"
            getPDF()
        }

        btnIjazah.setOnClickListener {
            requestFile = "Ijazah"
            getPDF()
        }

        btnTranskrip.setOnClickListener {
            requestFile = "Transkrip"
            getPDF()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainPegawaiActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setData(dataUser: ModelUser){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
            }

            override fun onDataChange(result: DataSnapshot) {
                progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanPelaksana::class.java)
                    if (data != null){
                        if (data.Karpeg.isNotEmpty()){
                            requestFile = "Karpeg"
                            changeButtonStyle()
                        }
                        if (data.SKCpns.isNotEmpty()){
                            requestFile = "SKCpns"
                            changeButtonStyle()
                        }
                        if (data.SKPns.isNotEmpty()){
                            requestFile = "SKPns"
                            changeButtonStyle()
                        }
                        if (data.SKPangkatTerakhir.isNotEmpty()){
                            requestFile = "SKPangkatTerakhir"
                            changeButtonStyle()
                        }
                        if (data.SKJabatanTerakhir.isNotEmpty()){
                            requestFile = "SKJabatanTerakhir"
                            changeButtonStyle()
                        }
                        if (data.PAKTerakhir.isNotEmpty()){
                            requestFile = "PAKTerakhir"
                            changeButtonStyle()
                        }
                        if (data.FungsionalTerakhir.isNotEmpty()){
                            requestFile = "FungsionalTerakhir"
                            changeButtonStyle()
                        }
                        if (data.SKP.isNotEmpty()){
                            requestFile = "SKP"
                            changeButtonStyle()
                        }
                        if (data.Ijazah.isNotEmpty()){
                            requestFile = "Ijazah"
                            changeButtonStyle()
                        }
                        if (data.Transkrip.isNotEmpty()){
                            requestFile = "Transkrip"
                            changeButtonStyle()
                        }
                    }
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanPelaksana")
            .child("${dataUser.nip}__$tglPengajuan")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getPDF() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
            PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, getPDFCode)
        } else { ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 9)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == getPDFCode && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val resultData = data.data
            if (resultData != null) {
                uploadFile(resultData)
            } else {
                Toast.makeText(this, "Tidak ada file yang dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadFile(data: Uri) {
        progress.visibility = View.VISIBLE
        val sRef = mStorageReference?.child("${savedData.getDataUser()?.nip}__$tglPengajuan/$requestFile" + ".pdf")
        sRef?.putFile(data)
            ?.addOnSuccessListener { taskSnapshot ->
                savedData.getDataUser()?.nip?.let { getUrlFile(taskSnapshot, it) }
            }
            ?.addOnFailureListener { exception ->
                progress.visibility = View.GONE
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun getUrlFile(taskSnapshot: UploadTask.TaskSnapshot, nip: String){
        val onSuccessListener = OnSuccessListener<Uri?>{
            Toast.makeText(this, "File berhasil di upload", Toast.LENGTH_LONG).show()
            changeButtonStyle()
            progress.visibility = View.GONE
            mDatabaseReference?.child("${nip}__$tglPengajuan")?.child(requestFile)?.setValue(it.toString())
            sendNotification()
        }

        val onFailureListener = OnFailureListener {
            Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG)
                .show()
            progress.visibility = View.GONE
        }

        taskSnapshot.storage.downloadUrl
            .addOnSuccessListener(onSuccessListener)
            .addOnFailureListener(onFailureListener)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun changeButtonStyle(){
        when (requestFile) {
            "Karpeg" -> {
                btnKarpeg.text = "Change"
                btnKarpeg.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnKarpeg.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "SKCpns" -> {
                btnSKCPNS.text = "Change"
                btnSKCPNS.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnSKCPNS.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "SKPns" -> {
                btnSKPNS.text = "Change"
                btnSKPNS.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnSKPNS.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "SKPangkatTerakhir" -> {
                btnSKPangkatTerakhir.text = "Change"
                btnSKPangkatTerakhir.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnSKPangkatTerakhir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "SKJabatanTerakhir" -> {
                btnSKJabatanTerakhir.text = "Change"
                btnSKJabatanTerakhir.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnSKJabatanTerakhir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "PAKTerakhir" -> {
                btnPAKTerakhir.text = "Change"
                btnPAKTerakhir.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnPAKTerakhir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "FungsionalTerakhir" -> {
                btnFungsionalTerakhir.text = "Change"
                btnFungsionalTerakhir.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnFungsionalTerakhir.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "SKP" -> {
                btnSKP.text = "Change"
                btnSKP.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnSKP.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "Ijazah" -> {
                btnIjazah.text = "Change"
                btnIjazah.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnIjazah.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            "Transkrip" -> {
                btnTranskrip.text = "Change"
                btnTranskrip.setTextColor(resources.getColor(R.color.orange1))
                val img = resources.getDrawable(R.drawable.ic_change_orange)
                btnTranskrip.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }
    }

    private fun sendNotification() {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)
                        if (data != null && data.token.isNotEmpty()){
                            val notification = Notification("${savedData.getDataUser()?.nama} mengirimkan pengajuan usulan",
                                "Pengajuan Usulan Pelaksana"
                                , "com.exomatik.manajemenpangkat.fcm_TARGET_MAIN_ADMIN_FAKULTAS")

                            val sender = Sender(notification, data.token)
                            val mService: APIService = Common.fCMClient
                            mService.sendNotification(sender)?.enqueue(object : Callback<MyResponse?> {
                                override fun onResponse(
                                    call: Call<MyResponse?>?,
                                    response: Response<MyResponse?>?) {}
                                override fun onFailure(call: Call<MyResponse?>?, t: Throwable) {}
                            })
                        }
                    }
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .orderByChild("jenisUser")
            .equalTo("AdminFakultas")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}