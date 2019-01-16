package com.jamie1192.haveibeenpwned.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jamie1192.haveibeenpwned.database.models.EmailBreach
import io.reactivex.Maybe

/**
 * Created by jamie1192 on 15/1/19.
 */

@Dao
interface EmailBreachDao {

    @Query("SELECT * FROM emailBreach")
    fun getAllEmailBreaches() : LiveData<List<EmailBreach>>

    @Query("SELECT * FROM emailBreach where emailId = :email")
    fun getBreachesForEmail(email : String) : Maybe<List<EmailBreach>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmailBreach(emailBreach: EmailBreach)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmailBreachList(breachList : List<EmailBreach>)

    @Query("DELETE FROM emailBreach WHERE id = :id")
    fun deleteEmailBreachData(id : Int)

    @Query("DELETE FROM emailBreach")
    fun deleteAllEmailBreachData()
}