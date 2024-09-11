package org.unizd.rma.babic.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity()
data class State(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "stateId")
    val id: String,
    @ColumnInfo(name = "stateTitle")
    val title: String,
    val description: String,
    val date: Date,
    val surface : String,
    val imageUri: String,
    val country: String // Add the country field
)