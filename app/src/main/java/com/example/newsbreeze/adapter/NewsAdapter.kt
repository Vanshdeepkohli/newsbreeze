package com.example.newsbreeze.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsbreeze.data.Article
import com.example.newsbreeze.databinding.NewsItemBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MyAdapter>() {
    inner class MyAdapter(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter {
        return MyAdapter(
            NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: MyAdapter, position: Int) {
        val article = differ.currentList[position]

        holder.binding.apply {
            newsHeading.text = article.title
            newsContent.text = article.description
            newsDate.text = article.publishedAt

            Glide.with(holder.itemView).load(article.urlToImage).into(newsImage)
            if (article.isSaved == 1) {
                articleSaved.visibility = View.VISIBLE
                articleUnSaved.visibility = View.INVISIBLE

                save.visibility = View.INVISIBLE
                delete.visibility = View.VISIBLE
            }
            if (article.isSaved == 0) {
                articleSaved.visibility = View.INVISIBLE
                articleUnSaved.visibility = View.VISIBLE

                save.visibility = View.VISIBLE
                delete.visibility = View.INVISIBLE
            }
        }
        holder.binding.readMore.setOnClickListener {
            onReadMoreClickListener?.let {
                it(article)
            }
        }

        holder.binding.save.setOnClickListener {
            onSaveClickListener?.let {
                it(article)
            }
        }

        holder.binding.delete.setOnClickListener {
            onDeleteClickListener?.let {
                it(article)
            }
        }
    }

    private var onReadMoreClickListener: ((Article) -> Unit)? = null
    private var onSaveClickListener: ((Article) -> Unit)? = null
    private var onDeleteClickListener: ((Article) -> Unit)? = null

    fun setOnReadMoreClickListener(listener: (Article) -> Unit) {
        onReadMoreClickListener = listener
    }

    fun setOnSaveClickListener(listener: (Article) -> Unit) {
        onSaveClickListener = listener
    }

    fun setOnDeleteClickListener(listener: (Article) -> Unit) {
        onDeleteClickListener = listener
    }

}