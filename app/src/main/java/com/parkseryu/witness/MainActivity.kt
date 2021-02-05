package com.parkseryu.witness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(){

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0F


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}



/*   fun showDialog(
       activity: Activity,
       context: Context,
       okButtonTitle: String = "",
       cancelButtonTitle: String = "",
       isCancelVisible: Boolean,
       okCallback: () -> Unit,
       dismissCallback: () -> Unit
   ) {
       val builder: AlertDialog.Builder = activity.let {
           AlertDialog.Builder(context)
       }
       val dialogView = activity.layoutInflater.inflate(id, null)
       val okButton = dialogView.findViewById<MaterialTextView>(R.id.btn_ok)
       if (okButtonTitle != "")
           okButton.text = okButtonTitle
       okButton.setOnClickListener {
           if (id == R.layout.dialog_start_day) {
               val datePicker = dialogView.datePickerStartDay
               val calendar = Calendar.getInstance()
               val year = datePicker.year
               val month = datePicker.month
               val day = datePicker.dayOfMonth
               calendar.set(year, month, day)
               startDay = simpleDateFormat.format(calendar.timeInMillis)
               if (startDay > today) {
                   Toast.makeText(applicationContext, "미래의 날짜는 설정할 수 없습니다.", Toast.LENGTH_LONG)
                       .show()
               } else {
                   okCallback.invoke()
                   alertDialog?.dismiss()
               }
           } else {
               okCallback.invoke()
               alertDialog?.dismiss()
           }
       }
       val cancelButton = dialogView.findViewById<MaterialTextView>(R.id.btn_cancel)
       cancelButton.visibility = if (isCancelVisible) View.VISIBLE else View.GONE
       if (cancelButtonTitle != "")
           cancelButton.text = cancelButtonTitle
       cancelButton.setOnClickListener {
           alertDialog?.dismiss()
       }
       builder.setView(dialogView)
       if (alertDialog == null) {
           alertDialog = builder.create()
       }

       alertDialog?.setOnDismissListener {
           dismissCallback.invoke()
           alertDialog = null
       }
       alertDialog?.show()
   }*/
/*
    companion object {
        var id: Int = 0
        var startDay = ""
        private val now = System.currentTimeMillis()
        private val mDate = Date(now)
        private val simpleDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        val today: String = simpleDateFormat.format(mDate)
    }
*/
/*private var alertDialog: android.app.AlertDialog? = null*/



