package com.richa.moengageasssignment.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Button
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.richa.moengageasssignment.R
import com.richa.moengageasssignment.model.Article
import java.text.SimpleDateFormat
import java.util.Locale

class ArticleAdapter(public var articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = articles[position]
        holder.titleTextView.text = currentArticle.title
        holder.articleDate.text = currentArticle.publishedAt
        holder.authorTextView.text = currentArticle.author
        holder.descriptionTextView.text = currentArticle.description

//        Glide.with(holder.itemView)
//            .load(currentArticle.urlToImage)
//            .into(holder.newsThumbnail)

        // Showing loader while loading image
        holder.loader.visibility = View.VISIBLE

        Glide.with(holder.itemView)
            .load(currentArticle.urlToImage)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide loader if image loading failed
                    holder.loader.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    // Hide loader when image loading is successful
                    holder.loader.visibility = View.GONE
                    return false
                }
            })
            .into(holder.newsThumbnail)

        holder.viewNewsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.url))
            holder.itemView.context.startActivity(intent)
        }
    }

    // Method to sort articles by old to new
    fun sortByOldToNew() {
        articles = articles.sortedBy { article ->
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(article.publishedAt)?.time ?: 0
        }
    }

    // Method to sort articles by new to old
    fun sortByNewToOld() {
        articles = articles.sortedByDescending { article ->
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(article.publishedAt)?.time ?: 0
        }
    }

    override fun getItemCount() = articles.size

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsThumbnail: ImageView = itemView.findViewById(R.id.newsThumbnail)
        val articleDate: TextView = itemView.findViewById(R.id.articleDateTextView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val loader: ProgressBar = itemView.findViewById(R.id.item_loader)
        val viewNewsButton = itemView.findViewById<Button>(R.id.view_news_button)

    }
}
