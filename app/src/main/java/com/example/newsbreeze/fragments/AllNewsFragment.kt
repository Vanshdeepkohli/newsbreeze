package com.example.newsbreeze.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsbreeze.MainActivity
import com.example.newsbreeze.R
import com.example.newsbreeze.adapter.NewsAdapter
import com.example.newsbreeze.databinding.FragmentAllNewsBinding
import com.example.newsbreeze.viewModel.NewsViewModel
import com.example.newsbreeze.viewModel.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class AllNewsFragment : Fragment() {

    private var _binding: FragmentAllNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllNewsBinding.inflate(layoutInflater, container, false)

        viewModel = (activity as MainActivity).viewModel
        newsAdapter = NewsAdapter()

        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = newsAdapter
        }


        showBreakingNews()


        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                showProgressBar()
                search?.let {
                    val `in` =
                        activity!!.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    `in`.hideSoftInputFromWindow(binding.searchView.getWindowToken(), 0)
                    viewModel.searchNews(it)
                    showSearchedNews()
                }
                return true
            }

            override fun onQueryTextChange(search: String?): Boolean {
                if (search.isNullOrEmpty()) {
                    showBreakingNews()
                }
                return true
            }
        })

        binding.saveFab.setOnClickListener {
            findNavController().navigate(R.id.action_allNewsFragment_to_savedNewsFragment)
        }

        newsAdapter.setOnReadMoreClickListener {
            val bundle = Bundle().apply {
                putString("article", Gson().toJson(it))
            }
            findNavController().navigate(R.id.action_allNewsFragment_to_fullNewsFragment2, bundle)
        }

        newsAdapter.setOnSaveClickListener { article ->
            viewModel.insertArticle(article)
            Snackbar.make(requireView(), "Article Saved", Snackbar.LENGTH_SHORT).show()
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

        return binding.root
    }


    private fun showBreakingNews() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "an error occured : $it",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_allNewsFragment_to_savedNewsFragment)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showSearchedNews() {
        viewModel.SearchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "an error occured : $it",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.pgBar.apply {
            isIndeterminate = false
            visibility = View.INVISIBLE
        }
    }

    private fun showProgressBar() {
        binding.pgBar.apply {
            isIndeterminate = true
            visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}