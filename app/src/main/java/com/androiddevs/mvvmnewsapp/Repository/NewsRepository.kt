package com.androiddevs.mvvmnewsapp.Repository

import com.androiddevs.mvvmnewsapp.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.db.articleDatabase
import com.androiddevs.mvvmnewsapp.models.Article

//get the data from our database / remote database(API)
class NewsRepository(
    val db: articleDatabase
) {
    suspend fun getBreakingNewsFromAPI(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchForNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(countryCode, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)
    fun getSavedNews() = db.getArticleDao().getAllArticles()
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}