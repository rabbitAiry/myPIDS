package com.airy.mypids.pids.gz_bus_style

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airy.mypids.databinding.FragmentPidsGzBusStyleBinding
import com.airy.mypids.objects.Line
import com.airy.mypids.pids.RunStopPostStartPids

class GZBusStyleFragment(context: Context, line: Line) : RunStopPostStartPids(context, line) {
    private var _binding: FragmentPidsGzBusStyleBinding? = null
    private val binding get() = _binding!!
    override fun getPidsStyleName(): String = "广州公交风格"
    override fun displayStationArrived() {
        binding.layoutStationArrived.visibility = View.VISIBLE
        binding.listStations.visibility = View.GONE
        binding.textCurrStation.text = line.currStation.name
        if (line.isLastStation) {
            binding.textNextStation.text = ""
            binding.textNextStationTag.text = "这是本次列车的终点站"
        } else binding.textNextStation.text = line.getStation(line.currStationIdx + 1).name
    }

    // temporary return 0 since nothing to post
    override fun displayRunningStart(): Long = 0

    override fun displayRunning() {
        binding.layoutStationArrived.visibility = View.GONE
        binding.listStations.visibility = View.VISIBLE
    }

    override fun displayNextStation() {
//        binding.listStations.nextStation()
        TODO("补充")
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
        binding.textLineName.text = line.briefLineName
        binding.textStartStation.text = line.firstStation.name
        binding.textEndStation.text = line.lastStation.name
        pidsStationArrived(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        _binding = null
    }
}