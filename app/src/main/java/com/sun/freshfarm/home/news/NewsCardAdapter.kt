package com.sun.freshfarm.home.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.freshfarm.R

class Adapter(var context: Context, var arrayList: ArrayList<NewsCardItem>) :
    RecyclerView.Adapter<Adapter.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.unit_card, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        val cardItem: NewsCardItem = arrayList.get(position)

//        holder.icons.setImageResource(cardItem.icons!!)
//        holder.titles.text = cardItem.alpha
//
//        holder.titles.setOnClickListener {
//            Toast.makeText(context, cardItem.alpha, Toast.LENGTH_LONG).show()
//        }

    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        var icons = itemView.findViewById<ImageView>(R.id.icon_image_view)
//        var titles = itemView.findViewById<TextView>(R.id.title_text_view)

    }
}