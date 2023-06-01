package ru.mrmarvel.camoletapp.data.sources

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import ru.gildor.coroutines.okhttp.await
import ru.mrmarvel.camoletapp.data.models.Project

class ProjectSource {
    suspend fun getAll(): List<Project> {
        val client = OkHttpClient()
        val queryParams = HttpUrl.parse("http://u1988986.isp.regruhosting.ru/rest")!!.newBuilder()
            .addQueryParameter("sql", "SELECT * FROM projects;")
        val api = Request.Builder()
            .url(queryParams.build())
            .get()
            .build()
        // BLOCKING
        val response = client.newCall(api).await()
        val result = response.body()?.string() ?: "[]"
        Log.d("MYDEBUG", "Send data to server was: $result")
        val jsonArray = JSONArray(result)
        val ormArray = mutableListOf<Project>()
        for (i in 0 until jsonArray.length()) {
            val x = GsonBuilder().create().fromJson(jsonArray.get(i).toString(), Project::class.java)
            ormArray.add(x)
        }
        return ormArray
    }
}