package it.unipd.dei.music_application

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cashflow(
    val amount: Double,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
