package com.teambox.client.ui.fragments;

import android.os.Bundle;
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Set radioGroup with value stores in SharedPreferences
		int positionInRadioGroup = Application
				.getTaskStatusFilterSelection(getActivity()) - 1;
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

		String[] taskStatusNames = getResources().getStringArray(
				R.array.task_status_names_array);

		for (int i = 0; i < taskStatusNames.length; i++) {
			RadioButton radioButton = new RadioButton(getActivity());
			radioButton.setText(taskStatusNames[i]);
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
						Application.setTaskStatusFilterSelection(getActivity(),
								positionInRadioGroup + 1);

						// If it is a mobile, this fragment is charged at full
						// screen. Return to the previous screen
						if (!Utilities.isDeviceATablet(getActivity())) {
							getFragmentManager().popBackStack();
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
