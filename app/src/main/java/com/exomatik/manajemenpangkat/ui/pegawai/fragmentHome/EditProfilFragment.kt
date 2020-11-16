package com.exomatik.manajemenpangkat.ui.pegawai.fragmentHome

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_edit_profil.view.*
import java.text.SimpleDateFormat
import java.util.*

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
        savedData.getDataUser()?.let {
            setData(it)
            onClick(it.nip)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(dataUser: ModelUser){
        v.etNip.editText?.setText(dataUser.nip)
        v.etNama.editText?.setText(dataUser.nama)
        v.etTempatLahir.editText?.setText(dataUser.tempatLahir)
        v.etTglLahir.editText?.setText(dataUser.tanggalLahir)
        if (dataUser.jenisKelamin == "Perempuan"){
            v.btnRadio.check(R.id.rbJk2)
        }
        else if (dataUser.jenisKelamin == "Laki-Laki"){
            v.btnRadio.check(R.id.rbJk1)
        }
        v.etPendidikanLast.editText?.setText(dataUser.pendidikanLast)
    }

    private fun onClick(nip: String) {
        v.btnSimpan.setOnClickListener {
            val radio = v.btnRadio.checkedRadioButtonId

            if (radio <= 0){
                throw Exception("Error, Anda belum memilih jenis kelamin")
            }
            else{
                val btnJK  = view?.findViewById<RadioButton?>(radio)

                val dataUser = savedData.getDataUser()
                dataUser?.nama = v.etNama.editText?.text.toString()
                dataUser?.tempatLahir = v.etTempatLahir.editText?.text.toString()
                dataUser?.tanggalLahir = v.etTglLahir.editText?.text.toString()
                dataUser?.jenisKelamin = btnJK?.text.toString()
                dataUser?.pendidikanLast = v.etPendidikanLast.editText?.text.toString()

                v.progress.visibility = View.VISIBLE
                FirebaseDatabase.getInstance().getReference("Users").child(nip).child("nama").setValue(v.etNama.editText?.text.toString())
                FirebaseDatabase.getInstance().getReference("Users").child(nip).child("tempatLahir").setValue(v.etTempatLahir.editText?.text.toString())
                FirebaseDatabase.getInstance().getReference("Users").child(nip).child("tanggalLahir").setValue(v.etTglLahir.editText?.text.toString())
                FirebaseDatabase.getInstance().getReference("Users").child(nip).child("jenisKelamin").setValue(btnJK?.text.toString())
                FirebaseDatabase.getInstance().getReference("Users").child(nip).child("pendidikanLast").setValue(v.etPendidikanLast.editText?.text.toString())

                fragmentManager?.beginTransaction()?.replace(
                    R.id.fragment_container
                    , HomePegawaiFragment()
                )?.commit()

                savedData.setDataObject(dataUser
                    , "Users")
                Toast.makeText(context, "Berhasil menyimpan data", Toast.LENGTH_LONG).show()
                v.progress.visibility = View.GONE
            }
        }

        v.btnPickTanggal.setOnClickListener {
            val datePickerDialog: DatePickerDialog
            val localCalendar = Calendar.getInstance()

            try {
                datePickerDialog = DatePickerDialog(
                    context ?: throw Exception("Error, mohon mulai ulang aplikasi"),
                    DatePickerDialog.OnDateSetListener { _, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3 ->
                        val dateSelected = Calendar.getInstance()
                        dateSelected[paramAnonymousInt1, paramAnonymousInt2] = paramAnonymousInt3
                        val dateFormatter = SimpleDateFormat("dd-M-yyyy", Locale.US)
                        v.etTglLahir.editText?.setText(dateFormatter.format(dateSelected.time))
                    },
                    localCalendar[Calendar.YEAR]
                    ,
                    localCalendar[Calendar.MONTH]
                    ,
                    localCalendar[Calendar.DATE]
                )

                datePickerDialog.show()
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}