package com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK.struktural

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

class DaftarSKStrukturalKepegawaianFragment : Fragment() {
    private lateinit var savedData : DataSave
    private var listPengajuan = ArrayList<ModelUsulanStruktural>()
    private var adapterUsulan: AdapterDaftarSKStrukturalKepegawaian? = null
    private lateinit var v : View

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_usul_struktural_fakultas, paramViewGroup, false)

        myCodeHere()
        onClick()
        return v
    }

    private fun myCodeHere(){
        savedData = DataSave(context)
        adapterUsulan =
            AdapterDaftarSKStrukturalKepegawaian(
                listPengajuan,
                { nip: String, textNama: AppCompatTextView -> getDataPegawai(nip, textNama) },
                { item: ModelUsulanStruktural -> onClickItem(item) })
        v.rcProgress.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        v.rcProgress.adapter = adapterUsulan

        getDataPelaksana()
    }

    private fun onClick() {
        v.swipeRefresh.setOnRefreshListener {
            listPengajuan.clear()
            adapterUsulan?.notifyDataSetChanged()
            getDataPelaksana()
            v.swipeRefresh.isRefreshing = false
        }
    }

    private fun onClickItem(item: ModelUsulanStruktural) {
        val intent = Intent(activity, DetailSKStrukturalKepegawaianActivity::class.java)
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
                            if (data.statusPengajuan == "BKN" && !data.statusDitolak && data.disposisiSKBaru.isEmpty()){
                                listPengajuan.add(data)
                                adapterUsulan?.notifyDataSetChanged()
                                v.textStatus.visibility = View.GONE
                            }
                        }
                    }

                    if (listPengajuan.size == 0){
                        v.textStatus.visibility = View.VISIBLE
                        v.textStatus.text = "Belum ada disposisi"
                    }
                    else{
                        v.textStatus.visibility = View.GONE
                        v.textStatus.text = ""
                    }
                }
                else{
                    v.textStatus.text = "Belum ada disposisi"
                    v.textStatus.visibility = View.VISIBLE
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}