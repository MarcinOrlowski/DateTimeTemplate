package com.marcinorlowski.datetimetemplate.demo

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.marcinorlowski.datetimetemplate.DateTimeTemplate
import com.marcinorlowski.datetimetemplate.demo.databinding.ActivityMainBinding
import java.util.*

/** ******************************************************************************
 *
 * DateTimeTemplate
 * Flexible date/time formatting library with placeholders support.
 *
 * @author Marcin Orlowski <mail (#) marcinOrlowski (.) com>
 * @copyright 2013-2022 Marcin Orlowski
 * @license http://www.opensource.org/licenses/mit-license.php MIT
 * @link https://github.com/MarcinOrlowski/DateTimeTemplate
 *
 * *************************************************************************** **/

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mCalendar.time = Date()
        mHour = mCalendar[Calendar.HOUR_OF_DAY]
        mMinute = mCalendar[Calendar.MINUTE]
        mYear = mCalendar[Calendar.YEAR]
        mMonth = mCalendar[Calendar.MONTH]
        mDay = mCalendar[Calendar.DAY_OF_MONTH]

        binding.update.setOnClickListener { update() }
        binding.time.setOnClickListener { pickTime() }
        binding.date.setOnClickListener { pickDate() }
    }

    private fun update() {
        val windowToken = window.decorView.rootView.windowToken
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        binding.root.clearFocus()
        binding.text.text = DateTimeTemplate.format(mCalendar, binding.format.text.toString())
        binding.resultCard.visibility = View.VISIBLE
    }

    // --------------------------------------------------------------------------------------------------------------------------

    private fun pickTime() {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }

    class TimePickerFragment : DialogFragment(), OnTimeSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return TimePickerDialog(activity, this, mHour, mMinute, DateFormat.is24HourFormat(activity))
        }

        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            mHour = hourOfDay
            mMinute = minute
            updateCalendar()
        }
    }

    // --------------------------------------------------------------------------------------------------------------------------

    fun pickDate() {
        DatePickerFragment().show(supportFragmentManager, "datePicker")
    }

    class DatePickerFragment : DialogFragment(), OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return DatePickerDialog(requireActivity(), this, mYear, mMonth, mDay)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            mYear = year
            mMonth = month
            mDay = day
            updateCalendar()
        }
    }

    companion object {
        val mCalendar: Calendar = GregorianCalendar(TimeZone.getDefault())
        protected fun updateCalendar() {
            mCalendar[mYear, mMonth, mDay, mHour] = mMinute
        }

        protected var mHour = 0
        protected var mMinute = 0
        protected var mYear = 0
        protected var mMonth = 0
        protected var mDay = 0
    }
}
