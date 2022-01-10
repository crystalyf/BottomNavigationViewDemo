package com.change.towerfarm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.change.towerfarm.base.BaseViewModel
import com.change.towerfarm.models.Param
import com.change.towerfarm.models.SensorInformation
import com.change.towerfarm.usecase.MainUseCase
import com.change.towerfarm.utils.Event
import com.change.towerfarm.utils.SharedPrefsManager
import java.util.ArrayList


class MainViewModel(
    private val sharePref: SharedPrefsManager,
    private val useCase: MainUseCase
) : BaseViewModel() {

    //sensor list 情報源(only information)
    val sensorInformationList = MutableLiveData<MutableList<SensorInformation>>()

    private val _showAsidePopWindow = MutableLiveData<Event<Unit>>()
    val showAsidePopWindow: LiveData<Event<Unit>> = _showAsidePopWindow

    private val _dismissAsidePopWindow = MutableLiveData<Event<Unit>>()
    val dismissAsidePopWindow: LiveData<Event<Unit>> = _dismissAsidePopWindow

    //現在選択されているgateway 索引
    var currentGateWayPosition = MutableLiveData<Int>(0)

    /////////////////////////////
    // pieChart ：
    //water temperature pie chart value
    val rangeValueForWaterTemperature = 20.toFloat()
    val maxValueForWaterTemperature = 100.toFloat()

    //water location pie chart value
    val rangeValueForWaterLocation = 43.toFloat()
    val maxValueForWaterLocation = 100.toFloat()

    /////////////////////////////
    // LineChart ：
    //X軸座標データ量(水温)
    val historyWaterTemperatureCount = 6
    //X軸座標データ(水温):
    val historyWaterTemperatureX = ArrayList<String>()

    //X軸座標データ量(ec)
    val historyEcCount = 6
    //X軸座標データ(ec):
    val historyEcX = ArrayList<String>()




    //fixme: false sensor list data
    fun setSensorListData() {
        val list = mutableListOf<SensorInformation>()
        val param11 = Param()
        param11.id = "LTMP"
        param11.name = "temperature"
        param11.min = "-20"
        param11.max = "100"
        param11.unit = "°C"
        val param12 = Param()
        param12.id = "HUM"
        param12.name = "Humidity"
        param12.min = "0"
        param12.max = "100"
        param12.unit = "%"
        val bean1 = SensorInformation()
        bean1.device = "TW2019001"
        bean1.param?.add(param11)
        bean1.param?.add(param12)
        list.add(bean1)

        val param21 = Param()
        param11.id = "LTMP"
        param11.name = "temperature"
        param11.min = "-20"
        param11.max = "100"
        param11.unit = "°C"
        val param22 = Param()
        param12.id = "HUM"
        param12.name = "Humidity"
        param12.min = "0"
        param12.max = "100"
        param12.unit = "%"
        val bean2 = SensorInformation()
        bean2.device = "TW2019002"
        bean2.param?.add(param21)
        bean2.param?.add(param22)
        list.add(bean2)

        val param31 = Param()
        param31.id = "LTMP"
        param31.name = "temperature"
        param31.min = "-20"
        param31.max = "100"
        param31.unit = "°C"
        val param32 = Param()
        param32.id = "HUM"
        param32.name = "Humidity"
        param32.min = "0"
        param32.max = "100"
        param32.unit = "%"
        val bean3 = SensorInformation()
        bean3.device = "TW2019003"
        bean3.param?.add(param31)
        bean3.param?.add(param32)
        list.add(bean3)

        val param41 = Param()
        param41.id = "LTMP"
        param41.name = "temperature"
        param41.min = "-20"
        param41.max = "100"
        param41.unit = "°C"
        val param42 = Param()
        param42.id = "HUM"
        param42.name = "Humidity"
        param42.min = "0"
        param42.max = "100"
        param42.unit = "%"
        val bean4 = SensorInformation()
        bean4.device = "TW2019004"
        bean4.param?.add(param41)
        bean4.param?.add(param42)
        list.add(bean4)
        sensorInformationList.value = list
    }

    fun setSensorHistoryListData() {

    }

    /**
     * click navigation icon
     */
    fun navigationIconClick() {
        _showAsidePopWindow.postValue(Event(Unit))
    }

    /**
     * click aside PopWindow item
     */
    fun aSideItemClick(position: Int) {
        //TODO: send 2 api

        //fixme:
        currentGateWayPosition.value = position
        _dismissAsidePopWindow.postValue(Event(Unit))
    }

}