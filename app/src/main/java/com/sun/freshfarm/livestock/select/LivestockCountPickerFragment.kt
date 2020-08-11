package com.sun.freshfarm.livestock.select

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sun.freshfarm.Constants

import com.sun.freshfarm.R
import kotlinx.android.synthetic.main.livestock_count_picker.view.*


class LivestockCountPickerFragment(val callBack: CounterCallBack) : DialogFragment() {
    private var kind: String? = null
    private var count: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kind = arguments?.getString("kind")
        count = arguments?.getString("count")?.toLong()
        if (kind == null || count == null) {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.livestock_count_picker, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        rootView.livestockCountPickerIcon.setImageResource(Constants.KIND_TO_ID[kind]!!)

        rootView.livestockCountPicker.minValue = 0
        rootView.livestockCountPicker.maxValue = 10000
        rootView.livestockCountPicker.wrapSelectorWheel = false
        rootView.livestockCountPicker.value = count!!.toInt()
        rootView.livestockCountPickerIcon.background =
            if (rootView.livestockCountPicker.value > 0) {
                resources.getDrawable(R.drawable.background_primary_circle_md)
            } else {
                resources.getDrawable(R.drawable.background_round_unselected)
            }


        rootView.livestockCountPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            rootView.livestockCountPickerIcon.background = if (newVal > 0) {
                resources.getDrawable(R.drawable.background_primary_circle_md)
            } else {
                resources.getDrawable(R.drawable.background_round_unselected)
            }
        }

        rootView.livestockCountPickerConfirm.setOnClickListener {
            callBack.onCounterSet(rootView.livestockCountPicker.value.toLong())
            dismiss()
        }
        return rootView
    }

    interface CounterCallBack {
        fun onCounterSet(count: Long)
    }
}
