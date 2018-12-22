package com.jamie1192.haveibeenpwned.database.dao

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


}