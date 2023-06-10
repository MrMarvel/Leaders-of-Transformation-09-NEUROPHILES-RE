package ru.mrmarvel.camoletapp.data.sources

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.io.FileSystem
import org.json.JSONArray
import ru.gildor.coroutines.okhttp.await
import ru.mrmarvel.camoletapp.data.models.Flat
import ru.mrmarvel.camoletapp.data.models.Floor
import ru.mrmarvel.camoletapp.data.models.House
import ru.mrmarvel.camoletapp.data.models.Project
import ru.mrmarvel.camoletapp.data.models.Section
import ru.mrmarvel.camoletapp.util.TypeAdapters
import java.io.File


class ProjectSource {
    suspend fun getProjects(): List<Project> {
        try {
            val client = OkHttpClient()
            val queryParams =
                HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
                    .addQueryParameter("sql", "SELECT * FROM projects;")
            val api = Request.Builder()
                .url(queryParams.build())
                .get()
                .build()
            // BLOCKING
            val response = client.newCall(api).await()
            val result = response.body()?.string() ?: "[]"
            Log.d("MYDEBUG", "Get Projects: $result")
            val jsonArray = JSONArray(result)
            val ormArray = mutableListOf<Project>()
            for (i in 0 until jsonArray.length()) {
                val x = GsonBuilder().create()
                    .fromJson(jsonArray.get(i).toString(), Project::class.java)
                ormArray.add(x)
            }
            return ormArray
        }
        catch (e: Exception){
            return mutableListOf<Project>()
        }

    }

    suspend fun getHouseByIdProject(ids: List<Int>): List<House> {
        try {
            val commaIds = ids.joinToString()
            val client = OkHttpClient()
            val queryParams =
                HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
                    .addQueryParameter(
                        "sql",
                        "SELECT * FROM houses WHERE id_project in ($commaIds) ;"
                    )
            val api = Request.Builder()
                .url(queryParams.build())
                .get()
                .build()
            // BLOCKING
            val response = client.newCall(api).await()
            val result = response.body()?.string() ?: "[]"
            Log.d("MYDEBUG", "Get House: $result")
            val jsonArray = JSONArray(result)
            val ormArray = mutableListOf<House>()
            for (i in 0 until jsonArray.length()) {
                val x =
                    GsonBuilder().create().fromJson(jsonArray.get(i).toString(), House::class.java)
                ormArray.add(x)
            }
            return ormArray
        }
        catch (e: Exception){
            return mutableListOf<House>()
        }
    }

    suspend fun getSectionByIdHouse(ids: List<Int>): List<Section> {
        try {
            val commaIds = ids.joinToString()
            val client = OkHttpClient()
            val queryParams =
                HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
                    .addQueryParameter(
                        "sql",
                        "SELECT * FROM sections WHERE id_house in ($commaIds) ;"
                    )
            val api = Request.Builder()
                .url(queryParams.build())
                .get()
                .build()
            // BLOCKING
            val response = client.newCall(api).await()
            val result = response.body()?.string() ?: "[]"
            Log.d("MYDEBUG", "Get Section: $result")
            val jsonArray = JSONArray(result)
            val ormArray = mutableListOf<Section>()
            for (i in 0 until jsonArray.length()) {
                val x = GsonBuilder().create()
                    .fromJson(jsonArray.get(i).toString(), Section::class.java)
                ormArray.add(x)
            }
            return ormArray
        }
        catch (e: Exception){
            return mutableListOf<Section>()
        }
    }

    suspend fun getFloorByIdSection(ids: List<Int>): List<Floor> {
        try {
            val commaIds = ids.joinToString()
            val client = OkHttpClient()
            val queryParams =
                HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
                    .addQueryParameter(
                        "sql",
                        "SELECT * FROM floors WHERE id_section in ($commaIds) AND floor_number = 2;"
                    )
            val api = Request.Builder()
                .url(queryParams.build())
                .get()
                .build()
            // BLOCKING
            val response = client.newCall(api).await()
            val result = response.body()?.string() ?: "[]"
            Log.d("MYDEBUG", "Get Floor: $result")
            val jsonArray = JSONArray(result)
            val ormArray = mutableListOf<Floor>()
            for (i in 0 until jsonArray.length()) {
                val x =
                    GsonBuilder().create().fromJson(jsonArray.get(i).toString(), Floor::class.java)
                ormArray.add(x)
            }
            return ormArray
        }
        catch (e: Exception){
            return mutableListOf<Floor>()
        }
    }

    suspend fun getFlatsByIdFloor(ids: Int): List<Flat> {
        try {
            val client = OkHttpClient()
            val queryParams =
                HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
                    .addQueryParameter("sql", "SELECT * FROM apartments where id_floor = $ids;")
            val api = Request.Builder()
                .url(queryParams.build())
                .get()
                .build()
            // BLOCKING
            val response = client.newCall(api).await()
            val result = response.body()?.string() ?: "[]"
            Log.d("MYDEBUG", "Get Flats: $result")
            val jsonArray = JSONArray(result)
            val ormArray = mutableListOf<Flat>()
            for (i in 0 until jsonArray.length() - 1) {
                val x = TypeAdapters.gson.fromJson(jsonArray.get(i).toString(), Flat::class.java)
//                val x = GsonBuilder().create().fromJson(jsonArray.get(i).toString(), Flat::class.java)
                ormArray.add(x)
            }
            Log.d("MYDEBUG", "Flats: $ormArray")
            return ormArray
        }
        catch (e: Exception){
            Log.d("MYDEBUG", e.toString())
            return mutableListOf<Flat>()
        }
    }

    suspend fun putFlatStat(flats: List<Flat>) {
        try {
            val client = OkHttpClient()
            for (flat in flats){
                val queryParams =
                    HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
                        .addQueryParameter("sql", "UPDATE apartments SET " +
                                flat.toString() +
                                "WHERE id = ${flat.id};")
                val api = Request.Builder()
                    .url(queryParams.build())
                    .get()
                    .build()
                // BLOCKING
                val response = client.newCall(api).await()
                val result = response.body()?.string() ?: "[]"
                Log.d("MYDEBUG", "Set Flat")
            }
        }
        catch (e: Exception){
            Log.d("MYDEBUG", e.toString())
        }
    }

    suspend fun putChess(houseNumber: Int) {
        val f = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString() + "/report.xlsx"
        )
        val client = OkHttpClient()
        val formBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file", f.name,
                RequestBody.create(MediaType.parse("text/plain"), f.readBytes())
            )
            .addFormDataPart("token", "qwerty")
            .addFormDataPart("id_house_in_db", houseNumber.toString())
            .build()
        val request = Request.Builder()
            .url("https://webhook.site/56c0d094-813b-4f60-a3d6-bdff1e39b0db")
            .post(formBody)
            .build()

        try {
            val response = client.newCall(request).await()
            Log.d("MYDEBUG", "DATA HAS BEEN SENT")
        } catch (e: Exception) {
            Log.d("MYDEBUG", "DATA HAS NOT BEEN SENT")
        }
    }
}