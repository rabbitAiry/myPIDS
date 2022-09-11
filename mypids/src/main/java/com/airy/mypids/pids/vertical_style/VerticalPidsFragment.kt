package com.airy.mypids.pids.vertical_style

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.airy.mypids.databinding.FragmentPidsVerticalStyleBinding
import com.airy.mypids.objects.Line
import com.airy.mypids.pids.BasePidsFragment
import com.airy.mypids.pids.PidsStatus

const val PIDS_SWITCH = 1

class VerticalPidsFragment(context: Context, private val line: Line) : BasePidsFragment(context) {
    override fun getPidsStyleName(): String = "基础竖向风格"
    private var _binding: FragmentPidsVerticalStyleBinding? = null
    private val binding get() = _binding!!
    private val adapter = VerticalPidsAdapter(line.getStationNames(), context, line.currStationIdx)

    override fun pidsStationArrived() {
        handler.removeMessages(PIDS_SWITCH)
        status = PidsStatus.BUS_STATION_ARRIVED
        adapter.stationArrived()
        val text = if (line.isLastStation())
            StringBuilder(line.getCurrStationName()).append("，这是本趟旅程的终点站，请所有乘客带齐行李，欢迎再次乘搭")
                .append(line.lineName)
        else StringBuilder("当前到站：").append(line.getCurrStationName())
        setNextStationText(text.toString())
    }

    override fun pidsRunning() {
        if (status == PidsStatus.BUS_RUNNING || line.isLastStation()) return
        if (status == PidsStatus.BUS_STATION_ARRIVED) {
            status = PidsStatus.BUS_RUNNING_START
            adapter.busRunning()
            nextStation()
            setNextStationText("尊老爱幼是中华民族的传统美德，请您为有需要的乘客让座，谢谢！")
            val message = Message.obtain(handler) {
                pidsRunning()
            }
            message.what = PIDS_SWITCH
            handler.sendMessageDelayed(message, 12000)
            return
        }
        handler.removeMessages(PIDS_SWITCH)
        status = PidsStatus.BUS_RUNNING
        setNextStationText(StringBuilder("下一站：").append(line.getCurrStationName()).toString())
    }

    override fun pidsRunningArriveSoon() {
        TODO("Not yet implemented")
    }

    override fun nextStation() {
        handler.removeMessages(PIDS_SWITCH)
        if (line.isLastStation()) return
        binding.listStations.smoothScrollToPosition(line.nextStation() + 6)
        adapter.nextStation()
        if (status == PidsStatus.BUS_STATION_ARRIVED) pidsStationArrived()
        else {
            status = PidsStatus.BUS_RUNNING_START
            pidsRunning()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPidsVerticalStyleBinding.inflate(inflater, container, false)
        binding.listStations.adapter = adapter
        binding.listStations.layoutManager = object:LinearLayoutManager(context){
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView?,
                state: RecyclerView.State?,
                position: Int
            ) {
                val scroller = object: LinearSmoothScroller(context){
                    override fun calculateTimeForScrolling(dx: Int): Int =
                        super.calculateTimeForScrolling(dx)*20
                }
                scroller.targetPosition = position
                startSmoothScroll(scroller)
            }
        }
        binding.listStations.itemAnimator = null
        binding.listStations.setHasFixedSize(true)
        binding.textLineName.text = line.lineName
        binding.textLineDescription.text = StringBuilder(line.directionName).append("方向").toString()
        pidsStationArrived()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }

    private fun setNextStationText(text: String) {
        binding.textNextStation.setText(text)
    }
}