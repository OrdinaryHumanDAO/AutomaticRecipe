package com.example.calendartestapp.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.calendartestapp.R
import com.example.calendartestapp.retrofit.Recipe
import kotlinx.android.synthetic.main.list_item_calendar.view.*
import kotlinx.android.synthetic.main.list_item_calendar_header.view.*
import java.util.*

class CalendarAdapter(private val listener:ListListener) : RecyclerView.Adapter<CalendarAdapter
.BaseViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = R.layout.list_item_calendar_header
        private const val VIEW_TYPE_DAY = R.layout.list_item_calendar
    }

    var dataSource: Array<CalendarItem> = emptyArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface ListListener {
        fun onClickItem(tappedView: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> CalendarHeaderViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            VIEW_TYPE_DAY -> CalendarDayViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            else -> throw IllegalStateException("Bad view type!!")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataSource[position]
        when (item) {
            is CalendarItem.Header -> {
                holder.setViewData(item)
            }
            is CalendarItem.Day -> {
                holder.setViewData(item)
            }
        }
        holder.itemView.setOnClickListener {
            //val recipeItem = item as CalendarItem.Day
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DATE, 1)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dbRecipePosition = position - (7 + dayOfWeek - 1)
            //dbRecipePosition.toString()
            Toast.makeText(it.context, "$position ${7 + dayOfWeek - 1}", Toast.LENGTH_LONG).show()
            listener.onClickItem(it, dbRecipePosition)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSource[position]) {
            is CalendarItem.Header -> VIEW_TYPE_HEADER
            is CalendarItem.Day -> VIEW_TYPE_DAY
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setViewData(item: CalendarItem)
    }

    private class CalendarHeaderViewHolder(view: View) : BaseViewHolder(view) {

        private val headerLabel: TextView = view.headerLabel

        override fun setViewData(item: CalendarItem) {
            item as CalendarItem.Header
            headerLabel.text = item.text
        }
    }

    private class CalendarDayViewHolder(view: View) : BaseViewHolder(view) {

        private val dayLabel: TextView = view.dayLabel
        private val tv: TextView = view.tv

        override fun setViewData(item: CalendarItem) {
            item as CalendarItem.Day

            dayLabel.text = item.day
            if (item.day.isEmpty()) {
                //dayLabel.visibility = View.GONE
            } else {
                dayLabel.visibility = View.VISIBLE
                tv.text = item.recipeInfo!!.recipeTitle
            }
            if (item.isToDay) {
                itemView.setBackgroundColor(Color.YELLOW)
            } else {
                itemView.setBackgroundColor(Color.WHITE)
            }
        }
    }
}