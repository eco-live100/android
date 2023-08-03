package com.app.ecolive.taximodule.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.app.ecolive.R
import com.app.ecolive.databinding.ActivityNotifactionBinding
import com.app.ecolive.databinding.FragmentSchudleBinding
import java.util.*


class FragmentSchudle : Fragment() {
    lateinit var binding: FragmentSchudleBinding
    val myCalendar: Calendar = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSchudleBinding.inflate(inflater, container, false)
        binding.toolbar.toolbarTitle.text = "Schedule rides for a\nweek/month"
        binding.toolbar.ivBack.visibility =View.INVISIBLE
        val date =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, day)

            }

        val myTimeListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (view.isShown) {
                    myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    myCalendar.set(Calendar.MINUTE, minute)
                }
            }
        binding.calender1.setOnClickListener {
            var dialog = DatePickerDialog(
                requireContext(),
                R.style.my_dialog_theme,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            dialog.show()
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black))
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))

        }
        binding.calender2.setOnClickListener {
            var dialog= DatePickerDialog(
                requireContext(), R.style.my_dialog_theme,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            )
            dialog.show()
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black))
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))

        }
        binding.timePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                R.style.my_dialog_theme,
                myTimeListener,
                myCalendar[Calendar.HOUR],
                myCalendar[Calendar.MINUTE],
                false
            )

            timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
            timePickerDialog.show()
            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))
            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black))



        }

        binding.timePicker2.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                R.style.my_dialog_theme,
                myTimeListener,
                myCalendar[Calendar.HOUR],
                myCalendar[Calendar.MINUTE],
                false
            )

            timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.white)
            timePickerDialog.show()
            timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))
            timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black))



        }
        return binding.root

    }

}