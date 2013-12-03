package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.Utilities;
import com.teambox.client.adapters.DropDrownProjectsListAdapter;
import com.teambox.client.adapters.TaskListAdapter;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;
import com.teambox.client.ui.activities.MainActivity;

/**
 * Fragment which contains and manages a list of tasks.
 * 
 * @author Joan Fuentes
 * 
 */
public class TasksListFragment extends BaseListFragment {
	private class LoadDataInDropDownOfActionBarAsyncTask extends
			AsyncTask<Void, List<ProjectTable>, List<ProjectTable>> {

		@Override
		protected List<ProjectTable> doInBackground(Void... params) {

			return getNewDataToPopulateTheDropDownListOfActionBar();

		}

		@Override
		protected void onPostExecute(List<ProjectTable> projects) {

			mInfoToLoadAtDropDownListOfActionBar.clear();
			mInfoToLoadAtDropDownListOfActionBar.addAll(projects);

			((MainActivity) getActivity()).getSupportActionBar()
					.setSelectedNavigationItem(
							getPositionInList(mProjectIdToFilter));

			mDropDownAdapter.notifyDataSetChanged();

		}

		private List<ProjectTable> getNewDataToPopulateTheDropDownListOfActionBar() {
			List<ProjectTable> projects = ProjectTable
					.listAll(ProjectTable.class);

			projects.add(0, new ProjectTable(getActivity(), 0,
					getString(R.string.dropdown_list_all_projects_element)));

			return projects;
		}
	}

	private class LoadDataInListViewAsyncTask extends
			AsyncTask<Long, List<TaskTable>, List<TaskTable>> {

		@Override
		protected List<TaskTable> doInBackground(Long... params) {
			Long projectIdToFilter = params[0]; // With projectIdToFilter==0 no
												// filter by project is needed
			Long taskPriorityToFilter = params[1]; // With
													// taskStatusToFilter==-1
													// no filter by task
													// priority
													// is needed
			projectIdToFilter = (projectIdToFilter == 0 ? null
					: projectIdToFilter);
			taskPriorityToFilter = (taskPriorityToFilter == -1 ? null
					: taskPriorityToFilter);

			return getNewDataToShow(projectIdToFilter, taskPriorityToFilter);
		}

		private List<TaskTable> getNewDataToShow(Long projectIdToFilter,
				Long taskPriorityToFilter) {
			String filterWhereSQL = "";

			if (projectIdToFilter != null) {
				filterWhereSQL = filterWhereSQL + "project_id = "
						+ projectIdToFilter.longValue() + " AND ";
			}

			if (taskPriorityToFilter != null) {
				filterWhereSQL = filterWhereSQL
						+ "urgent = "
						+ (taskPriorityToFilter.compareTo((long) 0) == 0 ? "'false'"
								: "'true'") + " AND ";
			}

			List<TaskTable> tasks;
			if (filterWhereSQL.length() > 0) {
				filterWhereSQL = filterWhereSQL.substring(0,
						filterWhereSQL.length() - 5);
				tasks = TaskTable.find(TaskTable.class, filterWhereSQL);
				tasks = TaskTable.find(TaskTable.class, filterWhereSQL, null,
						null, "urgent DESC", null);

			} else {
				tasks = TaskTable.find(TaskTable.class, null, null, null,
						"urgent DESC", null);
			}

			return tasks;
		}

		@Override
		protected void onPostExecute(List<TaskTable> tasks) {

			mInfoToLoad.clear();
			mInfoToLoad.addAll(tasks);

			mTaskAdapter.notifyDataSetChanged();

		}

	}

	private List<ProjectTable> mInfoToLoadAtDropDownListOfActionBar = new ArrayList<ProjectTable>();
	private TaskListAdapter mTaskAdapter;
	private List<TaskTable> mInfoToLoad = new ArrayList<TaskTable>();
	private DropDrownProjectsListAdapter mDropDownAdapter;
	private long mProjectIdToFilter;

	private SharedPreferences mSharedPreferences;
	private SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener;

	private LoadDataInDropDownOfActionBarAsyncTask mLoadDataInDropDownOfActionBarAsyncTask;
	private LoadDataInListViewAsyncTask mLoadDataInListViewAsyncTask;

	public int getPositionInList(long projectId) {
		int position = -1;
		int i = 0;
		while ((i < mInfoToLoadAtDropDownListOfActionBar.size())
				&& (position < 0)) {
			if (mInfoToLoadAtDropDownListOfActionBar.get(i).projectId == projectId) {
				position = i;
			}
			i++;
		}

		return position;
	}

