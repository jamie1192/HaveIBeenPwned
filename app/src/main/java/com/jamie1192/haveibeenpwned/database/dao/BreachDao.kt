package com.jamie1192.haveibeenpwned.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamie1192.haveibeenpwned.database.models.Breach
import io.reactivex.Maybe

/**
 * Created by jamie1192 on 27/11/18.
 */

@Dao
interface BreachDao {

    @Query("SELECT * FROM breaches")
    fun getAllTableEntities() : Maybe<List<Breach>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBreaches(list : List<Breach>)

    @Query("SELECT * FROM breaches WHERE name = :name")
    fun getSiteByName(name : String) : LiveData<Breach>

    @Query("SELECT * FROM breaches ORDER BY breachDate ASC")
    fun getBreachesByDateAsc(): DataSource.Factory<Int, Breach>

    @Query("SELECT * FROM breaches ORDER BY breachDate DESC")
    fun getBreachesByDateDesc(): DataSource.Factory<Int, Breach>

    @Query("SELECT * FROM breaches ORDER BY name ASC")
    fun getBreachesByNameAsc(): DataSource.Factory<Int, Breach>

    @Query("SELECT * FROM breaches ORDER BY name DESC")
    fun getBreachesByNameDesc(): DataSource.Factory<Int, Breach>

    @Query("SELECT * FROM breaches ORDER BY pwnCount ASC")
    fun getBreachesByPwnAsc(): DataSource.Factory<Int, Breach>

    @Query("SELECT * FROM breaches ORDER BY pwnCount DESC")
    fun getBreachesByPwnDesc(): DataSource.Factory<Int, Breach>

}