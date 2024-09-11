package org.unizd.rma.babic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.unizd.rma.babic.dao.StateDao
import org.unizd.rma.babic.models.State


@Database(
    entities = [State::class],
    version = 5,
)
@TypeConverters(org.unizd.rma.babic.converter.TypeConverter::class)
abstract class StateDatabase : RoomDatabase() {

    abstract val stateDao : StateDao

    companion object {

        private val migration_1_2 = object : Migration(4,5){
            override fun migrate(database: SupportSQLiteDatabase) {
               // database.execSQL("ALTER TABLE State ADD COLUMN imageData BLOB")
                database.execSQL("ALTER TABLE State ADD COLUMN country TEXT NOT NULL DEFAULT ''")

            }


        }


        @Volatile
        private var INSTANCE: StateDatabase? = null
        fun getInstance(context: Context): StateDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StateDatabase::class.java,
                    "state_db"

                ).addMigrations(migration_1_2)
                    .build().also {
                    INSTANCE = it
                }
            }

        }
    }

}