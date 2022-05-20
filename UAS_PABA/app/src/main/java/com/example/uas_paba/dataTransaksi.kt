package com.example.uas_paba

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataTransaksi(
    var ID_Transaction : String,
    var Date : String,
    var Type : String,
    var NamaCategory : String,
    var Note : String,
    var Money : String
) : Parcelable
