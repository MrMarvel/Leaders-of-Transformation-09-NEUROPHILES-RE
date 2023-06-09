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
    val id: Int,
    val id_floor: Int,
    @SerializedName("apartment_number")
    val appNumber: Int,
    val coordinates: String?,
    val sockets: Int,
    val switches: Int,
    val toilet: Boolean,
    val sink: Boolean,
    val bath: Boolean,
    @SerializedName("floor_finishing")
    val floorFinish: Int,
    @SerializedName("draft_floor_department")
    val floorRough: Int,
    @SerializedName("ceiling_finishing")
    val ceilingFinish: Int,
    @SerializedName("draft_ceiling_finish")
    val ceilingRough: Int,
    @SerializedName("wall_finishing")
    val wallFinish: Int,
    @SerializedName("draft_wall_finish")
    val wallRough: Int,
    val windowsill: Int,
    val kitchen: Boolean,
    val slopes: Int,
    val doors: Int,
    @SerializedName("wall_plaster")
    val wallPlaster: Int,
    val trash: Boolean,
    val radiator: Int,
    @SerializedName("floor_plaster")
    val floorPlaster: Int,
    @SerializedName("ceiling_plaster")
    val ceilingPlaster: Int,
    val windows: Int
)

data class ResultNearestObject(
    var project: Project? = null,
    var house: House? = null,
    var section: Section? = null,
    var floor: Floor? = null,
    var flatsList: List<Flat> = listOf()
)