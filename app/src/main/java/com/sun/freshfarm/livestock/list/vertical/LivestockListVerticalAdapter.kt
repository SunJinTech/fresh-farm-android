package com.sun.freshfarm.livestock.list.vertical

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.Constants
import com.sun.freshfarm.R
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.livestock.select.LivestockCountPickerFragment
import kotlinx.android.synthetic.main.unit_livestock_vertical.view.*
import org.jetbrains.anko.support.v4.withArguments


class LivestockListVerticalAdapter(
    val data: List<Livestock>,
    val displayCountOption: Boolean,
    val context: Context,
    val callBack: LivestockListCallBack
) :
    RecyclerView.Adapter<LivestockListVerticalAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.unit_livestock_vertical, parent, false)
        return ViewHolder(
            itemView
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val livestock = data[position]
        updateView(livestock, holder.itemView)

        holder.itemView.setOnClickListener {
            val activity = context as FragmentActivity
            LivestockCountPickerFragment(object : LivestockCountPickerFragment.CounterCallBack {
                override fun onCounterSet(count: Long) {
                    callBack.onCountChanged(Livestock(data[position].kind, count))
                    livestock.count = count
                    updateView(livestock, holder.itemView)
                }
            }).withArguments("count" to livestock.count.toString(), "kind" to livestock.kind)
                .show(activity.supportFragmentManager, "counterPicker")
        }

        holder.itemView.livestockCount.visibility = if (displayCountOption) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private fun updateView(livestock: Livestock, itemView: View) {
        itemView.livestockIcon.setImageResource(Constants.KIND_TO_ID[livestock.kind]!!)

        itemView.livestockKind.text = Constants.toKorean(livestock.kind)
        itemView.livestockCount.text = "${livestock.count}마리"

        itemView.livestockIcon.setBackgroundResource(
            if (livestock.count > 0) {
                R.drawable.background_primary_circle_md
            } else {
                R.drawable.background_round_unselected
            }
        )


    }

    interface LivestockListCallBack {
        fun onCountChanged(livestock: Livestock)
    }
}