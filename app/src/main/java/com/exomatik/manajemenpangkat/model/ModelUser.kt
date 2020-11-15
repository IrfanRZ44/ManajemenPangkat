package com.exomatik.manajemenpangkat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUser(
    var password: String = "",
    var token: String = "",
    var jenisUser: String = "",
    var gol: String = "",
    var golOld: String = "",
    var jenisKelamin: String = "",
    var nama: String = "",
    var nip: String = "",
    var nomorSKPangkat: String = "",
    var pangkat: String = "",
    var pangkatOld: String = "",
    var pendidikanLast: String = "",
    var tanggalLahir: String = "",
    var tempatLahir: String = "",
    var tglSKPangkat: String = "",
    var tglSKPangkatOld: String = "",
    var tmtPangkat: String = "",
    var tmtPangkatOld: String = "",
    var tglPengajuan: String = ""
    ) : Parcelable