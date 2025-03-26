package com.cabral.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cabral.core.common.domain.model.IngredientRecipeRegister
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class IngredientRecipeRegisterArgs(
    var keyDocument: String? = null,
    var volumeUsed: Float? = null
): Parcelable

fun MutableList<IngredientRecipeRegisterArgs>.toIngredientRecipeRegister(): MutableList<IngredientRecipeRegister>? {
    val list = mutableListOf<IngredientRecipeRegister>()
    forEach {
        val ingredient =  IngredientRecipeRegister(it.keyDocument, it.volumeUsed)
        list.add(ingredient)
    }
    return list
}


fun MutableList<IngredientRecipeRegister>.toIngredientRecipeRegisterArgs():MutableList<IngredientRecipeRegisterArgs>{
    val list = mutableListOf<IngredientRecipeRegisterArgs>()
    forEach {
        val ingredient =  IngredientRecipeRegisterArgs(it.keyDocument, it.volumeUsed)
        list.add(ingredient)
    }
    return list
}

