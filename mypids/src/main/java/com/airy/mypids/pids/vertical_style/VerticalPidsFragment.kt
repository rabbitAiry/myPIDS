package com.airy.mypids.pids.vertical_style

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.data.LineInfo
import com.airy.mypids.data.StationListInfo
import com.airy.mypids.databinding.FragmentPidsVerticalStyleBinding
import com.airy.mypids.pids.RunStopPostStartPids

class VerticalPidsFragment(context: Context, private val lineInfo: LineInfo, stations: StationListInfo) :
    RunStopPostStartPids(context, stations) {
    override fun getPidsStyleName(): String = "基础竖向风格"
    private var _binding: FragmentPidsVerticalStyleBinding? = null
    private val binding get() = _binding!!
    private val adapter = VerticalPidsAdapter(stations.stations, context, stations.currIdx)

    override fun displayStationArrived() {
        adapter.stationArrived()
        setNextStationText(
            if (stations.isLastStation()) StringBuilder(stations.currStation.name).append("，这是本趟旅程的终点站，请所有乘客带齐行李，欢迎再次乘搭")
                .append(lineInfo.rawLineName).toString()
            else StringBuilder("当前到站：").append(stations.currStation.name).toString()
        )
    }

    override fun displayRunningStart() :Long{
        adapter.busRunning()
        setNextStationText("尊老爱幼是中华民族的传统美德，请您为有需要的乘客让座，谢谢！")
        return 16000
    }

    override fun displayRunning() {
        setNextStationText(StringBuilder("下一站：").append(stations.currStation.name).toString())
    }

    override fun displayNextStation() {
        binding.listStations.smoothScrollToPosition(stations.currIdx + 6)
        adapter.nextStation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPidsVerticalStyleBinding.inflate(inflater, container, false)
        binding.listStations.adapter = adapter
        binding.listStations.layoutManager = object : LinearLayoutManager(context) {
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView?,
                state: RecyclerView.State?,
                position: Int
            ) {
                val scroller = object : LinearSmoothScroller(context) {
                    override fun calculateTimeForScrolling(dx: Int): Int =
                        super.calculateTimeForScrolling(dx) * 20
                }
                scroller.targetPosition = position
                startSmoothScroll(scroller)
            }
        }
        binding.listStations.itemAnimator = null
        binding.listStations.setHasFixedSize(true)
        binding.textLineName.text = lineInfo.rawLineName
        binding.textLineDescription.text = StringBuilder(stations.terminal.name).append("方向").toString()
        pidsStationArrived(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }

    private fun setNextStationText(text: String) {
        binding.textNextStation.post {
            binding.textNextStation.setText(text)
        }
    }
}