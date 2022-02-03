package com.example.calendartestapp.calendarRecipe

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calendartestapp.R
import com.example.calendartestapp.calendar.CalendarAdapter
import com.example.calendartestapp.calendar.CalendarItemFactory
import com.example.calendartestapp.database.AppDatabase
import com.example.calendartestapp.database.RecipeEntity
import com.example.calendartestapp.retrofit.Recipe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_calendar_recipe.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalendarRecipeFragment : Fragment() {

    private var offsetMonth = 0
    private val viewModel by viewModels<CalendarRecipeViewModel>()
    //private lateinit var viewModel: CalendarRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_recipe, container, false)

        //Hilt,Ktxを使うといらない
        //val viewModelFactory = CalendarRecipeViewModelFactory(view.context)
        //viewModel = ViewModelProvider(this, viewModelFactory).get(CalendarRecipeViewModel::class.java)


        val adapter = CalendarAdapter(object : CalendarAdapter.ListListener {
            override fun onClickItem(tappedView: View, position: Int) {
                this@CalendarRecipeFragment.onClickItem(tappedView, position)
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(view.context, 7)

        viewModel.recipeEntity.observe(this,{
            if(it.isNotEmpty()) {
                btnSetRecipe.isEnabled = false
                val list: MutableList<Recipe.Result> = mutableListOf()
                for(i in it) {
                    list.add(
                        Recipe.Result(
                            i.foodImageUrl,
                            i.recipeCost,
                            i.recipeIndication,
                            i.recipeTitle,
                            i.recipeUrl,
                            i.recipeMaterial
                        )
                    )
                }
                adapter.dataSource = CalendarItemFactory.create(offsetMonth, list, view.context)
            } else {
                if(findNavController().currentDestination?.id == R.id.calendarRecipeFragment) {
                    findNavController().navigate(R.id.action_calendarRecipeFragment_to_noDataFragment)
                    Log.d("aiueo", "noDataに遷移します(month)")
                }
            }
        })


        val bs: Button = view.findViewById(R.id.btn_db_set)
        val bd: Button = view.findViewById(R.id.btn_db_delete)
        bs.setOnClickListener {
        }
        bd.setOnClickListener {
            viewModel.allDelete()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateDateLabel()
    }


    private fun updateDateLabel() {
        val dateLabel: TextView = view!!.findViewById(R.id.dateLabel)
        dateLabel.text = Date().apply { offset(month = offsetMonth) }.toYearMonthText()
    }

    private fun Date.offset(year: Int = 0, month: Int = 0, day: Int = 0) {
        time = Calendar.getInstance().apply {
            add(Calendar.YEAR, year)
            add(Calendar.MONTH, month)
            add(Calendar.DATE, day)
        }.timeInMillis
    }

    private fun Date.toYearMonthText(): String {
        return SimpleDateFormat("yyyy/MM").format(time)
    }

    private fun onClickItem(tappedView: View, position: Int) {
        val action = CalendarRecipeFragmentDirections
            .actionCalendarRecipeFragmentToCalendarRecipeDetailFragment(
                viewModel.recipeEntity.value!![position].date)
        findNavController().navigate(action)
        Log.d("aiueo", "recipeDetailに遷移します")
    }

}