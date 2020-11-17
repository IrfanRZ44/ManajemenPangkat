package com.exomatik.manajemenpangkat.ui.bagianKepegawaian.fragment.daftarSK.struktural

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import kotlinx.android.synthetic.main.item_pengajuan.view.*

class AdapterDaftarSKStrukturalKepegawaian (private val listKelas : ArrayList<ModelUsulanStruktural>,
                                            private val getDataPengurus: (String, AppCompatTextView) -> Unit,
                                            private val onClickItem: (ModelUsulanStruktural) -> Unit
) : RecyclerView.Adapter<AdapterDaftarSKStrukturalKepegawaian.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        fun bindAfiliasi(item: ModelUsulanStruktural){
            getDataPengurus(item.nip, viewItem.textName)

            viewItem.textAwal.text = item.pangkatAwal
            viewItem.textUsulan.text = item.pangkatUsulan
            viewItem.textTanggal.text = item.tglBagianKepegawaian

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
