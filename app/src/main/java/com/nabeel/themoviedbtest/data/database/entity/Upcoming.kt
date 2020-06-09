package com.nabeel.themoviedbtest.data.database.entity

import androidx.room.*
import com.nabeel.themoviedbtest.model.Genre
import com.nabeel.themoviedbtest.model.SpokenLanguage

@Entity(tableName = "upcoming", indices = [Index(value = ["title"], unique = true)])
data class Upcoming(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    val rank: Int,
    val title: String,
    val poster: String? = null,
    var voteCount: Int? = null,
    var popularity: Double? = null,
    var backdropPath: String? = null,
    var voteAverage: Double? = null,
    var releaseDate: String? = null,
    var budget: Int? = null,
    var revenue: Int? = null,
    var runtime: Int? = null,
    var tagline: String? = null,
    var overview: String? = null,
    @Embedded
    var genres: ArrayList<Genre>? = null,
    @Embedded
    var spokenLanguages: ArrayList<SpokenLanguage>? = null,
    var category: String
)