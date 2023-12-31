package com.example.githubuserapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.data.User
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.databinding.FragmentFollowingBinding
import com.example.githubuserapp.ui.activity.DetailUserActivity
import com.example.githubuserapp.ui.adapter.FollowingAdapter
import com.example.githubuserapp.ui.viewmodel.FollowingViewModel

class FollowingFragment : Fragment() {

    private var listData: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    private lateinit var binding: FragmentFollowingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowingAdapter(listData)
        followingViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        val dataUser = requireActivity().intent.getParcelableExtra<User>(EXTRA_USER)!!
        config()

        followingViewModel.getDataGit(
            requireActivity().applicationContext,
            dataUser.username.toString()
        )
        if (dataUser.following == "0") {
            showLoading(false)
            errorMessage(true)
        } else {
            showLoading(true)
        }

        followingViewModel.getListFollowing().observe(viewLifecycleOwner) { listFollowing ->
            if (listFollowing != null) {
                adapter.setData(listFollowing)
                showLoading(false)
            }
        }
    }

    private fun config() {
        binding.recyclerViewFollowing.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewFollowing.setHasFixedSize(true)
        binding.recyclerViewFollowing.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun errorMessage(state: Boolean) {
        if (state) {
            binding.cvErrorMessage.visibility = View.VISIBLE
        } else {
            binding.cvErrorMessage.visibility = View.INVISIBLE
        }
    }


    companion object {
        val TAG = FollowingFragment::class.java.simpleName
        const val EXTRA_USER = "extra_user"

    }

}