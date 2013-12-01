package com.teambox.client.ui.fragments;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.db.AccountTable;
import com.teambox.client.ui.activities.MainActivity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FilterFragment extends BaseFragment{

	RadioGroup radioGroupFilter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_filter, container, false);
		
		radioGroupFilter = (RadioGroup) view.findViewById(R.id.radioGroupFilter);

		// TODO : mount radiobuttons
		
		String[] taskStatusNames = getResources().getStringArray(R.array.task_status_names_array);
		String[] taskStatusValues = getResources().getStringArray(R.array.task_status_values_array);

		for(int i=0; i<taskStatusNames.length; i++){
	        RadioButton radioButton  = new RadioButton(getActivity());
	        radioButton.setText(taskStatusNames[i]);
	        radioButton.setTag(radioButton.getId(), taskStatusValues[i]);
			radioGroupFilter.addView(radioButton);
	    }
		
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Set radioGroup with value stores in SharedPreferences
		int positionInRadioGroup = Application.getTaskStatusFilterSelection(getActivity())-1;
		radioGroupFilter.check(radioGroupFilter.getChildAt(positionInRadioGroup).getId());
		
		if(!Utilities.isDeviceATablet(getActivity()));
		{
			setupActionBar();
		}
		
	}

	private void registerOnCheckedChangeListenerOfRadioGroup() {
		radioGroupFilter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int positionInRadioGroup = group.indexOfChild(group.findViewById(checkedId));
				Application.setTaskStatusFilterSelection(getActivity(),positionInRadioGroup+1);
				
			}
		});
	}

	@Override
	public void onResume() {
		registerOnCheckedChangeListenerOfRadioGroup();

		super.onResume();
	}
	
	@Override
	public void onPause() {
		unregisterOnCheckedChangeListenerOfRadioGroup();

		super.onPause();
	}

	
	/**
	 * 
	 */
	private void unregisterOnCheckedChangeListenerOfRadioGroup() {
		radioGroupFilter.setOnCheckedChangeListener(null);		
	}

	@Override
	public void refreshDataInViews() {

		
	}

	private void setupActionBar() {
		final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayShowTitleEnabled(true);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	

	/**
	 * @param account
	 */
	public void refreshInfoInViews(AccountTable account) {
	}

	
}
