package com.parkseryu.witness.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.parkseryu.witness.R
import com.parkseryu.witness.dao.MeetDayDao
import com.parkseryu.witness.databinding.FragmentHomeBinding
import com.parkseryu.witness.db.AppDatabase
import com.parkseryu.witness.repository.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewDataBinding: FragmentHomeBinding
    private val repository : MeetDayDao by lazy {  AppDatabase.getInstance(requireContext()).meetDayDao() }
    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("test", "${repository.getAll()}")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewDataBinding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        ).apply {
            this.viewModel = homeViewModel
            this.lifecycleOwner = this@HomeFragment
        }






        return homeViewDataBinding.root
    }
}