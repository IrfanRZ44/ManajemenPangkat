package com.exomatik.manajemenpangkat.ui.bagianUmum.fragment.pelaksana

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import kotlinx.android.synthetic.main.item_pengajuan.view.*

class AdapterUsulPelaksanaBagianUmum (private val listKelas : ArrayList<ModelUsulanPelaksana>,
                                      private val getDataPengurus: (String, AppCompatTextView) -> Unit,
                                      private val onClickItem: (ModelUsulanPelaksana) -> Unit
) : RecyclerView.Adapter<AdapterUsulPelaksanaBagianUmum.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        fun bindAfiliasi(item: ModelUsulanPelaksana){
            getDataPengurus(item.nip, viewItem.textName)

            viewItem.textAwal.text = item.pangkatAwal
            viewItem.textUsulan.text = item.pangkatUsulan
            viewItem.textTanggal.text = item.tglRektor

            viewItem.setOnClickListener {
                onClickItem(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pengajuan, parent, false))
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
