package ru.mrmarvel.camoletapp.data.repository

import androidx.compose.runtime.mutableStateOf
import ru.mrmarvel.camoletapp.data.models.Project
import ru.mrmarvel.camoletapp.data.sources.ProjectSource

class ProjectRepository(private val projectSource: ProjectSource) {
    var projects = mutableStateOf(listOf<Project>())
    suspend fun getAll(): List<Project> {
        projects.value = projectSource.getAll()
        return projects.value
    }

}