	String[] getProjectNames(List<ProjectTable> projects) {
		String[] result = new String[projects.size()];
		for (int i = 0; i < projects.size(); i++) {
			result[i] = projects.get(i).name;
		}
		return result;
	}

	public boolean isDropDownListLoaded() {
		return (mInfoToLoadAtDropDownListOfActionBar.size() > 0 ? Boolean.TRUE
				: Boolean.FALSE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mProjectIdToFilter = getArguments().getLong(ARG_PROJECT_FILTER);

		mTaskAdapter = new TaskListAdapter(getActivity(),
				R.layout.list_item_task, mInfoToLoad);
		setListAdapter(mTaskAdapter);

		setEmptyText(getString(R.string.fragment_task_list_no_elements));

		setupActionBar();

		mLoadDataInDropDownOfActionBarAsyncTask = new LoadDataInDropDownOfActionBarAsyncTask();
		mLoadDataInDropDownOfActionBarAsyncTask.execute();

		// This screen has an element specific at the menu.
		setHasOptionsMenu(true);

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	@Override
	public void onPause() {
		unregisterListenerToControlTaskStatusFilter();
		super.onPause();
	}

	@Override
	public void onResume() {
		registerListenerToControlTaskStatusFilter();
		super.onResume();
	}

	@Override
	public void onStop() {

		if (mLoadDataInDropDownOfActionBarAsyncTask != null)
			mLoadDataInDropDownOfActionBarAsyncTask.cancel(true);

		if (mLoadDataInListViewAsyncTask != null)
			mLoadDataInListViewAsyncTask.cancel(true);

		super.onStop();
	}

	@Override
	public void refreshDataInViews() {

		mLoadDataInListViewAsyncTask = new LoadDataInListViewAsyncTask();
		mLoadDataInListViewAsyncTask.execute(mProjectIdToFilter,
				Application.getTaskPriorityFilterValue(getActivity()));

		mLoadDataInDropDownOfActionBarAsyncTask = new LoadDataInDropDownOfActionBarAsyncTask();
		mLoadDataInDropDownOfActionBarAsyncTask.execute();
	}

	private void registerListenerToControlTaskStatusFilter() {
		mSharedPreferences = getActivity().getSharedPreferences(
				Application.APP_STORE_PREF_FILE, Activity.MODE_PRIVATE);
		mOnSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(SharedPreferences prefs,
					String key) {
				if (key.equalsIgnoreCase(Application.FILTER_TASK_STATUS_KEY)) {
					mLoadDataInListViewAsyncTask = new LoadDataInListViewAsyncTask();
					mLoadDataInListViewAsyncTask.execute(mProjectIdToFilter,
							Application
									.getTaskPriorityFilterValue(getActivity()));
				}
			}

		};
		mSharedPreferences
				.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		if (!Utilities.isDeviceATablet(getActivity())) {
			menu.findItem(R.id.action_filter_tasks_by_status).setVisible(
					Boolean.TRUE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_filter_tasks_by_status:
			openFilterScreen();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openFilterScreen() {
		// We must pass ProjectIdToFilter to can reload a fragment like the
		// current fragment
		Fragment fragment = new FilterFragment();
		FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();
		Bundle bundle = new Bundle();
		bundle.putLong(ARG_PROJECT_FILTER, mProjectIdToFilter);
		fragment.setArguments(bundle);
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
	}

	private void setupActionBar() {
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Specify a SpinnerAdapter to populate the dropdown list.
		mDropDownAdapter = new DropDrownProjectsListAdapter(
				actionBar.getThemedContext(),
				android.R.layout.simple_spinner_item,
				mInfoToLoadAtDropDownListOfActionBar);

		OnNavigationListener listener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int position, long id) {
				// When the Navigation mode is activated, the source of
				// DropDownList (mInfoToLoadAtDropDownListOfActionBar) is empty,
				// because it is populate in an asynctask.
				// This condition must be check to avoid error.
				if (isDropDownListLoaded()) {
					mProjectIdToFilter = mInfoToLoadAtDropDownListOfActionBar
							.get(position).projectId;
					mLoadDataInListViewAsyncTask = new LoadDataInListViewAsyncTask();
					mLoadDataInListViewAsyncTask.execute(mProjectIdToFilter,
							Application
									.getTaskPriorityFilterValue(getActivity()));
				}
				return true;
			}
		};

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(mDropDownAdapter, listener);

		// use getActionBar().getThemedContext() to ensure
		// that the text color is always appropriate for the action bar
		// background rather than the activity background.

	}

	private void unregisterListenerToControlTaskStatusFilter() {
		mSharedPreferences
				.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
	}

}
