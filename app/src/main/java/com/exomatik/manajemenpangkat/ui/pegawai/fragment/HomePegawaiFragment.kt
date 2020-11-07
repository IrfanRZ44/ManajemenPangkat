package com.exomatik.manajemenpangkat.ui.pegawai.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.pegawai.BiodataActivity
import com.exomatik.manajemenpangkat.ui.pegawai.PangkatActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import kotlinx.android.synthetic.main.fragment_home_pegawai.view.*

class HomePegawaiFragment : Fragment() {
    private lateinit var savedData : DataSave
    private lateinit var v : View

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_home_pegawai, paramViewGroup, false)

        savedData = DataSave(context)
        myCodeHere()
        return v
    }

    private fun myCodeHere(){
        savedData.getDataUser()?.let { setData(it) }
        onClick()
    }

    @SuppressLint("SetTextI18n")
    private fun setData(dataUser: ModelUser){
        v.textNama.text = dataUser.nama
        v.textNip.text = dataUser.nip
        v.textJabatan.text = "Jabatan"
        v.textValueJabatan.text = dataUser.gol
    }

    private fun onClick() {
        v.btnBiodata.setOnClickListener {
            val intent = Intent(activity, BiodataActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

        v.btnPangkat.setOnClickListener {
            val intent = Intent(activity, PangkatActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()
        }

//        btnJabatan.setOnClickListener {
//            val intent = Intent(this, JabatanActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        btnUsulan.setOnClickListener {
//            val intent = Intent(this, UsulanBiodataActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }
}