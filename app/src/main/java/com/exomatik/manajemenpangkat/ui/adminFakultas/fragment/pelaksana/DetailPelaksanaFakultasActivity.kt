package com.exomatik.manajemenpangkat.ui.adminFakultas.fragment.pelaksana

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.exomatik.manajemenpangkat.ui.adminFakultas.fragment.MainFakultasActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.exomatik.manajemenpangkat.utils.DetailPDFActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_detail_pelaksana_fakultas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DetailPelaksanaFakultasActivity : AppCompatActivity() {
    private lateinit var savedData : DataSave
    private val getPDFCode = 86
    private var mStorageReference: StorageReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var dataPengajuan: ModelUsulanPelaksana? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_pelaksana_fakultas)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        dataPengajuan = intent.getParcelableExtra("dataPengajuan")

        mStorageReference = FirebaseStorage.getInstance().getReference("UsulanPelaksana")
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UsulanPelaksana")
        onClick()
        onClickDecline()

        val data = intent.getParcelableExtra<ModelUsulanPelaksana>("dataPengajuan")
        if (data != null){
            setData(data)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    private fun setData(data: ModelUsulanPelaksana){
        if (data.Karpeg.isEmpty()){
            btnKarpeg.isEnabled = false
            btnKarpegDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.SKCpns.isEmpty()){
            btnSKCPNS.isEnabled = false
            btnSKCPNSDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.SKPns.isEmpty()){
            btnSKPNS.isEnabled = false
            btnSKPNSDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.SKPangkatTerakhir.isEmpty()){
            btnSKPangkatTerakhir.isEnabled = false
            btnSKPangkatTerakhirDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.SKJabatanTerakhir.isEmpty()){
            btnSKJabatanTerakhir.isEnabled = false
            btnSKJabatanTerakhirDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.PAKTerakhir.isEmpty()){
            btnPAKTerakhir.isEnabled = false
            btnPAKTerakhirDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.FungsionalTerakhir.isEmpty()){
            btnFungsionalTerakhir.isEnabled = false
            btnFungsionalTerakhirDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.SKP.isEmpty()){
            btnSKP.isEnabled = false
            btnSKPDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.Ijazah.isEmpty()){
            btnIjazah.isEnabled = false
            btnIjazahDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }

        if (data.Transkrip.isEmpty()){
            btnTranskrip.isEnabled = false
            btnTranskripDecline.isEnabled = false

            btnSend.text = "Tolak Berkas"
            btnSend.setTextColor(resources.getColor(R.color.red1))
            val img = resources.getDrawable(R.drawable.ic_close_red)
            btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainFakultasActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSend.setOnClickListener {
            if (btnSend.text == "Kirim Nota"){
                getPDF()
            }
            else{
                showDialogDecline(this)
            }
        }

        btnKarpeg.setOnClickListener {
            dataPengajuan?.Karpeg?.let { it1 -> openPdf(it1) }
        }

        btnSKCPNS.setOnClickListener {
            dataPengajuan?.SKCpns?.let { it1 -> openPdf(it1) }
        }

        btnSKPNS.setOnClickListener {
            dataPengajuan?.SKPns?.let { it1 -> openPdf(it1) }
        }

        btnSKPangkatTerakhir.setOnClickListener {
            dataPengajuan?.SKPangkatTerakhir?.let { it1 -> openPdf(it1) }
        }

        btnSKJabatanTerakhir.setOnClickListener {
            dataPengajuan?.SKJabatanTerakhir?.let { it1 -> openPdf(it1) }
        }

        btnPAKTerakhir.setOnClickListener {
            dataPengajuan?.PAKTerakhir?.let { it1 -> openPdf(it1) }
        }

        btnFungsionalTerakhir.setOnClickListener {
            dataPengajuan?.FungsionalTerakhir?.let { it1 -> openPdf(it1) }
        }

        btnSKP.setOnClickListener {
            dataPengajuan?.SKP?.let { it1 -> openPdf(it1) }
        }

        btnIjazah.setOnClickListener {
            dataPengajuan?.Ijazah?.let { it1 -> openPdf(it1) }
        }

        btnTranskrip.setOnClickListener {
            dataPengajuan?.Transkrip?.let { it1 -> openPdf(it1) }
        }
    }

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    private fun onClickDecline(){
        btnKarpegDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.Karpeg = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("Karpeg")?.setValue("")

                btnKarpegDecline.isEnabled = false
                btnKarpeg.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnSKCPNSDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.SKCpns = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("SKCpns")?.setValue("")

                btnSKCPNSDecline.isEnabled = false
                btnSKCPNS.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnSKPNSDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.SKPns = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("SKPns")?.setValue("")

                btnSKPNSDecline.isEnabled = false
                btnSKPNS.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnSKPangkatTerakhirDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.SKPangkatTerakhir = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("SKPangkatTerakhir")?.setValue("")

                btnSKPangkatTerakhirDecline.isEnabled = false
                btnSKPangkatTerakhir.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnSKJabatanTerakhirDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.SKJabatanTerakhir = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("SKJabatanTerakhir")?.setValue("")

                btnSKJabatanTerakhirDecline.isEnabled = false
                btnSKJabatanTerakhir.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnPAKTerakhirDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.PAKTerakhir = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("PAKTerakhir")?.setValue("")

                btnPAKTerakhirDecline.isEnabled = false
                btnPAKTerakhir.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnFungsionalTerakhirDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.FungsionalTerakhir = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("FungsionalTerakhir")?.setValue("")

                btnFungsionalTerakhirDecline.isEnabled = false
                btnFungsionalTerakhir.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnSKPDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.SKP = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("SKP")?.setValue("")

                btnSKPDecline.isEnabled = false
                btnSKP.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnIjazahDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.Ijazah = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("Ijazah")?.setValue("")

                btnIjazahDecline.isEnabled = false
                btnIjazah.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }

        btnTranskripDecline.setOnClickListener {
            val nip = dataPengajuan?.nip

            if (nip != null){
                dataPengajuan?.Transkrip = ""
                mDatabaseReference?.child("${nip}__${dataPengajuan?.tglPengajuan}")?.child("Transkrip")?.setValue("")

                btnTranskripDecline.isEnabled = false
                btnTranskrip.isEnabled = false

                btnSend.text = "Tolak Berkas"
                btnSend.setTextColor(resources.getColor(R.color.red1))
                val img = resources.getDrawable(R.drawable.ic_close_red)
                btnSend.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainFakultasActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openPdf(urlFile: String) {
        val intent = Intent(this, DetailPDFActivity::class.java)
        intent.putExtra("urlFile", urlFile)
        startActivity(intent)
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
        val sRef = mStorageReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}/disposisiAdminFakultas" + ".pdf")
        sRef?.putFile(data)
            ?.addOnSuccessListener { taskSnapshot ->
                dataPengajuan?.let { getUrlFile(taskSnapshot, it.nip, it.tglPengajuan) }
            }
            ?.addOnFailureListener { exception ->
                progress.visibility = View.GONE
                Toast.makeText(this, exception.message, Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun getUrlFile(taskSnapshot: UploadTask.TaskSnapshot, nip: String, tglPengajuan: String){
        val onSuccessListener = OnSuccessListener<Uri?>{
            acceptData(nip, tglPengajuan, it.toString())
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

    private fun showDialogDecline(context: Context){
        val alert = AlertDialog.Builder(context)
        alert.setMessage("Alasan penolakan :")

        val editText = EditText(context)
        val linearLayout = LinearLayout(context)
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayout.setPadding(20, 0, 20, 0)
        linearLayout.layoutParams = layoutParams
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(editText)
        alert.setView(linearLayout)

        alert.setPositiveButton(
            "Tolak"
        ) { dialog, _ ->
            val note = editText.text.toString()
            dialog.dismiss()
            if (note.isEmpty()){
                Toast.makeText(context, "Error, mohon masukkan alasan penolakan", Toast.LENGTH_LONG).show()
            }
            else{
                rejectData(note, context)
            }
        }
        alert.setNegativeButton(
            "Batal"
        ) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun acceptData(nip: String, tglPengajuan: String, urlFile: String){
        val tglAdminFakultas = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy"))
        } else {
            SimpleDateFormat("dd-M-yyyy").format(Date())
        }

        Toast.makeText(this, "Nota berhasil di upload", Toast.LENGTH_LONG).show()
        progress.visibility = View.GONE
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("disposisiAdminFakultas")?.setValue(urlFile)
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("statusPengajuan")?.setValue("Rektor")
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("tglAdminFakultas")?.setValue(tglAdminFakultas)
        dataPengajuan?.disposisiAdminFakultas = urlFile
        dataPengajuan?.tglAdminFakultas = tglAdminFakultas
        FirebaseDatabase.getInstance().getReference("DataAdminFakultas")
            .child("UsulanPelaksana")
            .child("${dataPengajuan?.nip}__${tglAdminFakultas}")
            .setValue(dataPengajuan)

        val intent = Intent(this, MainFakultasActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun rejectData(note: String, context: Context){
        dataPengajuan?.statusDitolak = true
        dataPengajuan?.catatanDitolak = note

        mDatabaseReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}")
            ?.child("statusDitolak")?.setValue(true)
        mDatabaseReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}")
            ?.child("catatanDitolak")?.setValue(note)

        FirebaseDatabase.getInstance().getReference("DataAdminFakultas")
            .child("UsulanPelaksana")
            .child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}")
            .setValue(dataPengajuan)

        Toast.makeText(context, "Pengajuan berhasil ditolak", Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainFakultasActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendNotificationRektor() {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)
                        if (data != null && data.token.isNotEmpty()){
                            val notification = Notification("Admin Fakultas/Univ mengirimkan nota usulan",
                                "Pengajuan Usulan Pelaksana"
                                , "com.exomatik.manajemenpangkat.fcm_TARGET_MAIN_REKTOR")

                            val sender = Sender(notification, data.token)
                            val mService: APIService = Common.fCMClient
                            mService.sendNotification(sender)?.enqueue(object :
                                Callback<MyResponse?> {
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
            .equalTo("Rektor")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun sendNotificationPegawai() {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUser::class.java)
                        if (data != null && data.token.isNotEmpty()){
                            val notification = Notification("Admin Fakultas/Univ menolak berkas",
                                "Pengajuan Usulan Pelaksana"
                                , "com.exomatik.manajemenpangkat.fcm_TARGET_MAIN_PEGAWAI")

                            val sender = Sender(notification, data.token)
                            val mService: APIService = Common.fCMClient
                            mService.sendNotification(sender)?.enqueue(object :
                                Callback<MyResponse?> {
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
            .equalTo("Rektor")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}