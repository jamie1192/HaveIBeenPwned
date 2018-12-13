package com.jamie1192.haveibeenpwned.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamie1192.haveibeenpwned.api.models.Site
import com.jamie1192.haveibeenpwned.database.models.Breach

/**
 * Created by jamie1192 on 27/11/18.
 */
@Dao
interface TableEntityDao {

    @Query("SELECT * FROM breaches")
    fun getAllTableEntities() : LiveData<List<Breach>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBreaches(list : List<Breach>)


}