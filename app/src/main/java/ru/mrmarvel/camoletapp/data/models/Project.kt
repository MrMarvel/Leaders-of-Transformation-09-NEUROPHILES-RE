package ru.mrmarvel.camoletapp.data.models

import com.google.gson.annotations.SerializedName

data class Project(
    val id: Int,
    val title: String,
    val address: String,
    val coordinates: String?,
    @SerializedName("area_coordinates")
    val areaCoordinates: String
)

data class House(
    val id: Int,
    val title: String,
    val coordinates: String?,
)

data class Section(
    val id: Int,
    @SerializedName("section_number")
    val title: String,
    val coordinates: String?,
)

data class Floor(
    val id: Int,
    @SerializedName("floor_number")
    val floorNumber: String
)

data class Flat(
    val id: Int = 0,
    val id_floor: Int = 0,
    @SerializedName("apartment_number")
    val appNumber: Int = 0,
    val coordinates: String? = null,
    val sockets: Int = 0,
    val switches: Int = 0,
    val toilet: Boolean = false,
    val sink: Boolean = false,
    val bath: Boolean = false,
    @SerializedName("floor_finishing")
    val floorFinish: Int = 0,
    @SerializedName("draft_floor_department")
    val floorRough: Int = 0,
    @SerializedName("ceiling_finishing")
    val ceilingFinish: Int = 0,
    @SerializedName("draft_ceiling_finish")
    val ceilingRough: Int = 0,
    @SerializedName("wall_finishing")
    val wallFinish: Int = 0,
    @SerializedName("draft_wall_finish")
    val wallRough: Int = 0,
    val windowsill: Int = 0,
    val kitchen: Boolean = false,
    val slopes: Int = 0,
    val doors: Int = 0,
    @SerializedName("wall_plaster")
    val wallPlaster: Int = 0,
    val trash: Boolean = false,
    val radiator: Int = 0,
    @SerializedName("floor_plaster")
    val floorPlaster: Int = 0,
    @SerializedName("ceiling_plaster")
    val ceilingPlaster: Int = 0,
    val windows: Int = 0
)

data class ResultNearestObject(
    var project: Project? = null,
    var house: House? = null,
    var section: Section? = null,
    var floor: Floor? = null,
    var flatsList: List<Flat> = listOf()
)