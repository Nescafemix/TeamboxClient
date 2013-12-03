package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ListView;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.adapters.ProjectListAdapter;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;
import com.teambox.client.ui.activities.MainActivity;

/**
 * Fragment which contains and manages a list of projects
 * 
 * @author Joan Fuentes
 * 
 */
public class ProjectsListFragment extends BaseListFragment {
	private class LoadDataInListViewAsyncTask extends
			AsyncTask<Void, List<ProjectTable>, List<ProjectTable>> {

		@Override
		protected List<ProjectTable> doInBackground(Void... params) {

			return getNewDataToShow();
		}

		@Override
		protected void onPostExecute(List<ProjectTable> projects) {

			mInfoToLoad.clear();
			mInfoToLoad.addAll(projects);

			mProjectAdapter.notifyDataSetChanged();
		}

		private List<ProjectTable> getNewDataToShow() {
			List<ProjectTable> projects = ProjectTable
					.listAll(ProjectTable.class);

			for (int i = 0; i < projects.size(); i++) {

				ProjectTable project = projects.get(i);

				project.tasksCountNormal = TaskTable.find(
						TaskTable.class,
						"project_id = " + project.projectId
								+ " AND status = 1 AND urgent ='false'").size();
				project.tasksCountUrgent = TaskTable.find(
						TaskTable.class,
						"project_id = " + project.projectId
								+ " AND status = 1 AND urgent ='true'").size();
				if (isCancelled())
					break;
			}
			return projects;
		}
	}

	private ProjectListAdapter mProjectAdapter;
	private List<ProjectTable> mInfoToLoad = new ArrayList<ProjectTable>();
	private LoadDataInListViewAsyncTask mLoadDataInListViewAsyncTask;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mProjectAdapter = new ProjectListAdapter(getActivity(),
				R.layout.list_item_project, mInfoToLoad);
		setListAdapter(mProjectAdapter);

		setEmptyText(getString(R.string.fragment_projects_list_no_elements));

		setupActionBar();

		refreshDataInViews();

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		((MainActivity) getActivity())
				.loadNewFragments(Application.FRAGMENT_TASKS,
						mInfoToLoad.get(position).projectId);

		((MainActivity) getActivity())
				.updateSelectedItemInDrawer(Application.FRAGMENT_TASKS);

	}

	@Override
	public void onStop() {

		if (mLoadDataInListViewAsyncTask != null)
			mLoadDataInListViewAsyncTask.cancel(true);

		super.onStop();
	}

	@Override
	public void refreshDataInViews() {
		mLoadDataInListViewAsyncTask = new LoadDataInListViewAsyncTask();
		mLoadDataInListViewAsyncTask.execute();
	}

	private void setupActionBar() {
		final ActionBar actionBar = ((MainActivity) getActivity())
				.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

	}

}
