package com.sophieoc.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.sophieoc.realestatemanager.utils.PropertyAvailability
import com.sophieoc.realestatemanager.utils.PropertyType
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "property",
        foreignKeys = [ForeignKey(entity = EstateAgent::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("estateAgentId"))])
data class Property (
        @PrimaryKey(autoGenerate = true) val id: Int,
        @ColumnInfo(name = "type") val type: PropertyType,
        @ColumnInfo(name = "price") var price: String,
        @ColumnInfo(name = "surface") var surface: Int,
        @ColumnInfo(name = "number_of_rooms") var numberOfRooms: Int,
        @ColumnInfo(name = "number_of_bedrooms") var numberOfBedrooms: Int?,
        @ColumnInfo(name = "number_of_bathrooms") var numberOfBathrooms: Int,
        @ColumnInfo(name = "description") var description: String?,
        @ColumnInfo(name = "availability") val availability: PropertyAvailability,
        @ColumnInfo(name = "date_on_market") val dateOnMarket: Date,
        @ColumnInfo(name = "date_sold") val dateSold: Date?,
        var address: Address,
        var photos: ArrayList<Photo>,
        var pointOfInterests: ArrayList<PointOfInterest>,
        var estateAgentId: Int)