package com.androiddevs.mvvmnewsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androiddevs.mvvmnewsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class articleDatabase :RoomDatabase(){

abstract fun getArticleDao():ArticleDao // returns articleDao bs

companion object {
    // actual database
    //instance
    @Volatile // other threads immediatly updated when any changes
    private var instance: articleDatabase? = null
    private val LOCK = Any() // makes sure that there's only one instance of the database
    operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
        instance?:createDataBase(context).also{instance = it}
    }
    private fun createDataBase(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        articleDatabase::class.java,
        "article_db.db"
    ).build()

}
}