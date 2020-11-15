package com.exomatik.manajemenpangkat.ui.bkn.fragment.pelaksana

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
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.ui.bkn.MainBKNActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.exomatik.manajemenpangkat.utils.DetailPDFActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import kotlinx.android.synthetic.main.activity_detail_struktural_bagian_umum.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class DetailPelaksanaBKNActivity : AppCompatActivity(),
    RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any?>{
    private lateinit var savedData : DataSave
    private val getPDFCode = 86
    private var mStorageReference: StorageReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var dataPengajuan: ModelUsulanStruktural? = null
    private var rfabHelper: RapidFloatingActionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_struktural_bagian_umum)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        dataPengajuan = intent.getParcelableExtra("dataPengajuan")

        mStorageReference = FirebaseStorage.getInstance().getReference("UsulanStruktural")
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UsulanStruktural")
        setFAB()
        onClick()
    }

    @SuppressLint("SimpleDateFormat")
    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainBKNActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnOpenNota.setOnClickListener {
            dataPengajuan?.disposisiBKN?.let { it1 -> openPdf(it1) }
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

        btnSuratPelantikan.setOnClickListener {
            dataPengajuan?.SuratPelantikan?.let { it1 -> openPdf(it1) }
        }

        btnSuratTugas.setOnClickListener {
            dataPengajuan?.SuratTugas?.let { it1 -> openPdf(it1) }
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

    override fun onBackPressed() {
        val intent = Intent(this, MainBKNActivity::class.java)
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
        val sRef = mStorageReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}/disposisiBKN" + ".pdf")
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

    @SuppressLint("SimpleDateFormat")
    private fun getUrlFile(taskSnapshot: UploadTask.TaskSnapshot, nip: String, tglPengajuan: String){
        val onSuccessListener = OnSuccessListener<Uri?>{
            acceptDisposisi(nip, tglPengajuan, it.toString())
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

    private fun rejectData(note: String, context: Context){
        dataPengajuan?.statusDitolak = true
        dataPengajuan?.catatanDitolak = note

        mDatabaseReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}")
            ?.child("statusDitolak")?.setValue(true)
        mDatabaseReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}")
            ?.child("catatanDitolak")?.setValue(note)

        FirebaseDatabase.getInstance().getReference("DataBKN")
            .child("UsulanStruktural")
            .child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}")
            .setValue(dataPengajuan)

        Toast.makeText(context, "Pengajuan berhasil ditolak", Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainBKNActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("SimpleDateFormat")
    private fun acceptDisposisi(nip: String, tglPengajuan: String, urlFile: String){
        val tglBKN = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-M-yyyy"))
        } else {
            SimpleDateFormat("dd-M-yyyy").format(Date())
        }

        Toast.makeText(this, "Nota berhasil di upload", Toast.LENGTH_LONG).show()
        progress.visibility = View.GONE
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("disposisiBKN")?.setValue(urlFile)
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("statusPengajuan")?.setValue("BKN")
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("tglBKN")?.setValue(tglBKN)
        dataPengajuan?.disposisiBKN = urlFile
        dataPengajuan?.tglBKN = tglBKN
        FirebaseDatabase.getInstance().getReference("DataBKN")
            .child("UsulanStruktural")
            .child("${dataPengajuan?.nip}__${tglBKN}")
            .setValue(dataPengajuan)

        val intent = Intent(this, MainBKNActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    private fun setFAB(){
        val rfaContent = RapidFloatingActionContentLabelList(this)

        val items: MutableList<RFACLabelItem<*>> = ArrayList()
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Tolak")
                .setResId(R.drawable.ic_close_white)
                .setIconNormalColor(resources.getColor(R.color.colorPrimaryDark))
                .setIconPressedColor(resources.getColor(R.color.colorPrimary))
                .setLabelColor(resources.getColor(R.color.white))
                .setLabelBackgroundDrawable(resources.getDrawable(R.drawable.gradient_green))
                .setWrapper(2)
        )
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Disposisi")
                .setResId(R.drawable.ic_true_white)
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
            showDialogDecline(this)
        }
        else{
            getPDF()
        }
        rfabHelper?.toggleContent()
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any?>?) {
        if (position == 0){
            showDialogDecline(this)
        }
        else{
            getPDF()
        }
        rfabHelper?.toggleContent()
    }
}