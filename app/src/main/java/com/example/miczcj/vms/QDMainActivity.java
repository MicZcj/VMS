package com.example.miczcj.vms;

import android.os.Bundle;

import com.example.miczcj.vms.base.BaseFragment;
import com.example.miczcj.vms.base.BaseFragmentActivity;
import com.example.miczcj.vms.fragment.home.HomeFragment;


public class QDMainActivity extends BaseFragmentActivity {

	@Override
	protected int getContextViewId() {
		return R.id.vms;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			BaseFragment fragment = new HomeFragment();

			getSupportFragmentManager()
					.beginTransaction()
					.add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
					.addToBackStack(fragment.getClass().getSimpleName())
					.commit();
		}
	}
}
