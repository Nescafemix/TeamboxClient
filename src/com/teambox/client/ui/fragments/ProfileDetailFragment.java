package com.teambox.client.ui.fragments;

import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.ui.activities.MainActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileDetailFragment extends BaseFragment{


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile_detail, container, false);
		
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
				
		if(!Utilities.isDeviceATablet(getActivity()));
		{
			setupActionBar();
		}
	}


	@Override
	public void onResume() {

		super.onResume();
	}
	
	@Override
	public void onPause() {

		super.onPause();
	}

	
	@Override
	public void refreshDataInViews() {

		
	}

	private void setupActionBar() {
		final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayShowTitleEnabled(true);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}
}
