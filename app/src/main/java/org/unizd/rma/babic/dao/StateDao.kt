package org.unizd.rma.babic.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.unizd.rma.babic.models.State


@Dao
interface StateDao {


   @Query("SELECT * FROM State ORDER BY date DESC")
    fun getStateList() : Flow<List<State>>



//    @Query("""SELECT * FROM State ORDER BY
//        CASE WHEN :isAsc = 1 THEN stateTitle END ASC,
//        CASE WHEN :isAsc = 0 THEN stateTitle END DESC""")
//    fun getStateListSortByStateTitle(isAsc: Boolean) : Flow<List<State>>
//
//    @Query("""SELECT * FROM State ORDER BY
//        CASE WHEN :isAsc = 1 THEN date END ASC,
//        CASE WHEN :isAsc = 0 THEN date END DESC""")
//    fun getStateListSortByStateDate(isAsc: Boolean) : Flow<List<State>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertState(state: State): Long

//
    // First way
    @Delete
    suspend fun deleteState(state: State) : Int
//
//
    // Second Way
    @Query("DELETE FROM State WHERE stateId == :stateId")
    suspend fun deleteStateUsingId(stateId: String) : Int
//
//
    @Update
    suspend fun updateState(state: State): Int
//
//
    @Query("UPDATE State SET stateTitle=:title, description = :description  , surface = :surface, imageUri = :imageUri   WHERE stateId = :stateId")
    suspend fun updateStatePaticularField(stateId:String,title:String,description:String, surface: String, imageUri: String): Int

//
//    @Query("SELECT * FROM State WHERE stateTitle LIKE :query ORDER BY date DESC")
//    fun searchStateList(query: String) : Flow<List<State>>
}