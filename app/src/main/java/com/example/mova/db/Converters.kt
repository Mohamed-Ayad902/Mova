package com.example.mova.db

import androidx.room.TypeConverter
import com.example.mova.data.Genre
import com.example.mova.data.ProductionCompany
import com.example.mova.data.ProductionCountry
import com.example.mova.data.SpokenLanguage
import com.example.mova.data.tv.CreatedBy
import com.example.mova.data.tv.Network
import com.example.mova.data.tv.Season
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun toProductionCompanyList(data: String): List<ProductionCompany> {
        val listType = object : TypeToken<List<ProductionCompany>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toProductionCompanyString(productionCompany: List<ProductionCompany>): String {
        return GsonBuilder().create().toJson(productionCompany)
    }

//    @TypeConverter
//    fun toVideosList(data: String): List<Results> {
//        val listType = object : TypeToken<List<Results>>() {}.type
//        return GsonBuilder().create().fromJson(data, listType)
//    }
//
//    @TypeConverter
//    fun fromVideosString(videos: List<Videos>): String {
//        return GsonBuilder().create().toJson(videos)
//    }
//
//    @TypeConverter
//    fun toVideoList(data: String): List<Videos> {
//        val listType = object : TypeToken<List<Videos>>() {}.type
//        return GsonBuilder().create().fromJson(data, listType)
//    }
//
//    @TypeConverter
//    fun fromVideoString(videos: List<Results>): String {
//        return GsonBuilder().create().toJson(videos)
//    }

    @TypeConverter
    fun toGenreList(data: String): List<Genre> {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toGenreString(genre: List<Genre>): String {
        return GsonBuilder().create().toJson(genre)
    }

    @TypeConverter
    fun toProductionCountryList(data: String): List<ProductionCountry> {
        val listType = object : TypeToken<List<ProductionCountry>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toProductionCountryString(productionCountry: List<ProductionCountry>): String {
        return GsonBuilder().create().toJson(productionCountry)
    }

    @TypeConverter
    fun toSpokenLanguageList(data: String): List<SpokenLanguage> {
        val listType = object : TypeToken<List<SpokenLanguage>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toSpokenLanguageString(spokenLanguage: List<SpokenLanguage>): String {
        return GsonBuilder().create().toJson(spokenLanguage)
    }

    // typeConverters for tvShow

    @TypeConverter
    fun toCreatedByList(data: String): List<CreatedBy> {
        val listType = object : TypeToken<List<CreatedBy>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toCreatedByString(createdBy: List<CreatedBy>): String {
        return GsonBuilder().create().toJson(createdBy)
    }

    @TypeConverter
    fun toNetworkList(data: String): List<Network> {
        val listType = object : TypeToken<List<Network>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toNetworkString(network: List<Network>): String {
        return GsonBuilder().create().toJson(network)
    }

    @TypeConverter
    fun saveIntList(list: List<Int>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getIntList(list: String): List<Int> {
        return Gson().fromJson(
            list,
            object : TypeToken<List<Int>>() {}.type
        )
    }

    @TypeConverter
    fun saveStingList(list: List<String>): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun getStingList(list: String): List<String> {
        return Gson().fromJson(
            list,
            object : TypeToken<List<String>>() {}.type
        )
    }

    @TypeConverter
    fun toSeasonList(data: String): List<Season> {
        val listType = object : TypeToken<List<Season>>() {}.type
        return GsonBuilder().create().fromJson(data, listType)
    }

    @TypeConverter
    fun toSeasonString(season: List<Season>): String {
        return GsonBuilder().create().toJson(season)
    }


}