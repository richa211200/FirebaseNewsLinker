package com.richa.moengageasssignment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.richa.moengageasssignment.adapter.ArticleAdapter
import com.richa.moengageasssignment.model.Article
import com.richa.moengageasssignment.viewModel.ArticleViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var loader: ProgressBar
    private lateinit var spinnerSort: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        loader = findViewById(R.id.loader)
        spinnerSort = findViewById(R.id.spinner_sort)

        articleViewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        articleAdapter = ArticleAdapter(emptyList())
        recyclerView.adapter = articleAdapter

        ArrayAdapter.createFromResource(
            this,
            R.array.sorting_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerSort.adapter = adapter
        }

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle sorting based on selected option
                when (position) {
                    0 -> articleAdapter.sortByOldToNew()
                    1 -> articleAdapter.sortByNewToOld()
                }
                articleAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Observe changes in articles data
        articleViewModel.articles.observe(this, Observer { articles ->
            // Handling fetched articles
            articles?.let {
                displayArticles(it)
                articleAdapter.articles = it
                articleAdapter.notifyDataSetChanged()
                loader.visibility = View.GONE // Hide loader when data is fetched

            }
        })

        // Show loader on launch of the screen
        loader.visibility = View.VISIBLE

        // Fetching articles from the API
        articleViewModel.fetchArticles()
    }

    private fun displayArticles(articles: List<Article>) {
        // Iterate through the list of articles and display them in the UI
        for (article in articles) {
            println("Title: ${article.title}")
            println("Author: ${article.author}")
            println("Description: ${article.description}")
            println("URL: ${article.url}")
            println("Image URL: ${article.urlToImage}")
            println("Published At: ${article.publishedAt}")
            println("Content: ${article.content}")
            println()
        }
    }
}