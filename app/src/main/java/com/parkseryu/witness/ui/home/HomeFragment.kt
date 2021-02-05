package com.parkseryu.witness.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.textview.MaterialTextView
import com.parkseryu.witness.R
import com.parkseryu.witness.`object`.AnniversaryObject
import com.parkseryu.witness.`object`.AnniversaryObject.simpleDateFormat
import com.parkseryu.witness.`object`.AnniversaryObject.today
import com.parkseryu.witness.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.dialog_start_day.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewDataBinding: FragmentHomeBinding
    private lateinit var dialogView: View
    private var alertDialog: AlertDialog? = null
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private var isFabOpen = false

    private var id: Int? = 0

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

        fabOpen = AnimationUtils.loadAnimation(context, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(context, R.anim.fab_close)

        homeViewModel.dbInitializerEvent.observe(viewLifecycleOwner, Observer {
            id = R.layout.dialog_start_day

            showDialog(
                activity = requireActivity(),
                context = requireContext(),
                isCancelVisible = true,
                isCancelable = false,
                okCallback = {
                    val datePicker = dialogView.datePickerStartDay
                    val calendar = Calendar.getInstance()
                    val year = datePicker.year
                    val month = datePicker.month
                    val day = datePicker.dayOfMonth
                    calendar.set(year, month, day)
                    val startDay = simpleDateFormat.format(calendar.timeInMillis)
                    if (startDay > today) {
                        Toast.makeText(context, "미래의 날짜는 설정할 수 없습니다.", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        alertDialog?.dismiss()
                    }
                    homeViewModel.startDay = startDay
                    homeViewModel.insertDay()
                    alertDialog?.dismiss()
                },
                dismissCallback = {
                    Toast.makeText(context, "날짜를 설정해 주세요!", Toast.LENGTH_LONG)
                        .show()
                }
            )
        })

        homeViewModel.fabClickEvent.observe(viewLifecycleOwner, Observer {
            anim()
        })

        homeViewModel.fabAddAniClickEvent.observe(viewLifecycleOwner, Observer {

            showDialog(
                activity = requireActivity(),
                context = requireContext(),
                isCancelVisible = true,
                isCancelable = true,
                okCallback = {

                },
                dismissCallback = {

                }
            )
            anim()
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
            homeViewDataBinding.recyclerViewUpComing.addItemDecoration(
                DividerItemDecoration(
                    context,
                    1
                )
            )
            adapter.notifyDataSetChanged()
        })
    }

    private fun anim() {
        if (isFabOpen) {
            Log.d("test", "isFabOpen")
            fabAddAni.visibility = View.INVISIBLE
            //outer_layout.setBackgroundColor(getColor(R.color.transparent))
            prompt_fabAddAni.visibility = View.INVISIBLE
            prompt_fabAddAni.startAnimation(fabClose)
            fabAddAni.startAnimation(fabClose)
            fabAddAni.isClickable = false
            isFabOpen = false
        } else {
            Log.d("test", "isFabClose")
            fabAddAni.visibility = View.VISIBLE
            prompt_fabAddAni.visibility = View.VISIBLE
            //outer_layout.setBackgroundColor(getColor(R.color.test))
            prompt_fabAddAni.startAnimation(fabOpen)
            fabAddAni.startAnimation(fabOpen)
            fabAddAni.isClickable = true
            isFabOpen = true
        }
    }

    private fun showDialog(
        activity: Activity,
        context: Context,
        okButtonTitle: String = "",
        cancelButtonTitle: String = "",
        isCancelable : Boolean,
        isCancelVisible: Boolean,
        okCallback: () -> Unit,
        dismissCallback: () -> Unit
    ) {
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(context)
        }
        dialogView = activity.layoutInflater.inflate(id!!, null)

        val okButton = dialogView.findViewById<MaterialTextView>(R.id.btn_ok)
        if (okButtonTitle != "")
            okButton.text = okButtonTitle
        okButton.setOnClickListener {
            okCallback.invoke()
        }
        val cancelButton = dialogView.findViewById<MaterialTextView>(R.id.btn_cancel)
        cancelButton.visibility = if (isCancelVisible) View.VISIBLE else View.GONE
        if (cancelButtonTitle != "")
            cancelButton.text = cancelButtonTitle
        cancelButton.setOnClickListener {
            dismissCallback.invoke()
        }
        builder.setView(dialogView)
        if (alertDialog == null) {
            alertDialog = builder.create()

        }

        alertDialog?.setOnDismissListener {
            dismissCallback.invoke()
            alertDialog = null
        }
        alertDialog?.setCancelable(isCancelable)
        alertDialog?.show()

    }

}

