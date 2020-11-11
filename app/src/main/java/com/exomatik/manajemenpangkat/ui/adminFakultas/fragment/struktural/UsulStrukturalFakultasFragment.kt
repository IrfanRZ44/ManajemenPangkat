package com.exomatik.manajemenpangkat.ui.adminFakultas.fragment.struktural

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_usul_pelaksana_fakultas.view.*

class UsulStrukturalFakultasFragment : Fragment() {
    private lateinit var savedData : DataSave
    private var listPengajuan = ArrayList<ModelUsulanStruktural>()
    private var adapter: AdapterUsulStrukturalFakultas? = null
    private lateinit var v : View

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_usul_struktural_fakultas, paramViewGroup, false)

        myCodeHere()
        onClick()
        return v
    }

    private fun myCodeHere(){
        savedData = DataSave(context)
        adapter =
            AdapterUsulStrukturalFakultas(
                listPengajuan,
                { nip: String, textNama: AppCompatTextView -> getDataPegawai(nip, textNama) },
                { item: ModelUsulanStruktural -> onClickItem(item) })
        v.rcProgress.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        v.rcProgress.adapter = adapter

        getDataPelaksana()
    }

    private fun onClick() {
        v.swipeRefresh.setOnRefreshListener {
            getDataPelaksana()
            v.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickItem(item: ModelUsulanStruktural) {
        Toast.makeText(context, item.nip, Toast.LENGTH_SHORT).show()
    }

    private fun getDataPegawai(nip: String, textNama: AppCompatTextView) {
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE
                textNama.text = nip
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUser::class.java)

                    textNama.text = "${data?.nama} - $nip"
                }
                else{
                    textNama.text = nip
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }

    @Suppress("DEPRECATION")
    private fun getDataPelaksana(){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE
                v.textStatus.text = result.message
                v.textStatus.visibility = View.VISIBLE
            }

            @SuppressLint("SetTextI18n")
            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    for (snapshot in result.children) {
                        val data = snapshot.getValue(ModelUsulanStruktural::class.java)
                        Log.e("Data",  "${data?.nip} asda")
                        if (data != null){
                            if (data.statusPengajuan && !data.statusAdminFakultas){
                                listPengajuan.add(data)
                                adapter?.notifyDataSetChanged()
                                v.textStatus.visibility = View.GONE
                            }
                        }
                    }

                    if (listPengajuan.size == 0){
                        v.textStatus.visibility = View.VISIBLE
                        v.textStatus.text = "Belum ada data pengajuan"
                    }
                    else{
                        v.textStatus.visibility = View.GONE
                        v.textStatus.text = ""
                    }
                }
                else{
                    v.textStatus.text = "Belum ada data pengajuan"
                    v.textStatus.visibility = View.VISIBLE
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}