package com.exomatik.manajemenpangkat.ui.bkn.fragment.struktural

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class UsulStrukturalBKNFragment : Fragment() {
    private lateinit var savedData : DataSave
    private var listPengajuan = ArrayList<ModelUsulanStruktural>()
    private var adapter: AdapterUsulStrukturalBKN? = null
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
            AdapterUsulStrukturalBKN(
                listPengajuan,
                { nip: String, textNama: AppCompatTextView -> getDataPegawai(nip, textNama) },
                { item: ModelUsulanStruktural -> onClickItem(item) })
        v.rcProgress.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        v.rcProgress.adapter = adapter

        getDataPelaksana()
    }

    private fun onClick() {
        v.swipeRefresh.setOnRefreshListener {
            listPengajuan.clear()
            adapter?.notifyDataSetChanged()
            getDataPelaksana()
            v.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickItem(item: ModelUsulanStruktural) {
        val intent = Intent(activity, DetailStrukturalBKNActivity::class.java)
        intent.putExtra("dataPengajuan", item)
        activity?.startActivity(intent)
        activity?.finish()
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
                        if (data != null){
                            if (data.statusPengajuan == "BKN" && !data.statusDitolak && data.disposisiBKN.isEmpty()){
                                listPengajuan.add(data)
                                adapter?.notifyDataSetChanged()
                                v.textStatus.visibility = View.GONE
                            }
                        }
                    }

                    if (listPengajuan.size == 0){
                        v.textStatus.visibility = View.VISIBLE
                        v.textStatus.text = "Belum ada nota usulan"
                    }
                    else{
                        v.textStatus.visibility = View.GONE
                        v.textStatus.text = ""
                    }
                }
                else{
                    v.textStatus.text = "Belum ada nota usulan"
                    v.textStatus.visibility = View.VISIBLE
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}