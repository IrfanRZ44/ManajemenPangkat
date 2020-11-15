package com.exomatik.manajemenpangkat.ui.adminFakultas

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import com.exomatik.manajemenpangkat.utils.DetailPDFActivity
import kotlinx.android.synthetic.main.item_notifikasi_pegawai.view.*

class AdapterRiwayatFakultas (private val listKelas : ArrayList<ModelNotifikasiPegawai>,
                              private val context: Context?
) : RecyclerView.Adapter<AdapterRiwayatFakultas.AfiliasiHolder>(){

    inner class AfiliasiHolder(private val viewItem : View) : RecyclerView.ViewHolder(viewItem){
        @Suppress("DEPRECATION")
        @SuppressLint("SetTextI18n")
        fun bindAfiliasi(item: ModelNotifikasiPegawai){
            viewItem.textName.text = "Proses ${item.namaStatus}"
            viewItem.textTanggal.text = item.tgl
            when (item.status) {
                1 -> {
                    viewItem.textProgress.text = "Selesai"
                    context?.resources?.getColor(R.color.colorPrimaryDark)?.let {
                        viewItem.textProgress.setTextColor(
                            it
                        )
                    }
                    val img = context?.resources?.getDrawable(R.drawable.ic_true_green)
                    viewItem.textProgress.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
                    if (item.namaStatus != "Pengajuan Berkas"){
                        viewItem.setOnClickListener {
                            val intent = Intent(context, DetailPDFActivity::class.java)
                            intent.putExtra("urlFile", item.url)
                            context?.startActivity(intent)
                        }
                    }
                }
                2 -> {
                    viewItem.textProgress.text = "Ditolak"
                    context?.resources?.getColor(R.color.red1)?.let {
                        viewItem.textProgress.setTextColor(
                            it
                        )
                    }
                    val img = context?.resources?.getDrawable(R.drawable.ic_close_red)
                    viewItem.textProgress.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)

                    viewItem.setOnClickListener {
                        Toast.makeText(context, "Alasan di tolak : ${item.memo}", Toast.LENGTH_LONG).show()
                    }
                }
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
