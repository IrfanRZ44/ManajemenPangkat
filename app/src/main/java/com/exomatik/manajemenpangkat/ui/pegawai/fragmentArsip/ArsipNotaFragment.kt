package com.exomatik.manajemenpangkat.ui.pegawai.fragmentArsip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelNotifikasiPegawai
import com.exomatik.manajemenpangkat.model.ModelUsulanPelaksana
import com.exomatik.manajemenpangkat.model.ModelUsulanStruktural
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_arsip_nota.view.*

class ArsipNotaFragment(private val openPdf: (String) -> Unit) : Fragment() {
    private lateinit var savedData : DataSave
    private lateinit var v : View
    private var listProgress = ArrayList<ModelNotifikasiPegawai>()
    private var adapter: AdapterNotaPegawai? = null

    override fun onCreateView(paramLayoutInflater: LayoutInflater, paramViewGroup: ViewGroup?, paramBundle: Bundle?): View? {
        v = paramLayoutInflater.inflate(R.layout.fragment_arsip_nota, paramViewGroup, false)

        savedData = DataSave(context)
        initAdapter()
        getDataPelaksana()

        return v
    }

    private fun initAdapter() {
        adapter = AdapterNotaPegawai(listProgress, { urlFile: String -> openPdf(urlFile) }, activity)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
        v.rcProgress.layoutManager = layoutManager
        v.rcProgress.adapter = adapter
    }

    private fun getDataPelaksana(){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE

                getDataStruktural()
            }

            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanPelaksana::class.java)

                    if (data != null && data.nip == savedData.getDataUser()?.nip){
                        if (data.statusPengajuan == "AdminFakultas" && !data.statusDitolak && data.disposisiAdminFakultas.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "Rektor" && !data.statusDitolak && data.disposisiRektor.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "BagianUmum" && !data.statusDitolak && data.disposisiBagianUmum.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", data.disposisiBagianUmum, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "BagianKepegawaian" && !data.statusDitolak && data.disposisiBagianKepegawaian.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", data.disposisiBagianUmum, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", data.disposisiBagianKepegawaian, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "BKN" && !data.statusDitolak && data.disposisiBKN.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", data.disposisiBagianUmum, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", data.disposisiBagianKepegawaian, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN", data.disposisiBKN, "", 1))
                            adapter?.notifyDataSetChanged()

                            if (data.disposisiSKBaru.isNotEmpty()){
                                listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "SK Baru", data.disposisiSKBaru, "", 1))
                                adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }
                else{
                    getDataStruktural()
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanPelaksana")
            .child("${savedData.getDataUser()?.nip}__${savedData.getDataUser()?.tglPengajuan}")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    private fun getDataStruktural(){
        v.progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                v.progress.visibility = View.GONE
                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(result: DataSnapshot) {
                v.progress.visibility = View.GONE

                if (result.exists()) {
                    val data = result.getValue(ModelUsulanStruktural::class.java)

                    if (data != null && data.nip == savedData.getDataUser()?.nip){
                        if (data.statusPengajuan == "AdminFakultas" && !data.statusDitolak && data.disposisiAdminFakultas.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "Rektor" && !data.statusDitolak && data.disposisiRektor.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "BagianUmum" && !data.statusDitolak && data.disposisiBagianUmum.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", data.disposisiBagianUmum, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "BagianKepegawaian" && !data.statusDitolak && data.disposisiBagianKepegawaian.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", data.disposisiBagianUmum, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", data.disposisiBagianKepegawaian, "", 1))
                            adapter?.notifyDataSetChanged()
                        }
                        else if (data.statusPengajuan == "BKN" && !data.statusDitolak && data.disposisiBKN.isNotEmpty()){
                            listProgress.add(ModelNotifikasiPegawai(data.tglAdminFakultas, "Nota Admin Univ/Fakultas", data.disposisiAdminFakultas, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglRektor, "Disposisi Rektor", data.disposisiRektor, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianUmum, "Disposisi Bagian Umum", data.disposisiBagianUmum, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBagianKepegawaian, "Disposisi Kepegawaian", data.disposisiBagianKepegawaian, "", 1))
                            adapter?.notifyDataSetChanged()

                            listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "Persetujuan BKN", data.disposisiBKN, "", 1))
                            adapter?.notifyDataSetChanged()

                            if (data.disposisiSKBaru.isNotEmpty()){
                                listProgress.add(ModelNotifikasiPegawai(data.tglBKN, "SK Baru", data.disposisiSKBaru, "", 1))
                                adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Anda belum mengajukan berkas", Toast.LENGTH_LONG).show()
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("UsulanStruktural")
            .child("${savedData.getDataUser()?.nip}__${savedData.getDataUser()?.tglPengajuan}")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}