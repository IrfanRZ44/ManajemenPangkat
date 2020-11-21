package com.exomatik.manajemenpangkat.ui.pegawai.fragmentArsip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import kotlinx.android.synthetic.main.item_arsip_nota.view.*

class AdapterNotaPegawai (private val listKelas : ArrayList<ModelNotifikasiPegawai>,
                          private val openPdf: (String) -> Unit,
                          private val activity: Activity?
) : RecyclerView.Adapter<AdapterNotaPegawai.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @Suppress("DEPRECATION")
        fun bindAfiliasi(item: ModelNotifikasiPegawai){
            viewItem.textName.text = item.namaStatus
            viewItem.textTanggal.text = item.tgl
            viewItem.setOnClickListener {
                openPdf(item.url)
            }

            viewItem.btnDownload.setOnClickListener {
                val intent = Intent()
                intent.type = Intent.ACTION_VIEW
                intent.data = Uri.parse(item.url)
                activity?.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AfiliasiHolder {
        return AfiliasiHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_arsip_nota, parent, false))
    }
    override fun getItemCount(): Int = listKelas.size
    override fun onBindViewHolder(holder: AfiliasiHolder, position: Int) {
        holder.bindAfiliasi(listKelas[position])
    }
}
