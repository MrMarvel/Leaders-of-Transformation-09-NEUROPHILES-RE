package ru.mrmarvel.camoletapp.util

import android.location.Location
import android.util.Log
import androidx.compose.ui.unit.min
import ru.mrmarvel.camoletapp.data.SharedViewModel
import ru.mrmarvel.camoletapp.data.models.Floor
import ru.mrmarvel.camoletapp.data.models.House
import ru.mrmarvel.camoletapp.data.models.Project
import ru.mrmarvel.camoletapp.data.models.ResultNearestObject
import ru.mrmarvel.camoletapp.data.models.Section
import ru.mrmarvel.camoletapp.data.sources.ProjectSource
import java.util.stream.Stream

class DistanceCounter {
    fun split(coords: String): DoubleArray {
        return Stream.of(*coords.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
            .mapToDouble { s: String -> s.toDouble() }
            .toArray()
    }

    suspend fun getNearestObject(sharedViewModel: SharedViewModel): ResultNearestObject {
        val currLocation = sharedViewModel.currentLocation.value
        val currSource = sharedViewModel.projectRepository
        val resultObject = ResultNearestObject()
        if (currLocation == null) return resultObject

        Log.d("MYDEBUG", "GETTING NEAREST OBJ")
        val projects = currSource.getProjects()
        var minDist: Double = Double.MAX_VALUE
        for (project in projects){
            if (project.coordinates == null) continue
            val projLocation = Location("")
            val coords = split(project.coordinates)
            projLocation.latitude = coords[0]
            projLocation.longitude = coords[1]
            val dist = currLocation.distanceTo(projLocation).toDouble()
            if (dist < minDist){
                minDist = dist
                resultObject.project = project
            }
        }

        // ЖОСКИЙ КОСТЫЛЬ ДЛЯ ОТОБРАЖЕНИЯ
        // ППОТОМУ ЧТО В БД НЕ ВСЕ КООРДИНАТЫ ЕСТЬ
        resultObject.project = projects[0]
        val houses = currSource.getHouseByIdProject(listOf(resultObject.project!!.id))
        minDist = Double.MAX_VALUE
        for (house in houses){
            if (house.coordinates == null) continue
            val projLocation = Location("")
            val coords = split(house.coordinates)
            projLocation.latitude = coords[0]
            projLocation.longitude = coords[1]
            val dist = currLocation.distanceTo(projLocation).toDouble()
            if (dist < minDist){
                minDist = dist
                resultObject.house = house
            }
        }

        // ЖОСКИЙ КОСТЫЛЬ ДЛЯ ОТОБРАЖЕНИЯ
        // ППОТОМУ ЧТО В БД НЕ ВСЕ КООРДИНАТЫ ЕСТЬ
        resultObject.house = houses[0]
        val sections = currSource.getSectionByIdHouse(listOf(resultObject.house!!.id))
        minDist = Double.MAX_VALUE
        for (section in sections){
            if (section.coordinates == null) continue
            val projLocation = Location("")
            val coords = split(section.coordinates)
            projLocation.latitude = coords[0]
            projLocation.longitude = coords[1]
            val dist = currLocation.distanceTo(projLocation).toDouble()
            if (dist < minDist){
                minDist = dist
                resultObject.section = section
            }
        }

        // ЖОСКИЙ КОСТЫЛЬ ДЛЯ ОТОБРАЖЕНИЯ
        // ППОТОМУ ЧТО В БД НЕ ВСЕ КООРДИНАТЫ ЕСТЬ
        resultObject.section = sections[0]

        resultObject.floor = currSource.getFloorByIdSection(listOf(resultObject.section!!.id))[0]

        return resultObject
    }
}