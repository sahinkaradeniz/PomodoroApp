package com.skapps.YksStudyApp.view.Pomodoro.Statistics

import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.skapps.YksStudyApp.R
import com.skapps.YksStudyApp.Statistics.Pomodoro.PomodoroStatistics
import com.skapps.YksStudyApp.databinding.FragmentStatisticBinding
import com.skapps.YksStudyApp.view.Pomodoro.Statistics.StatisticViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StatisticFragment : Fragment() {

    private lateinit var viewModel: StatisticViewModel
    private var _binding:FragmentStatisticBinding?=null
    private val binding get() = _binding
   // private lateinit var barEntry:ArrayList<BarEntry>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       viewModel = ViewModelProvider(this).get(StatisticViewModel::class.java)
        _binding=FragmentStatisticBinding.inflate(inflater,container,false)
       viewModel.getDataBarEntry()
       viewModel.getDataLineEntry()
       viewModel.getDataPieEntry()
       observeLiveData()
       // viewModel.getDataBarEntry()
      //  val barEntry= arrayListOf<BarEntry>()
        //pie Chart
        return binding?.root

    }
   fun setPieChartData(pieEntry:ArrayList<PieEntry>){
       // Colors
       val colors: MutableList<Int> = ArrayList()
       colors.add(resources.getColor(R.color.green))
       colors.add(resources.getColor(R.color.pie_chart_1))
       colors.add(resources.getColor(R.color.pie_chart_2))
       colors.add(resources.getColor(R.color.pie_chart_4))
       colors.add(resources.getColor(R.color.pie_chart_5))
       colors.add(resources.getColor(R.color.pie_chart_6))
       val pieDataSet=PieDataSet(pieEntry,"")
       pieDataSet.valueTextSize=15f
       pieDataSet.setColors(colors)
       val pieData=PieData(pieDataSet)
       //binding!!.pieChart.setTouchEnabled(false)
       binding!!.pieChart.apply {
           //pieData.setValueFormatter(PercentFormatter())
           description=null
           setDrawSliceText(false)
           setDrawMarkers(false)
           pieData.setValueTextSize(10f)
           pieData.setValueTextColor(Color.WHITE);
           setUsePercentValues(true)
           setHoleColor(Color.WHITE)
           transparentCircleRadius = 0f
           invalidate()
           legend.isEnabled = true
           centerText="Aktiviteler"
           animateY(1400, Easing.EaseInOutQuad)
           data=pieData
       }
   }

   fun setLineChartData(linevalues:ArrayList<Entry>) {
        val linedataset = LineDataSet(linevalues, "First")
        //We add features to our chart
        linedataset.color =ContextCompat.getColor(requireContext(),R.color.colorPrimary)
       //linedataset.circleRadius = 10f
        linedataset.setDrawFilled(true)
        linedataset.lineWidth=2f
        linedataset.isHighlightEnabled=true
       // linedataset.valueTextSize = 20F
        linedataset.fillColor =ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        linedataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        binding!!.lineChart.apply {
            axisRight.isEnabled=false

            axisLeft.apply {
                isEnabled=false
            }
            xAxis.apply {
                setDrawAxisLine(false)
                axisMaximum=7f // gün sayisi
                setDrawAxisLine(false)
                setDrawGridLines(false)
                position =XAxis.XAxisPosition.BOTTOM
                setTouchEnabled(false)
                description.text="Bu Hafta"
                legend.isEnabled=false
            }
        }
        //We connect our data to the UI Screen
        val data = LineData(linedataset)
        binding!!.lineChart.data = data
        binding!!.lineChart.animateXY(1000, 1000,Easing.EaseInCubic)
    }
    fun setDataChart(barEntry:ArrayList<BarEntry>){
        val activity= arrayOf("Konu A.","Soru","Konu T.","Deneme","Kitap O.","Diğer")
        val barDataSet= BarDataSet(barEntry,"Yapılan Aktiviteler")
        barDataSet.valueTextSize=15f
        barDataSet.setGradientColor(ContextCompat.getColor(requireContext(),R.color.grey),Color.parseColor("#448AFF"))
        barDataSet.setColors(ContextCompat.getColor(requireContext(),R.color.grey), Color.parseColor("#448AFF"))
        val barData= BarData(barDataSet)
        binding?.historyChart?.apply {
            animateY(1500)
            setFitBars(true)
            data=barData
            description=null
            setTouchEnabled(false)
            xAxis.position= XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter= IndexAxisValueFormatter(activity)
            axisRight.isEnabled=false
            // setTouchEnabled(false)
            xAxis.isEnabled=true
            xAxis.setDrawGridLines(false)
            axisLeft.isEnabled=false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    private fun observeLiveData(){
        viewModel.dataBarEntry.observe(viewLifecycleOwner){
            setDataChart(it)
        }
        viewModel.dataLineEntry.observe(viewLifecycleOwner){
            setLineChartData(it)
        }
        viewModel.dataPieEntry.observe(viewLifecycleOwner){
            setPieChartData(it)
        }
    }

}