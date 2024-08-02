package `in`.ram.gita.datasource.api.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlin.concurrent.Volatile

@Database(entities = [SavedChapters::class , SavedVerses::class], version = 2, exportSchema = false)
@TypeConverters(AppTypeConverters::class)

abstract class AppDataBase : RoomDatabase() {

    abstract fun savedChaptersDao() : SavedChaptersDao
    abstract fun savedVersesDao() : SavedVersesDao


    companion object{
        @Volatile
        var INSTANCE : AppDataBase?=null

        fun getDatabaseInstance(context: Context) : AppDataBase? {
            val tempInstance = INSTANCE
            if (INSTANCE != null) return tempInstance
            synchronized(this){
                val roomDb = Room.databaseBuilder(context,AppDataBase::class.java,"AppDataBase").fallbackToDestructiveMigration().build()

                INSTANCE = roomDb
                return roomDb
            }
        }
    }
}