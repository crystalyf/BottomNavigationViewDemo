package com.change.towerfarm.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.change.towerfarm.R
import com.change.towerfarm.activity.MainActivity
import com.change.towerfarm.base.BaseFragment
import com.change.towerfarm.databinding.FragmentHomeBinding
import com.change.towerfarm.extensions.getViewModelFactory
import com.change.towerfarm.utils.EventObserver
import com.change.towerfarm.viewmodels.MainViewModel
import com.change.towerfarm.views.AsidePopupWindow
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*

class HomeFragment : BaseFragment() {

    private val viewModel by activityViewModels<MainViewModel> { getViewModelFactory() }
    private var dataBinding: FragmentHomeBinding? = null

    //Sensor information listサイドバー
    private var popupWindow: AsidePopupWindow? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        dataBinding?.viewModel = viewModel
        return dataBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding?.lifecycleOwner = this.viewLifecycleOwner
        observeApiErrorEvent(viewModel)
        observeApiLoadingEvent(viewModel)
        initView()
    }

    private fun initView() {

        initWaterTemperaturePieView()
        initWaterLocationPieView()
        initWaterTemperatureLineView()
        initEcLineView()

        viewModel.currentGateWayPosition.observe(this.viewLifecycleOwner, {
            (activity as MainActivity).getToolbar()?.title =
                viewModel.sensorInformationList.value?.get(viewModel.currentGateWayPosition.value?:0)?.device
        })

        viewModel.showAsidePopWindow.observe(this.viewLifecycleOwner, EventObserver {
            if (isResumed) {
                showAsidePopWindow()
            }
        })

        viewModel.dismissAsidePopWindow.observe(this.viewLifecycleOwner, EventObserver {
            if (isResumed) {
                closePopWindow()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        //最初に最初の gateway を表示します
        if (viewModel.sensorInformationList.value?.isNotEmpty() == true) {
            (activity as MainActivity).getToolbar()?.title =
                viewModel.sensorInformationList.value?.get(viewModel.currentGateWayPosition.value?:0)?.device
        }
    }

    /**
     * 初期化水温Sensor
     */
    private fun initWaterTemperaturePieView() {
        dataBinding?.chartWaterTemperature?.extraBottomOffset = 0f
        dataBinding?.chartWaterTemperature?.extraLeftOffset = 0f
        dataBinding?.chartWaterTemperature?.extraRightOffset = 0f
        dataBinding?.chartWaterTemperature?.setBackgroundColor(Color.WHITE)
        dataBinding?.chartWaterTemperature?.description?.isEnabled = false
        dataBinding?.chartWaterTemperature?.isDrawHoleEnabled = true
        //円グラフの中央にある円の描画色
        dataBinding?.chartWaterTemperature?.setHoleColor(Color.WHITE)
        dataBinding?.chartWaterTemperature?.setTransparentCircleColor(Color.WHITE)
        dataBinding?.chartWaterTemperature?.setTransparentCircleAlpha(110)
        //円グラフの中央にある円の半径
        dataBinding?.chartWaterTemperature?.holeRadius = 48f
        //半径値を設定します
        dataBinding?.chartWaterTemperature?.transparentCircleRadius = 51f
        dataBinding?.chartWaterTemperature?.setDrawCenterText(true)
        dataBinding?.chartWaterTemperature?.isRotationEnabled = false
        dataBinding?.chartWaterTemperature?.isHighlightPerTapEnabled = true
        //半円180f、全円360f
        dataBinding?.chartWaterTemperature?.maxAngle = 360f
        dataBinding?.chartWaterTemperature?.rotationAngle = 360f
        dataBinding?.chartWaterTemperature?.setCenterTextOffset(0f, 0f)
        setWaterTemperatureChartPieData(
            viewModel.rangeValueForWaterTemperature,
            viewModel.maxValueForWaterTemperature
        )
        dataBinding?.chartWaterTemperature?.centerText =
            setPieChartCenterText(viewModel.rangeValueForWaterTemperature)
        dataBinding?.chartWaterTemperature?.animateY(1400, Easing.EaseInOutQuad)
        val l: Legend? = dataBinding?.chartWaterTemperature?.legend
        l?.yOffset = -50f
        //はかりを外します
        l?.isEnabled = false
        dataBinding?.chartWaterTemperature?.setEntryLabelColor(Color.WHITE)
        dataBinding?.chartWaterTemperature?.setEntryLabelTextSize(12f)
    }

    /**
     * 初期化水位Sensor
     */
    private fun initWaterLocationPieView() {
        dataBinding?.chartWaterLocation?.extraBottomOffset = 0f
        dataBinding?.chartWaterLocation?.extraLeftOffset = 0f
        dataBinding?.chartWaterLocation?.extraRightOffset = 0f
        dataBinding?.chartWaterLocation?.setBackgroundColor(Color.WHITE)
        dataBinding?.chartWaterLocation?.description?.isEnabled = false
        dataBinding?.chartWaterLocation?.isDrawHoleEnabled = true
        //円グラフの中央にある円の描画色
        dataBinding?.chartWaterLocation?.setHoleColor(Color.WHITE)
        dataBinding?.chartWaterLocation?.setTransparentCircleColor(Color.WHITE)
        dataBinding?.chartWaterLocation?.setTransparentCircleAlpha(110)
        //円グラフの中央にある円の半径
        dataBinding?.chartWaterLocation?.holeRadius = 48f
        //半径値を設定します
        dataBinding?.chartWaterLocation?.transparentCircleRadius = 51f
        dataBinding?.chartWaterLocation?.setDrawCenterText(true)
        dataBinding?.chartWaterLocation?.isRotationEnabled = false
        dataBinding?.chartWaterLocation?.isHighlightPerTapEnabled = true
        //半円180f、全円360f
        dataBinding?.chartWaterLocation?.maxAngle = 180f
        dataBinding?.chartWaterLocation?.rotationAngle = 180f
        dataBinding?.chartWaterLocation?.setCenterTextOffset(0f, 0f)
        setWaterLocationChartPieData(
            viewModel.rangeValueForWaterLocation,
            viewModel.maxValueForWaterLocation
        )
        dataBinding?.chartWaterLocation?.centerText =
            setPieChartCenterText(viewModel.rangeValueForWaterLocation)
        dataBinding?.chartWaterLocation?.animateY(1400, Easing.EaseInOutQuad)
        val l: Legend? = dataBinding?.chartWaterLocation?.legend
        l?.yOffset = -50f
        //はかりを外します
        l?.isEnabled = false
        dataBinding?.chartWaterLocation?.setEntryLabelColor(Color.WHITE)
        dataBinding?.chartWaterLocation?.setEntryLabelTextSize(12f)
    }

    /**
     * charPetセットのデータソース(水温)
     * @param range
     * @param max
     */
    private fun setWaterTemperatureChartPieData(range: Float, max: Float) {
        val values = ArrayList<PieEntry>()
        //2セグメントの円グラフを設定する
        values.add(PieEntry(range, ""))
        values.add(PieEntry(max - range, ""))
        val dataSet = PieDataSet(values, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        //円グラフの各セグメントの色を設定します
        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        //[%]のサイズと色
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        dataBinding?.chartWaterTemperature?.data = data
        dataBinding?.chartWaterTemperature?.invalidate()
    }

    /**
     * charPetセットのデータソース(WaterLocation)
     * @param range
     * @param max
     */
    private fun setWaterLocationChartPieData(range: Float, max: Float) {
        val values = ArrayList<PieEntry>()
        //2セグメントの円グラフを設定する
        values.add(PieEntry(range, ""))
        values.add(PieEntry(max - range, ""))
        val dataSet = PieDataSet(values, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        //円グラフの各セグメントの色を設定します
        dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        //[%]のサイズと色
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        dataBinding?.chartWaterLocation?.data = data
        dataBinding?.chartWaterLocation?.invalidate()
    }

    /**
     * pieChart設定します：半円の中心にあるテキスト
     *
     * @return
     */
    private fun setPieChartCenterText(range: Float): SpannableString {
        return SpannableString(range.toString() + "")
    }

    private fun initWaterTemperatureLineView() {
        //右軸を描画しないでください
        dataBinding?.lineChartWaterTemperature?.axisRight?.isEnabled = false
        val l: Legend? = dataBinding?.lineChartWaterTemperature?.legend
        l?.isEnabled = false
        val data: LineData = generateWaterTemperatureDataLine()
        dataBinding?.lineChartWaterTemperature?.data = data

        for (i in 0 until viewModel.historyWaterTemperatureCount) {
            viewModel.historyWaterTemperatureX.add((2 + i).toString() + ":00")
        }
        val xl: XAxis? = dataBinding?.lineChartWaterTemperature?.xAxis
        xl?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return viewModel.historyWaterTemperatureX[value.toInt()]
            }
        }
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setDrawAxisLine(true)
        xl?.setDrawGridLines(false)
        //X軸スケールは最初と最後で繰り返されます，-2
        xl?.labelCount = viewModel.historyWaterTemperatureCount - 2
    }

    private fun initEcLineView() {
        //右軸を描画しないでください
        dataBinding?.lineChartEc?.axisRight?.isEnabled = false
        val l: Legend? = dataBinding?.lineChartEc?.legend
        l?.isEnabled = false
        val data: LineData = generateEcDataLine()
        dataBinding?.lineChartEc?.data = data

        for (i in 0 until viewModel.historyEcCount) {
            viewModel.historyEcX.add((2 + i).toString() + ":00")
        }
        val xl: XAxis? = dataBinding?.lineChartEc?.xAxis
        xl?.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return viewModel.historyEcX[value.toInt()]
            }
        }
        xl?.position = XAxis.XAxisPosition.BOTTOM
        xl?.setDrawAxisLine(true)
        xl?.setDrawGridLines(false)
        //X軸スケールは最初と最後で繰り返されます，-2
        xl?.labelCount = viewModel.historyEcCount - 2
    }

    /**
     * 折れ線グラフのデータソースを設定します(水温)
     */
    private fun generateWaterTemperatureDataLine(): LineData {
        val values1 = ArrayList<Entry>()
        for (i in 0 until viewModel.historyWaterTemperatureCount) {
            values1.add(Entry(i.toFloat(), (Math.random() * 65).toInt().toFloat()))
        }
        val d1 = LineDataSet(values1, "")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        //line color
        d1.color = R.color.red
        d1.setCircleColor(R.color.red)
        d1.setDrawValues(false)
        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        return LineData(sets)
    }

    /**
     * 折れ線グラフのデータソースを設定します(Ec)
     */
    private fun generateEcDataLine(): LineData {
        val values1 = ArrayList<Entry>()
        for (i in 0 until viewModel.historyEcCount) {
            values1.add(Entry(i.toFloat(), (Math.random() * 65).toInt().toFloat()))
        }
        val d1 = LineDataSet(values1, "")
        d1.lineWidth = 2.5f
        d1.circleRadius = 4.5f
        //line color
        d1.color = Color.rgb(255, 140, 157)
        d1.setCircleColor(Color.rgb(255, 140, 157))

        d1.setDrawValues(false)
        val sets = ArrayList<ILineDataSet>()
        sets.add(d1)
        return LineData(sets)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dataBinding = null
    }

    private fun showAsidePopWindow() {
        popupWindow = AsidePopupWindow(requireContext(), requireActivity(), viewModel)
        popupWindow?.showWindow(dataBinding?.root!!)
    }

    private fun closePopWindow() {
       if(popupWindow?.isShowing == true){
           popupWindow?.dismiss()
       }
    }

}