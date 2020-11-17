package com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK.struktural

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.MainBagianKepegawaianActivity
import com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK.DaftarSKKepegawaianActivity
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
import kotlinx.android.synthetic.main.activity_detail_daftar_struktural_kepegawaian.*
import java.util.*

class DetailSKStrukturalKepegawaianActivity : AppCompatActivity(),
    RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any?> {
    private lateinit var savedData : DataSave
    private val getPDFCode = 86
    private var mStorageReference: StorageReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var dataPengajuan: ModelUsulanStruktural? = null
    private var rfabHelper: RapidFloatingActionHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail_daftar_struktural_kepegawaian)
        myCodeHere()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)
        dataPengajuan = intent.getParcelableExtra("dataPengajuan")

        mStorageReference = FirebaseStorage.getInstance().getReference("UsulanStruktural")
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("UsulanStruktural")
        onClick()
        setFAB()
    }

    @SuppressLint("SimpleDateFormat")
    private fun onClick() {
        btnBack.setOnClickListener {
            val intent = Intent(this, MainBagianKepegawaianActivity::class.java)
            startActivity(intent)
            finish()
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
        val intent = Intent(this, MainBagianKepegawaianActivity::class.java)
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
        val sRef = mStorageReference?.child("${dataPengajuan?.nip}__${dataPengajuan?.tglPengajuan}/disposisiSKBaru" + ".pdf")
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

    @SuppressLint("SimpleDateFormat")
    private fun acceptDisposisi(nip: String, tglPengajuan: String, urlFile: String){
        Toast.makeText(this, "disposisi berhasil di upload", Toast.LENGTH_LONG).show()
        progress.visibility = View.GONE
        mDatabaseReference?.child("${nip}__$tglPengajuan")?.child("disposisiSKBaru")?.setValue(urlFile)

        FirebaseDatabase.getInstance().getReference("DataBagianKepegawaian")
            .child("UsulanStruktural")
            .child("${dataPengajuan?.nip}__${dataPengajuan?.tglBagianKepegawaian}")
            .child("disposisiSKBaru")
            .setValue(urlFile)

        val intent = Intent(this, DaftarSKKepegawaianActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Suppress("DEPRECATION")
    private fun setFAB(){
        val rfaContent = RapidFloatingActionContentLabelList(this)

        val items: MutableList<RFACLabelItem<*>> = ArrayList()
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Persetujuan BKN")
                .setResId(R.drawable.ic_open_white)
                .setIconNormalColor(resources.getColor(R.color.colorPrimaryDark))
                .setIconPressedColor(resources.getColor(R.color.colorPrimary))
                .setLabelColor(resources.getColor(R.color.white))
                .setLabelBackgroundDrawable(resources.getDrawable(R.drawable.gradient_green))
                .setWrapper(2)
        )
        items.add(
            RFACLabelItem<Int>()
                .setLabel("Upload SK")
                .setResId(R.drawable.ic_add_white)
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
            dataPengajuan?.disposisiBKN?.let { openPdf(it) }
        }
        else{
            getPDF()
        }
        rfabHelper?.toggleContent()
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any?>?) {
        if (position == 0){
            dataPengajuan?.disposisiBKN?.let { openPdf(it) }
        }
        else{
            getPDF()
        }
        rfabHelper?.toggleContent()
    }
}