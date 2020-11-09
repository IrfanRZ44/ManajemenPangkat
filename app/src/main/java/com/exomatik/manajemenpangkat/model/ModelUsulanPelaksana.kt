package com.exomatik.manajemenpangkat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelUsulanPelaksana(
    var Karpeg: String = "",
    var SKCpns: String = "",
    var SKPns: String = "",
    var SKPangkatTerakhir: String = "",
    var SKJabatanTerakhir: String = "",
    var PAKTerakhir: String = "",
    var FungsionalTerakhir: String = "",
    var SKP: String = "",
    var Ijazah: String = "",
    var Transkrip: String = "",
    var statusPengajuan: Boolean = false,
    var statusAdminFakultas: Boolean = false,
    var statusRektor: Boolean = false,
    var statusBagianUmum: Boolean = false,
    var statusBagianKepegawaian: Boolean = false,
    var statusBKN: Boolean = false,
    var tglPengajuan: String = "",
    var tglAdminFakultas: String = "",
    var tglRektor: String = "",
    var tglBagianUmum: String = "",
    var tglBagianKepegawaian: String = "",
    var tglBKN: String = "",
    var pangkatAwal: String = "",
    var pangkatUsulan: String = ""
    ) : Parcelable