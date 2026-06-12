package com.lissu.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lissu.data.database.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

  @Query("SELECT * FROM reminders")
  fun getAllReminders(): Flow<List<ReminderEntity>>

  // Se retorna Long para obtener el id autogenerado por Room.
  // Se necesita obtener este id para programar las notificaciones.
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertReminder(reminder: ReminderEntity): Long

  @Delete
  suspend fun deleteReminder(reminder: ReminderEntity)

  @Update
  suspend fun updateReminder(reminder: ReminderEntity)
}