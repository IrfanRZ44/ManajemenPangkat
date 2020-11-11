package com.exomatik.manajemenpangkat.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.exomatik.manajemenpangkat.R
import com.exomatik.manajemenpangkat.model.ModelUser
import com.exomatik.manajemenpangkat.ui.pegawai.MainPegawaiActivity
import com.exomatik.manajemenpangkat.utils.DataSave
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity(){
    private lateinit var savedData : DataSave

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        myCodeHere()
        onClick()
    }

    private fun myCodeHere(){
        savedData = DataSave(this)

    }

    private fun onClick() {
        etPassword.editText?.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                cekEditText()
                return@OnEditorActionListener false
            }
            false
        })

        btnLogin.setOnClickListener {
            cekEditText()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cekEditText(){
        val nip = etNip.editText?.text.toString()
        val password = etPassword.editText?.text.toString()

        if (nip.isNotEmpty() && password.isNotEmpty()){
            textStatus.text = ""
            onClickLogin(nip, md5(password))
        }
        else{
            if (nip.isEmpty()){
                textStatus.text = "Mohon masukkan NIP"
            }
            else if (password.isEmpty()){
                textStatus.text = "Mohon masukkan Password"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickLogin(nip: String, password: String){
        progress.visibility = View.VISIBLE

        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(result: DatabaseError) {
                progress.visibility = View.GONE
                textStatus.text = "Error, ${result.message}"
            }

            override fun onDataChange(result: DataSnapshot) {
                if (result.exists()) {
                    val data = result.getValue(ModelUser::class.java)

                    if (data?.password == password && data.nip == nip){
                      getUserToken(data)
                    }
                    else{
                        progress.visibility = View.GONE
                        textStatus.text = "Error, password salah"
                    }
                } else {
                    progress.visibility = View.GONE
                    textStatus.text = "Error, NIP belum terdaftar"
                }
            }
        }

        FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(nip)
            .addListenerForSingleValueEvent(valueEventListener)
    }

    @SuppressLint("SetTextI18n")
    private fun getUserToken(dataUser: ModelUser){
        val onCompleteListener =
            OnCompleteListener<InstanceIdResult> { result ->
                if (result.isSuccessful) {
                    result.result?.token?.let { saveToken(dataUser, it) }
                } else {
                    progress.visibility = View.GONE
                    textStatus.text = "Gagal mendapatkan token"
                }
            }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(onCompleteListener)
    }

    @SuppressLint("SetTextI18n")
    private fun saveToken(dataUser: ModelUser, token: String){
        val onCompleteListener =
            OnCompleteListener<Void> { result ->
                progress.visibility = View.GONE
                if (result.isSuccessful) {
                    dataUser.token = token
                    moveActivity(dataUser)
                } else {
                    textStatus.text = "Gagal menyimpan data user"
                }
            }

        val onFailureListener = OnFailureListener { result ->
            progress.visibility = View.GONE
            textStatus.text = result.message
        }

        FirebaseDatabase.getInstance().getReference("Users")
            .child(dataUser.nip)
            .child("token")
            .setValue(token)
            .addOnCompleteListener(onCompleteListener)
            .addOnFailureListener(onFailureListener)
    }

    @SuppressLint("SetTextI18n")
    private fun moveActivity(dataUser: ModelUser){
        savedData.setDataObject(dataUser, "Users")
        progress.visibility = View.GONE
        textStatus.text = "Berhasil login"
        Toast.makeText(this, "Berhasil login", Toast.LENGTH_LONG).show()
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun md5(s: String): String {
        val type = "MD5"
        try {
            // Create MD5 Hash
            val digest = MessageDigest
                .getInstance(type)
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}