package com.cabral.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.annotation.Keep

@Keep
@Parcelize
data class RecipeArgs(
    var id:Int
) : Parcelable