package ru.mrmarvel.camoletapp.data.repository

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import ru.mrmarvel.camoletapp.data.models.Flat
import ru.mrmarvel.camoletapp.data.models.Floor
import ru.mrmarvel.camoletapp.data.models.House
import ru.mrmarvel.camoletapp.data.models.Project
import ru.mrmarvel.camoletapp.data.models.Section
import ru.mrmarvel.camoletapp.data.sources.ProjectSource
import java.io.File

class ProjectRepository(private val projectSource: ProjectSource) {
    var projects = mutableStateOf(listOf<Project>())
    suspend fun getProjects(): List<Project> {
        projects.value = projectSource.getProjects()
        return projects.value
    }

    suspend fun getSectionByIdHouse(ids: List<Int>): List<Section> {
        return projectSource.getSectionByIdHouse(ids)
    }

    suspend fun getFloorByIdSection(ids: List<Int>): List<Floor> {
        return projectSource.getFloorByIdSection(ids)
    }

    suspend fun getHouseByIdProject(ids: List<Int>): List<House> {
        return projectSource.getHouseByIdProject(ids)
    }

    suspend fun getAllFlatsOnFloor(ids: Int): List<Flat> {
        return projectSource.getFlatsByIdFloor(ids)
    }

    suspend fun putFlatStat(flats: List<Flat>) {
        projectSource.putFlatStat(flats)
    }

    suspend fun putChess(houseNumber: Int) {
        projectSource.putChess(houseNumber)
    }
}