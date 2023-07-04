package com.example.newsbreeze.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.newsbreeze.R
import com.example.newsbreeze.data.Article
import com.example.newsbreeze.databinding.FragmentFullNewsBinding
import com.example.newsbreeze.databinding.FragmentWebViewBinding
import com.google.gson.Gson


class WebViewFragment : Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)


        val json = arguments?.getString("article")
        val article = Gson().fromJson(json, Article::class.java)

        article?.let {
            binding.webViewContainer.apply {
                webViewClient = WebViewClient()
                loadUrl(article.url)
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}