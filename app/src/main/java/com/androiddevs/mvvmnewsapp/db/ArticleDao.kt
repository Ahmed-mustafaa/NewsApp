package com.androiddevs.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.androiddevs.mvvmnewsapp.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long // the id which inserted
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>> // not suspended cuz it returns a LIVEDATA UPDATES

@Delete
suspend fun deleteArticle(article: Article)

}