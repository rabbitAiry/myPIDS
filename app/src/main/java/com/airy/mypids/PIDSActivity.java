package com.airy.mypids;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.airy.mypids.databinding.ActivityPidsBinding;
import com.airy.mypids.pidsStyle.BasicVerticalPidsFragment;

public class PIDSActivity extends AppCompatActivity {
    private ActivityPidsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPidsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)actionBar.hide();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.pids_container, new BasicVerticalPidsFragment(new ActivityProgressBarSetting())).commit();
    }

    class ActivityProgressBarSetting implements BasicVerticalPidsFragment.ProgressBarSetting {
        @Override
        public void setProgressBarVisibility(int visibility) {
            binding.pidsProgressBar.setVisibility(visibility);
        }
    }
}