package com.richa.moengageasssignment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.richa.moengageasssignment.model.Article
import com.richa.moengageasssignment.view.ArticleRepo

class ArticleViewModel : ViewModel() {

    private val articleRepository = ArticleRepo()
    private val _articles = MutableLiveData<List<Article>>()

    val articles: LiveData<List<Article>>
        get() = _articles

    fun fetchArticles() {
        articleRepository.getArticles { articles ->
            _articles.postValue(articles)
        }
    }
}