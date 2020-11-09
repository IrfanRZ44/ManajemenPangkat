package com.exomatik.manajemenpangkat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelNotifikasiPegawai(
    var tgl: String = "",
    var namaStatus: String = "",
    var status: Boolean= false
    ) : Parcelable