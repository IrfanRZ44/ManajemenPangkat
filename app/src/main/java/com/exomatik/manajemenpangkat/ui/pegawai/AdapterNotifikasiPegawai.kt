package com.exomatik.manajemenpangkat.ui.pegawai

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import kotlinx.android.synthetic.main.item_notifikasi_pegawai.view.*

class AdapterNotifikasiPegawai (private val listKelas : ArrayList<ModelNotifikasiPegawai>,
                                private val context: Context?
) : RecyclerView.Adapter<AdapterNotifikasiPegawai.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @Suppress("DEPRECATION")
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelNotifikasiPegawai){
            viewItem.textName.text = "Proses ${item.namaStatus}"
            viewItem.textTanggal.text = item.tgl
            if (item.status){
                viewItem.textProgress.text = "Selesai"
                val img = context?.resources?.getDrawable(R.drawable.ic_true_green)
                viewItem.textProgress.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
            else{
                viewItem.textProgress.text = "Sedang diproses"
                val img = context?.resources?.getDrawable(R.drawable.ic_time_gray)
                viewItem.textProgress.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notifikasi_pegawai, parent, false))
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
