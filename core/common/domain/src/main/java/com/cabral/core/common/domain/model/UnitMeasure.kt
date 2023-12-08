package com.cabral.core.common.domain.model

data class UnitMeasure(
    val name: String,
    val type: UnitMeasureType
)

fun allUnitMeasure(): List<UnitMeasure> {
    val list = mutableListOf<UnitMeasure>()

    val unit = UnitMeasure("Unidades", UnitMeasureType.U)
    list.add(unit)
    list.addAll(listMassUnitMeasure())
    list.addAll(listLiquidUnitMeasure())

    return list

}

fun listMassUnitMeasure(): List<UnitMeasure> {
    val list = mutableListOf<UnitMeasure>()

    val kilo = UnitMeasure("Kilo", UnitMeasureType.KG)
    val g = UnitMeasure("g", UnitMeasureType.G)
    val mg = UnitMeasure("mg", UnitMeasureType.MG)
    list.run {
        add(kilo)
        add(g)
        add(mg)
    }
    return list
}

fun listLiquidUnitMeasure(): List<UnitMeasure> {
    val list = mutableListOf<UnitMeasure>()

    val l = UnitMeasure("l", UnitMeasureType.L)
    val dl = UnitMeasure("dl", UnitMeasureType.DL)
    val ml = UnitMeasure("ml", UnitMeasureType.ML)

    list.run {
        add(l)
        add(dl)
        add(ml)
    }
    return list
}

fun listMeasure(list: List<UnitMeasure>): List<String> {
    val stringList = mutableListOf<String>()
    list.forEach {
        stringList.add(it.name)
    }
    return stringList
}