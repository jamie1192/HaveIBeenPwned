package com.jamie1192.haveibeenpwned.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jamie1192.haveibeenpwned.database.models.UserEmail
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by jamie1192 on 7/1/19.
 */

@Dao
interface UserDao {

    @Query("SELECT * FROM userEmail")
    fun getAllAccounts() : LiveData<List<UserEmail>>

    @Query("SELECT * FROM userEmail")
    fun getAllAccountsObsv() : Observable<List<UserEmail>>

    //TODO add WHERE notifyNewBreaches = 1
    @Query("SELECT * FROM userEmail")
    fun getAllAccountsSingle() : Single<List<UserEmail>>

    @Query("UPDATE userEmail SET breachCount = :breaches WHERE email = :email")
    fun updateSingleEmail(breaches : Int, email : String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmailList(list: List<UserEmail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmail(email : UserEmail)

    @Query("DELETE FROM userEmail WHERE email = :email")
    fun deleteEmail(email : String)

    @Query("DELETE FROM userEmail")
    fun deleteAllEmails()

    @Update
    fun updateEmailDetails(email: UserEmail)

    @Query("SELECT * FROM userEmail ORDER BY breachCount ASC")
    fun getEmailsbyBreachAsc() : Maybe<List<UserEmail>>

    @Query("SELECT * FROM userEmail ORDER BY breachCount DESC")
    fun getEmailsbyBreachDesc() : Maybe<List<UserEmail>>

}