package com.example.newsbreeze.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsbreeze.MainActivity
import com.example.newsbreeze.R
import com.example.newsbreeze.adapter.NewsAdapter
import com.example.newsbreeze.data.Article
import com.example.newsbreeze.databinding.FragmentFullNewsBinding
import com.example.newsbreeze.databinding.FragmentSavedNewsBinding
import com.example.newsbreeze.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class FullNewsFragment : Fragment() {
    private var _binding: FragmentFullNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentFullNewsBinding.inflate(layoutInflater, container, false)

        newsAdapter = NewsAdapter()

        val json = arguments?.getString("article")
        val article = Gson().fromJson(json, Article::class.java)

        article?.let {
            binding.apply {
                newsTitle.text = it.title
                newsContent.text = it.content
                newsDate.text = it.publishedAt
                Glide.with(requireContext()).load(it.urlToImage).into(newsImage)
                authorName.text = it.author
                authorId.text = it.source.name
            }
        }


        binding.openWebView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("article", Gson().toJson(article))
            }
            findNavController().navigate(R.id.action_fullNewsFragment_to_webViewFragment, bundle)
        }


        viewModel = (activity as MainActivity).viewModel


        binding.saveBtn.setOnClickListener {
            viewModel.insertArticle(article)
            Snackbar.make(requireView(), "Article Saved", Snackbar.LENGTH_SHORT).show()
        }

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_fullNewsFragment_to_allNewsFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}