package com.nabeel.themoviedbtest.data.database.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nabeel.themoviedbtest.model.Genre

class ResultTypeConverter {
    val gson = Gson()

    @TypeConverter
    fun fromStringToRoom(value: String): ArrayList<Genre>? {
        val type = object : TypeToken<ArrayList<Genre>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromRoomToString(value: ArrayList<Genre>): String {
        return gson.toJson(value)
    }

}