package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.view.View;
import android.widget.ListView;

import com.teambox.client.Application;
import com.teambox.client.R;
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
			AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			refreshInfoAtDropDownListOfActionBar(mInfoToLoadAtDropDownListOfActionBar);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			((MainActivity) getActivity()).getSupportActionBar()
					.setSelectedNavigationItem(
							getPositionInList(mProjectIdToFilter));

			mDropDownAdapter.notifyDataSetChanged();

		}

		private void refreshInfoAtDropDownListOfActionBar(
				List<ProjectTable> projectList) {
			List<ProjectTable> projects = ProjectTable
					.listAll(ProjectTable.class);

			projectList.clear();
			for (int i = 0; i < projects.size(); i++) {
				projectList.add(projects.get(i));

				if (isCancelled())
					break;
			}

			projectList.add(0, new ProjectTable(getActivity(), 0,
					getString(R.string.dropdown_list_all_projects_element)));
		}
	}

	private class LoadDataInListViewAsyncTask extends
			AsyncTask<Long, Void, Void> {

		@Override
		protected Void doInBackground(Long... params) {
			Long projectIdToFilter = params[0]; // With projectIdToFilter==0 no
												// filter by project is needed
			Long taskStatusToFilter = params[1]; // With taskStatusToFilter==-1
													// no filter by task status
													// is needed
			projectIdToFilter = (projectIdToFilter == 0 ? null
					: projectIdToFilter);
			taskStatusToFilter = (taskStatusToFilter == -1 ? null
					: taskStatusToFilter);

			refreshInfoToShow(mInfoToLoad, projectIdToFilter, taskStatusToFilter);

			return null;
		}

		private List<TaskTable> getFilteredTasks(Long projectIdToFilter,
				Long taskStatusToFilter) {
			String filterWhereSQL = "";

			if (projectIdToFilter != null) {
				filterWhereSQL = filterWhereSQL + "project_id = "
						+ projectIdToFilter.longValue() + " AND ";
			}

			if (taskStatusToFilter != null) {
				filterWhereSQL = filterWhereSQL + "status = "
						+ taskStatusToFilter.longValue() + " AND ";
			}

			List<TaskTable> tasks;
			if (filterWhereSQL.length() > 0) {
				filterWhereSQL = filterWhereSQL.substring(0,
						filterWhereSQL.length() - 5);
				tasks = TaskTable.find(TaskTable.class, filterWhereSQL);

			} else {
				tasks = TaskTable.listAll(TaskTable.class);
			}

			return tasks;
		}

		@Override
		protected void onPostExecute(Void result) {

			notifyDataSetChangedToAdapter();

		}

		private void refreshInfoToShow(List<TaskTable> taskList,
				Long projectIdToFilter, Long taskStatusToFilter) {

			List<TaskTable> tasks = getFilteredTasks(projectIdToFilter,
					taskStatusToFilter);

			updateSourceData(taskList, tasks);
		}

		private void updateSourceData(List<TaskTable> taskList,
				List<TaskTable> tasks) {
			taskList.clear();
			for (int i = 0; i < tasks.size(); i++) {
				taskList.add(tasks.get(i));

				if (isCancelled())
					break;
			}
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

		setupActionBar();

		mLoadDataInDropDownOfActionBarAsyncTask = new LoadDataInDropDownOfActionBarAsyncTask();
		mLoadDataInDropDownOfActionBarAsyncTask.execute();

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

		if(mLoadDataInDropDownOfActionBarAsyncTask != null)
			mLoadDataInDropDownOfActionBarAsyncTask.cancel(true);
		
		if(mLoadDataInListViewAsyncTask != null)
			mLoadDataInListViewAsyncTask.cancel(true);
		
		super.onStop();
	}

	@Override
	public void refreshDataInViews() {

		mLoadDataInListViewAsyncTask = new LoadDataInListViewAsyncTask();
		mLoadDataInListViewAsyncTask.execute(mProjectIdToFilter,
				Application.getTaskStatusFilterValue(getActivity()));

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
					mLoadDataInListViewAsyncTask.execute(mProjectIdToFilter, Application
									.getTaskStatusFilterValue(getActivity()));
				}
			}

		};
		mSharedPreferences
				.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
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
					mLoadDataInListViewAsyncTask.execute(mProjectIdToFilter, Application
									.getTaskStatusFilterValue(getActivity()));
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
