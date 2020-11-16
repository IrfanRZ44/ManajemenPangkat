package com.exomatik.manajemenpangkat.ui.pegawai.fragmentArsip

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_arsip_berkas.view.*

class ArsipBerkasFragment( private val openPdf: (String) -> Unit) : Fragment() {
    private lateinit var savedData : DataSave
    private lateinit var v : View

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_arsip_berkas, paramViewGroup, false)

        savedData = DataSave(context)
        getDataPelaksana()

        return v
    }

    @SuppressLint("SetTextI18n")
    private fun onClickPelaksana(dataPengajuan: ModelUsulanPelaksana){
        v.text6.text = "6. PAK Terakhir"
        v.text7.text = "7. SK Fungsional Terakhir"

        if (dataPengajuan.Karpeg.isNotEmpty()){
            v.btnKarpeg.setOnClickListener {
                openPdf(dataPengajuan.Karpeg)
            }
        }
        else{
            v.btnKarpeg.isEnabled = false
        }

        v.btnSKCPNS.setOnClickListener {
            openPdf(dataPengajuan.SKCpns)
        }

        v.btnSKPNS.setOnClickListener {
            openPdf(dataPengajuan.SKPns)
        }

        v.btnSKPangkatTerakhir.setOnClickListener {
            openPdf(dataPengajuan.SKPangkatTerakhir)
        }

        v.btnSKJabatanTerakhir.setOnClickListener {
            openPdf(dataPengajuan.SKJabatanTerakhir)
        }

        v.btn6.setOnClickListener {
            openPdf(dataPengajuan.PAKTerakhir)
        }

        v.btn7.setOnClickListener {
            openPdf(dataPengajuan.FungsionalTerakhir)
        }

        v.btnSKP.setOnClickListener {
            openPdf(dataPengajuan.SKP)
        }

        v.btnIjazah.setOnClickListener {
            openPdf(dataPengajuan.Ijazah)
        }

        v.btnTranskrip.setOnClickListener {
            openPdf(dataPengajuan.Transkrip)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickStruktural(dataPengajuan: ModelUsulanStruktural) {
        v.text6.text = "6. Surat Pelantikan"
        v.text7.text = "7. Surat Melaksanakan Tugas"

        v.btnKarpeg.setOnClickListener {
            openPdf(dataPengajuan.Karpeg)
        }

        v.btnSKCPNS.setOnClickListener {
            openPdf(dataPengajuan.SKCpns)
        }

        v.btnSKPNS.setOnClickListener {
            openPdf(dataPengajuan.SKPns)
        }

        v.btnSKPangkatTerakhir.setOnClickListener {
            openPdf(dataPengajuan.SKPangkatTerakhir)
        }

        v.btnSKJabatanTerakhir.setOnClickListener {
            openPdf(dataPengajuan.SKJabatanTerakhir)
        }

        v.btn6.setOnClickListener {
            openPdf(dataPengajuan.SuratPelantikan)
        }

        v.btn7.setOnClickListener {
            openPdf(dataPengajuan.SuratTugas)
        }

        v.btnSKP.setOnClickListener {
            openPdf(dataPengajuan.SKP)
        }

        v.btnIjazah.setOnClickListener {
            openPdf(dataPengajuan.Ijazah)
        }

        v.btnTranskrip.setOnClickListener {
            openPdf(dataPengajuan.Transkrip)
        }
    }

    private fun getDataPelaksana(){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE

                getDataStruktural()
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanPelaksana::class.java)

                    if (data != null){
                        onClickPelaksana(data)
                    }
                }
                else{
                    getDataStruktural()
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanPelaksana")
            .child("${savedData.getDataUser()?.nip}__${savedData.getDataUser()?.tglPengajuan}")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getDataStruktural(){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanStruktural::class.java)

                    if (data != null){
                        onClickStruktural(data)
                    }
                }
                else{
                    Toast.makeText(context, "Anda belum mengajukan berkas", Toast.LENGTH_LONG).show()
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .child("${savedData.getDataUser()?.nip}__${savedData.getDataUser()?.tglPengajuan}")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}