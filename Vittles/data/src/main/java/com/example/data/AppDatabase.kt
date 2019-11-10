package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Creates the ProductDao.
 *
 * @param context The application context
 * @return The ProductDao.
 */
fun createProductDaoImpl(context: Context): ProductDao {
    return AppDatabase.getDatabase(context).productDao()
}

/**
 * Room database singleton implementation. Used for CRUD in the database. Use the
 * getDatabase method to retrieve the singleton instance to select the needed dao.
 *
 * @author Jeroen Flietstra
 * @author Jan-Willem van Bremen
 * @author Sarah Lange
 */
@Database(entities = [ProductEntity::class, WasteReportEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * If instance is not created, creates a new database instance. Otherwise it will
         * return the existing instance.
         *
         * @param context Application context.
         * @return Singleton instance of database.
         */
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vittles_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
