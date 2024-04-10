package com.richa.moengageasssignment.view

import android.util.Log
import com.richa.moengageasssignment.model.Article
import com.richa.moengageasssignment.model.Source
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ArticleRepo {

    fun getArticles(onResult: (List<Article>?) -> Unit) {
        val url = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")

        Thread {
            var connection: HttpURLConnection? = null
            try {
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    bufferedReader.close()
                    val jsonResponse = JSONObject(response.toString())
                    val articlesJsonArray = jsonResponse.getJSONArray("articles")
                    val articlesList = mutableListOf<Article>()

                    for (i in 0 until articlesJsonArray.length()) {
                        val articleJson = articlesJsonArray.getJSONObject(i)
                        val sourceJson = articleJson.getJSONObject("source")
                        val source = Source(sourceJson.getString("id"), sourceJson.getString("name"))
                        val article = Article(
                            source,
                            articleJson.getString("author"),
                            articleJson.getString("title"),
                            articleJson.getString("description"),
                            articleJson.getString("url"),
                            articleJson.getString("urlToImage"),
                            articleJson.getString("publishedAt"),
                            articleJson.getString("content")
                        )
                        articlesList.add(article)
                    }
                    onResult(articlesList)
                } else {
                    Log.e("ArticleRepository", "HTTP error code: $responseCode")
                    onResult(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            } finally {
                connection?.disconnect()
            }
        }.start()
    }
}



