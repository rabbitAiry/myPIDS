package com.airy.mypids.pidsStyle;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airy.mypids.databinding.FragmentGzBusStylePidsBinding;

public class GZStylePidsFragment extends BasicPidsFragment{
    private FragmentGzBusStylePidsBinding binding;
    private Context context;

    public GZStylePidsFragment(ProgressBarSetting progressBarSetting) {
        super(progressBarSetting);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGzBusStylePidsBinding.inflate(inflater, container, false);
        context = container.getContext();
        getLine(context);
        return binding.getRoot();
    }

    @Override
    void refreshView() {

    }
}
