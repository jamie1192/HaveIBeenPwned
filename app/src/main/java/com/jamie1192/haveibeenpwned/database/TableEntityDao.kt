package com.jamie1192.haveibeenpwned.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

/**
 * Created by jamie1192 on 27/11/18.
 */
@Dao
interface TableEntityDao {

    @Query("SELECT * FROM table_entity")
    fun getAllTableEntities() : LiveData<List<TableEntity>>


}