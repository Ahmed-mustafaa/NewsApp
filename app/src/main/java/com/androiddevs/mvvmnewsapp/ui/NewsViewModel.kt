package com.androiddevs.mvvmnewsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.Repository.NewsRepository
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.models.NewsResponse
import com.androiddevs.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {


    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null // to  detect if it is the first response
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    val searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading()) // el AWl el loading
        val resource = newsRepository.getBreakingNewsFromAPI(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(resource))
    }

    fun searchNews(countryCode: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val resource = newsRepository.searchForNews(countryCode, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(resource))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = responseResult
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = responseResult.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(breakingNewsResponse ?: responseResult)
            }

        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { responseResult ->
                return Resource.Success(responseResult)
            }

        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

}

