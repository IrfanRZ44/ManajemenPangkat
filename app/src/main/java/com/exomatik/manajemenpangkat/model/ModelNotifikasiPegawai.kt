package com.exomatik.manajemenpangkat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelNotifikasiPegawai(
    var tgl: String = "",
    var namaStatus: String = "",
    var url: String = "",
    var memo: String = "",
    var status: Int= 0
    ) : Parcelable