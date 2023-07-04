package com.example.newsbreeze.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsbreeze.MainActivity
import com.example.newsbreeze.R
import com.example.newsbreeze.adapter.NewsAdapter
import com.example.newsbreeze.databinding.FragmentSavedNewsBinding
import com.example.newsbreeze.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class SavedNewsFragment : Fragment() {
    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSavedNewsBinding.inflate(layoutInflater, container, false)

        viewModel = (activity as MainActivity).viewModel
        newsAdapter = NewsAdapter()

        binding.rvSavedNews.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }

        newsAdapter.setOnReadMoreClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_allNewsFragment_to_fullNewsFragment2, bundle)
        }


        viewModel.getAllSavedArticles().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })

        binding.deleteAll.setOnClickListener {
            newsAdapter.differ.currentList.forEach { article ->
                viewModel.deleteArticle(article)
            }
        }


        newsAdapter.setOnDeleteClickListener { article ->
            viewModel.deleteArticle(article)
            Snackbar.make(requireView(), "Successfully deleted article", Snackbar.LENGTH_LONG)
                .apply {
                    setAction("Undo") {
                        viewModel.insertArticle(article)
                    }
                    show()
                }
        }

        newsAdapter.setOnReadMoreClickListener {
            val bundle = Bundle().apply {
                putString("article", Gson().toJson(it))
            }
            findNavController().navigate(R.id.action_savedNewsFragment_to_fullNewsFragment, bundle)
        }

        binding.back.setOnClickListener {
            findNavController().navigate(R.id.action_savedNewsFragment_to_allNewsFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}