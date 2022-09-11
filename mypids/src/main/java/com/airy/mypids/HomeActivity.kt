package com.airy.mypids

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.adapter.LineSearchAdapter
import com.airy.mypids.adapter.StationAdapter
import com.airy.mypids.databinding.ActivityHomeBinding
import com.airy.mypids.objects.Line
import com.airy.mypids.objects.Station
import com.airy.mypids.utils.LineUtil
import com.airy.mypids.utils.SearchUtil
import com.baidu.mapapi.search.busline.BusLineResult
import com.baidu.mapapi.search.busline.BusLineSearchOption
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener
import com.baidu.mapapi.search.core.PoiInfo
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val TAG = "BusLine"
    private lateinit var binding: ActivityHomeBinding
    private val adapter = LineSearchAdapter {
        switchToChosen()
        binding.progressBarLine.visibility = View.VISIBLE
        SearchUtil.getBusLineSearch().searchBusLine(BusLineSearchOption().city(it.city).uid(it.uid))
    }
    private var status = LineStatus.NOT_CHOOSE
    private lateinit var line: Line

    enum class LineStatus{
        NOT_CHOOSE, IN_CHOOSE, CHOSEN
    }

    /**
     * 搜索线路回调
     */
    private val mPoiSearchListener = object : OnGetPoiSearchResultListener {
        override fun onGetPoiResult(p0: PoiResult?) {
            binding.progressBarLine.visibility = View.GONE
            if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(this@HomeActivity, "搜索出错：${p0?.error?.name}", Toast.LENGTH_SHORT)
                    .show()
                switchToNotChoose()
                return
            }
            val list = LinkedList<PoiInfo>()
            for (info in p0.allPoi) {
                val tag = info.getPoiDetailInfo().tag
                if (tag.contains("地铁线路") || tag.contains("公交线路")) list.add(info)
            }
            adapter.list = list
        }

        @Deprecated("Deprecated in Java")
        override fun onGetPoiDetailResult(p0: PoiDetailResult?) {
        }

        override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {}
        override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {}
    }

    /**
     * 搜索线路详细信息回调
     */
    private val mBusLineSearchListener = object : OnGetBusLineSearchResultListener {
        override fun onGetBusLineResult(p0: BusLineResult?) {
            binding.progressBarLine.visibility = View.GONE
            if (p0 == null || p0.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(this@HomeActivity, "查找线路出错：${p0?.error?.name}", Toast.LENGTH_SHORT)
                    .show()
                switchToInChoose()
                return
            }
            try {
                line = LineUtil.baiduLineToAppLine(p0)
            }catch (e:NullPointerException){
                Toast.makeText(this@HomeActivity, "数据缺失", Toast.LENGTH_SHORT).show()
                return
            }
            binding.textLineDescription.text = line.getLineDescription()
            binding.buttonSelectStation.text = line.getFirstStation().name
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SearchUtil.initSearches(mPoiSearchListener, mBusLineSearchListener)
        initUI()
    }

    private fun initUI() {
        supportActionBar?.hide()
        switchToNotChoose()
        binding.progressBarLine.visibility = View.GONE
        binding.buttonLineSearch.setOnClickListener {
            val cityText = binding.editCitySearch.text.toString().trim()
            val lineText = binding.editLineSearch.text.toString().trim()
            if (lineText.isEmpty() || cityText.isEmpty()) {
                Toast.makeText(this, "内容不应为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            SearchUtil.getPoiSearch().searchInCity(
                PoiCitySearchOption().city(cityText).keyword(lineText).scope(2).pageCapacity(1000)
            )
            binding.progressBarLine.visibility = View.VISIBLE
            switchToInChoose()
        }
        binding.listSearchResult.layoutManager = LinearLayoutManager(this)
        binding.listSearchResult.adapter = adapter
        binding.buttonClearLine.setOnClickListener {
            status = LineStatus.IN_CHOOSE
            switchToNotChoose()
        }
        binding.buttonSelectStation.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_select_station, null)
            val recyclerView : RecyclerView= view.findViewById(R.id.list)
            dialogBuilder.setView(view)
            val dialog = dialogBuilder.show()
            val stationAdapter = StationAdapter(line.stations){
                line.currStationIdx = it
                binding.buttonSelectStation.text = line.getCurrStationName()
                dialog.cancel()
            }
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = stationAdapter
        }
        binding.buttonStartPids.setOnClickListener {
            val intent = Intent(this, PidsActivity::class.java)
            intent.putExtra("Line", line)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SearchUtil.releaseSearches()
    }

    private fun switchToNotChoose() {
        clearStatus()
        status = LineStatus.NOT_CHOOSE
        binding.layoutSearchBar.visibility = View.VISIBLE
    }

    private fun switchToInChoose() {
        clearStatus()
        status = LineStatus.IN_CHOOSE
        binding.layoutSearchBar.visibility = View.VISIBLE
        binding.listSearchResult.visibility = View.VISIBLE
    }

    private fun switchToChosen() {
        clearStatus()
        status = LineStatus.CHOSEN
        binding.layoutSelectedLine.visibility = View.VISIBLE
        binding.layoutSelectStation.visibility = View.VISIBLE
        binding.buttonStartPids.isEnabled = true
    }

    private fun clearStatus() {
        binding.layoutSearchBar.visibility = View.GONE
        binding.listSearchResult.visibility = View.GONE
        binding.layoutSelectedLine.visibility = View.GONE
        binding.layoutSelectStation.visibility = View.GONE
        binding.buttonStartPids.isEnabled = false
    }
}

/*
poiInfo.toString()内容

PoiInfo: name = 36路(珠江泳场总站(海印公园)-黄石路总站); uid = e16f760277215d6195ca07a4; address = ; province = 广东省; city = 广州市;
area = ; street_id = ; phoneNum = ; postCode = null; detail = 0; location = latitude: 23.112254, longitude: 113.294811; hasCaterDetails = false;
isPano = false; tag = null; poiDetailInfo = PoiDetailInfo: name = null; location = null; address = null; province = null; city = null;
area = null; telephone = null; uid = null; detail = 0; distance = 0; type = ; tag = 公交线路;普通日行公交车; naviLocation = null; detailUrl = ;
price = 0.0; shopHours = ; overallRating = 0.0; tasteRating = 0.0; serviceRating = 0.0; environmentRating = 0.0; facilityRating = 0.0; hygieneRating = 0.0;
technologyRating = 0.0; imageNum = 0; grouponNum = 0; discountNum = 0; commentNum = 0; favoriteNum = 0; checkinNum = 0; direction = null; distance = 0

 */