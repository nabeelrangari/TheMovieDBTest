package com.nabeel.themoviedbtest.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Genre(
    @SerializedName("id")
    @Expose
    var id: Int? = null,
    @SerializedName("name")
    @Expose
    var name: String? = null
) : Serializable {
    constructor() : this(1, "Thriller")
}