package com.parkseryu.witness.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.dialog_add_anniversary.*
import kotlinx.android.synthetic.main.dialog_add_anniversary.view.*
import kotlinx.android.synthetic.main.dialog_start_day.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewDataBinding: FragmentHomeBinding
    private lateinit var dialogView: View
    private var alertDialog: AlertDialog? = null
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private var isFabOpen = false
    private lateinit var datePicker: DatePicker
    private lateinit var calendar: Calendar
    private var year by Delegates.notNull<Int>()
    private var month by Delegates.notNull<Int>()
    private var day by Delegates.notNull<Int>()
    private lateinit var startDay: String

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
                    datePicker = dialogView.datePickerStartDay
                    calendar = Calendar.getInstance()
                    year = datePicker.year
                    month = datePicker.month
                    day = datePicker.dayOfMonth
                    calendar.set(year, month, day)
                    startDay = simpleDateFormat.format(calendar.timeInMillis)
                    if (startDay > today) {
                        Toast.makeText(context, "미래의 날짜는 설정할 수 없습니다.", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        homeViewModel.startDay = startDay
                        homeViewModel.insertDay()
                        alertDialog?.dismiss()
                    }
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
            id = R.layout.dialog_add_anniversary
            showDialog(
                activity = requireActivity(),
                context = requireContext(),
                isCancelVisible = true,
                isCancelable = true,
                okCallback = {
                    val aniName = dialogView.edtNewAnniv.text
                    if (aniName.isNotBlank()) {
                        datePicker = dialogView.datePickerAddAniv
                        calendar = Calendar.getInstance()
                        year = datePicker.year
                        month = datePicker.month
                        day = datePicker.dayOfMonth
                        calendar.set(year, month, day)
                        startDay = simpleDateFormat.format(calendar.timeInMillis)
                        homeViewModel.tenYearsFlag = dialogView.cbRepeatYear.isChecked
                        homeViewModel.userAddAnniversaryName = aniName.toString()
                        homeViewModel.userAddAnniversaryDay = startDay
                        homeViewModel.insertAnniversaryDay()
                        alertDialog?.dismiss()
                    } else {
                        Toast.makeText(context, "기념일 명을 입력해주세요.", Toast.LENGTH_LONG).show()
                    }
                },
                dismissCallback = {
                    alertDialog?.dismiss()
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
            for (i in 0..homeViewModel.anniversaryDay.lastIndex) {
                if (homeViewModel.anniversaryDay[i].leftDay.substring(1, 2) == "-") {
                    homeViewDataBinding.recyclerViewUpComing.scrollToPosition(i)
                    break
                }
            }
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
            requireActivity().window.statusBarColor = getColor(requireContext(), R.color.colorBasic)
            home_layout.setBackgroundColor(getColor(requireContext(), R.color.colorBasic))
            requireActivity().toolbar.background = ResourcesCompat.getDrawable(resources, R.drawable.toolbar_border, null)
            prompt_fabAddAni.visibility = View.INVISIBLE
            prompt_fabAddAni.startAnimation(fabClose)
            fabAddAni.startAnimation(fabClose)
            fabAddAni.isClickable = false
            isFabOpen = false
        } else {
            Log.d("test", "isFabClose")
            fabAddAni.visibility = View.VISIBLE
            prompt_fabAddAni.visibility = View.VISIBLE
            home_layout.setBackgroundColor(getColor(requireContext(), R.color.test))
            requireActivity().toolbar.setBackgroundColor(getColor(requireContext(), R.color.test))
            requireActivity().window.statusBarColor = getColor(requireContext(), R.color.black)
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
        isCancelable: Boolean,
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
            // dismissCallback.invoke()
            alertDialog = null
        }
        alertDialog?.setCancelable(isCancelable)
        alertDialog?.show()

    }

}

