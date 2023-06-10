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
    var id: Int = 0,
    var id_floor: Int = 0,
    @SerializedName("apartment_number")
    var appNumber: Int = 0,
    var coordinates: String? = null,
    var sockets: Int = 0,
    var switches: Int = 0,
    var toilet: Boolean = false,
    var sink: Boolean = false,
    var bath: Boolean = false,
    @SerializedName("floor_finishing")
    var floorFinish: Float = 0.0f,
    @SerializedName("draft_floor_department")
    var floorRough: Float = 0.0f,
    @SerializedName("ceiling_finishing")
    var ceilingFinish: Float = 0.0f,
    @SerializedName("draft_ceiling_finish")
    var ceilingRough: Float = 0.0f,
    @SerializedName("wall_finishing")
    var wallFinish: Float = 0.0f,
    @SerializedName("draft_wall_finish")
    var wallRough: Float = 0.0f,
    var windowsill: Float = 0.0f,
    var kitchen: Boolean = false,
    var slopes: Float = 0.0f,
    var doors: Float = 0.0f,
    @SerializedName("wall_plaster")
    var wallPlaster: Float = 0.0f,
    var trash: Boolean = false,
    var radiator: Float = 0.0f,
    @SerializedName("floor_plaster")
    var floorPlaster: Float = 0.0f,
    @SerializedName("ceiling_plaster")
    var ceilingPlaster: Float = 0.0f,
    var windows: Int = 0,
)

data class ResultNearestObject(
    var project: Project? = null,
    var house: House? = null,
    var section: Section? = null,
    var floor: Floor? = null,
    var flatsList: List<Flat> = listOf()
)