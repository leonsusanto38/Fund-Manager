package com.example.uas_paba

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class dataKategori(
    var ID_Category : String,
    var NamaKategori : String
) : Parcelable
