package com.sophieoc.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.*
import com.sophieoc.realestatemanager.model.Property
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface PropertyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(property: Property): Long

    @Update
    suspend fun update(property: Property): Int

    suspend fun upsert(property: Property): Long {
        val id: Long = insert(property)
        return if (id == -1L) {
            update(property).toLong()
        } else
            id
    }

    @Query("SELECT * FROM property ORDER BY timestamp DESC")
    fun getProperties(): Flow<List<Property>>

    @Query("SELECT * FROM property")
    fun getPropertiesWithCursor(): Cursor

    @Query("SELECT * FROM property WHERE user_id = :userId")
    fun getPropertiesWithCursorForUser(userId: String): Cursor

    @Query("SELECT * FROM property WHERE id = :id")
    fun getPropertyById(id: String): Flow<Property>

    @Query("DELETE FROM property WHERE id = :propertyId")
    fun deleteById(propertyId: String): Int

    @Query("DELETE FROM property")
    fun deleteAll(): Int

    @Query("SELECT price FROM property WHERE price = (SELECT MAX(price) FROM property)")
    fun getPriceOfPriciestProperty(): Flow<Int>

    @Query("SELECT surface FROM property WHERE surface = (SELECT MAX(surface) FROM property)")
    fun getSurfaceOfBiggestProperty(): Flow<Int>

    @Query("""SELECT * FROM property WHERE  
        CASE WHEN :propertyType IS NOT NULL THEN type LIKE '_' || :propertyType || '_' ELSE 1 END 
        AND CASE WHEN :nbrOfBed IS NOT NULL THEN number_of_bedrooms = :nbrOfBed ELSE 1 END
        AND CASE WHEN :nbrOfBath IS NOT NULL THEN number_of_bathrooms = :nbrOfBath ELSE 1 END
        AND CASE WHEN :nbrOfRooms IS NOT NULL THEN number_of_rooms = :nbrOfRooms ELSE 1 END
        AND CASE WHEN :propertyAvailability IS NOT NULL THEN availability LIKE '_'|| :propertyAvailability || '_' ELSE 1 END 
        AND CASE WHEN :dateOnMarket IS NOT NULL THEN date_on_market > :dateOnMarket ELSE 1 END
        AND CASE WHEN :dateSold IS NOT NULL THEN date_sold > :dateSold ELSE 1 END
        AND price > :priceMin AND price < :priceMax
        AND surface > :surfaceMin AND surface < :surfaceMax 
        AND CASE WHEN :area IS NOT NULL THEN address LIKE '%' || :area || '%' ELSE 1 END
        AND CASE WHEN :nbrOfPictures IS NOT NULL THEN (number_of_pictures >= :nbrOfPictures) ELSE 1 END""")
    fun getFilteredList(
            propertyType: String?, nbrOfBed: Int?, nbrOfBath: Int?, nbrOfRooms : Int?,
            propertyAvailability: String?, dateOnMarket: Date?, dateSold: Date?,
            priceMin: Int?, priceMax: Int?, surfaceMin: Int?, surfaceMax: Int?,
            nbrOfPictures: Int?, area: String?): Flow<List<Property>>
}