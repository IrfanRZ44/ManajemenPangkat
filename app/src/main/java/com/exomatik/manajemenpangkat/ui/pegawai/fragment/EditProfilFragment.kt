package com.exomatik.manajemenpangkat.ui.pegawai.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.utils.DataSave

class EditProfilFragment : Fragment() {
    private lateinit var savedData : DataSave
    private lateinit var v : View

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_edit_profil, paramViewGroup, false)

        savedData = DataSave(context)
        myCodeHere()
        return v
    }

    private fun myCodeHere(){
        savedData.getDataUser()?.let { setData(it) }
        onClick()
    }

    private fun setData(dataUser: ModelUser){

    }

    private fun onClick() {
    }
}