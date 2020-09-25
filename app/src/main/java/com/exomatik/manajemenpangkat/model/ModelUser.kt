package com.exomatik.manajemenpangkat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUser(
    var oldNip: String = "",
    var newNip: String = "",
    var token: String = "",
    var jenisAkun: String = "",
    var password: String = "",
    var frontGelar: String = "",
    var nama: String = "",
    var backGelar: String = "",
    var jabatan: String = "",
    var gol: String = "",
    var tglLahir: String = "",
    var pendTertinggi: String = "",
    var nomorSKJabatan: String = "",
    var nomorSKPangkat: String = "",
    var tglSKPangkat: String = "",
    var tglSKJabatan: String = "",
    var tmtPangkat: String = "",
    var jenisKelamin: String = "",
    var tmtJabatan: String = "",
    var tempatLahir: String = "",
    var tanggalLahir: String = ""
    ) : Parcelable