package com.parkseryu.witness.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.parkseryu.witness.MainActivity
import com.parkseryu.witness.MainActivity.Companion.startDay
import com.parkseryu.witness.R
import com.parkseryu.witness.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewDataBinding: FragmentHomeBinding

    companion object {
        fun newInstance() = HomeFragment()
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

        homeViewModel.dbInitializerEvent.observe(viewLifecycleOwner, Observer {
            MainActivity.id = R.layout.dialog_start_day

            (requireActivity() as MainActivity).showDialog(
                activity = requireActivity(),
                context = requireContext(),
                isCancelVisible = true,
                okCallback = {
                    homeViewModel.startDay.value = startDay
                    homeViewModel.insertDay()
                },
                dismissCallback = {
                }
            )

        })
        subscribeUI()
        return homeViewDataBinding.root
    }



    private fun subscribeUI() {
        homeViewModel.makeUiEvent.observe(viewLifecycleOwner, Observer {
            tvFirst.text = homeViewModel.selectDay[0].topPhrase
            val text = "${homeViewModel.diffDays}일"
            tvMainDay.text = text
            tvSecond.text = homeViewModel.selectDay[0].bottomPhrase

            val adapter = RecyclerViewAdapter(homeViewModel)
            homeViewDataBinding.recyclerViewUpComing.adapter = adapter
            adapter.items = homeViewModel.anniversaryDay
            homeViewDataBinding.recyclerViewUpComing.addItemDecoration(DividerItemDecoration(context,1))
            adapter.notifyDataSetChanged()
        })
    }
}