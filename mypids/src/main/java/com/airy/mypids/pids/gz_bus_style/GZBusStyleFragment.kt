package com.airy.mypids.pids.gz_bus_style

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airy.mypids.databinding.FragmentPidsGzBusStyleBinding
import com.airy.mypids.objects.Line
import com.airy.mypids.pids.BasePidsFragment
import com.airy.mypids.pids.PidsStatus

class GZBusStyleFragment(context: Context, private val line: Line) : BasePidsFragment(context) {
    private var _binding: FragmentPidsGzBusStyleBinding? = null
    private val binding get() = _binding!!
    override fun getPidsStyleName(): String = "广州公交风格"

    override fun pidsStationArrived() {
        status = PidsStatus.BUS_STATION_ARRIVED
        binding.layoutStationArrived.visibility = View.VISIBLE
        binding.listStations.visibility = View.GONE
        binding.textCurrStation.text = line.getCurrStationName()
        if(line.isLastStation()){
            binding.textNextStation.text = ""
            binding.textNextStationTag.text = "这是本次列车的终点站"
        }else binding.textNextStation.text = line.getStation(line.currStationIdx+1).name
    }

    override fun pidsRunning() {
        status = PidsStatus.BUS_RUNNING
        binding.layoutStationArrived.visibility = View.GONE
        binding.listStations.visibility = View.VISIBLE
    }

    override fun pidsRunningArriveSoon() {
        TODO("Not yet implemented")
    }

    override fun nextStation() {
        if(line.isLastStation())return
        line.nextStation()
        binding.listStations.nextStation()
        binding.textCurrStation.text = line.getCurrStationName()
        binding.textNextStation.text = if(line.isLastStation()) "已到达本次旅程的" else line.getStation(line.currStationIdx+1).name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPidsGzBusStyleBinding.inflate(inflater, container, false)
        binding.listStations.post {
            val layout = binding.listStations
            layout.line = line
        }
        binding.textLineName.text = line.getShortLineName()
        binding.textStartStation.text = line.getFirstStation().name
        binding.textEndStation.text = line.getLastStation().name
        pidsStationArrived()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}