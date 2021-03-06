package com.teambox.client.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.ui.activities.MainActivity;

/**
 * Fragment which contains a radiogroup(selector) to filter tasks by status.
 * Changes on selection are stored in SharedPreferences.
 * 
 * @author Joan Fuentes
 * 
 */
public class FilterFragment extends BaseFragment {

	RadioGroup mRadioGroupFilter;
	long mProjectIdToFilter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (getArguments() != null) {
			if (!Utilities.isDeviceATablet(getActivity())) {
				mProjectIdToFilter = getArguments().getLong(ARG_PROJECT_FILTER,
						0);
			}
		}

		// Set radioGroup with value stored in SharedPreferences
		int positionInRadioGroup = Application
				.getTaskPriorityFilterSelection(getActivity()) - 1;
		mRadioGroupFilter.check(mRadioGroupFilter.getChildAt(
				positionInRadioGroup).getId());

		if (!Utilities.isDeviceATablet(getActivity())) {
			setupActionBar();
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater
				.inflate(R.layout.fragment_filter, container, false);

		mRadioGroupFilter = (RadioGroup) view
				.findViewById(R.id.radioGroupFilter);

		String[] taskPriorityNames = getResources().getStringArray(
				R.array.task_priority_names_array);

		for (int i = 0; i < taskPriorityNames.length; i++) {
			RadioButton radioButton = new RadioButton(getActivity());
			radioButton.setText(taskPriorityNames[i]);
			mRadioGroupFilter.addView(radioButton);
		}

		return view;
	}

	@Override
	public void onPause() {
		unregisterOnCheckedChangeListenerOfRadioGroup();

		super.onPause();
	}

	@Override
	public void onResume() {
		registerOnCheckedChangeListenerOfRadioGroup();

		super.onResume();
	}

	@Override
	public void refreshDataInViews() {
		// No data sensitive of being updated
	}

	private void registerOnCheckedChangeListenerOfRadioGroup() {
		mRadioGroupFilter
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						int positionInRadioGroup = group.indexOfChild(group
								.findViewById(checkedId));
						Application.setTaskPriorityFilterSelection(
								getActivity(), positionInRadioGroup + 1);

						// If it is a mobile, this fragment is charged at full
						// screen. Return to the previous screen
						// with getFragmentManager().popBackStack() we could
						// reload the previous fragment (if it was saved in
						// stack)
						// but me are going to create a new fragment with same
						// values to avoid "flick effect";
						if (!Utilities.isDeviceATablet(getActivity())) {
							Fragment fragment = new TasksListFragment();
							FragmentManager fragmentManager = getActivity()
									.getSupportFragmentManager();
							Bundle bundle = new Bundle();
							bundle.putLong(ARG_PROJECT_FILTER,
									mProjectIdToFilter);
							fragment.setArguments(bundle);
							fragmentManager.beginTransaction()
									.replace(R.id.content_frame, fragment)
									.commit();
						}
					}
				});
	}

	private void setupActionBar() {
		final ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	private void unregisterOnCheckedChangeListenerOfRadioGroup() {
		mRadioGroupFilter.setOnCheckedChangeListener(null);
	}

}
