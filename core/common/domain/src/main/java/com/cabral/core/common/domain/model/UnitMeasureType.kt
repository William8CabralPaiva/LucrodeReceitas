package com.cabral.core.common.domain.model

enum class UnitMeasureType(val unit: String, val type: String) {
    U("Unidades", "U"),
    KG("Kg", "K"),
    G("g", "G"),
    MG("mg", "MG"),
    L("L", "l"),
    DL("dl", "dl"),
    ML("ml", "ml")
}

fun allUnitMeasure(): List<UnitMeasureType> {
    val list = mutableListOf<UnitMeasureType>()

    val unit = UnitMeasureType.U
    list.add(unit)
    list.addAll(listMassUnitMeasure())
    list.addAll(listLiquidUnitMeasure())

    return list

}

fun listMassUnitMeasure(): List<UnitMeasureType> {
    val list = mutableListOf<UnitMeasureType>()

    list.run {
        add(UnitMeasureType.KG)
        add(UnitMeasureType.G)
        add(UnitMeasureType.MG)
    }
    return list
}

fun listLiquidUnitMeasure(): List<UnitMeasureType> {
    val list = mutableListOf<UnitMeasureType>()


    list.run {
        add(UnitMeasureType.L)
        add(UnitMeasureType.DL)
        add(UnitMeasureType.ML)
    }
    return list
}

fun listMeasure(list: List<UnitMeasureType>): List<String> {
    val stringList = mutableListOf<String>()
    list.forEach {
        stringList.add(it.unit)
    }
    return stringList
}
