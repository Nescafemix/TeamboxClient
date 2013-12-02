package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ListView;

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
			AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			refreshInfoToShow(mInfoToLoad);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			notifyDataSetChangedToAdapter();
		}

		private void refreshInfoToShow(List<ProjectTable> projectList) {
			List<ProjectTable> projects = ProjectTable
					.listAll(ProjectTable.class);

			projectList.clear();
			for (int i = 0; i < projects.size(); i++) {
				ProjectTable project = projects.get(i);
				project.tasksCountNew = TaskTable
						.find(TaskTable.class,
								"project_id = " + project.projectId
										+ " AND status = 0").size();
				project.tasksCountOpen = TaskTable
						.find(TaskTable.class,
								"project_id = " + project.projectId
										+ " AND status = 1").size();
				project.tasksCountHold = TaskTable
						.find(TaskTable.class,
								"project_id = " + project.projectId
										+ " AND status = 2").size();
				project.tasksCountResolved = TaskTable
						.find(TaskTable.class,
								"project_id = " + project.projectId
										+ " AND status = 3").size();
				project.tasksCountRejected = TaskTable
						.find(TaskTable.class,
								"project_id = " + project.projectId
										+ " AND status = 4").size();
				projectList.add(projects.get(i));

				if (isCancelled())
					break;
			}
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

		setupActionBar();

		refreshDataInViews();

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
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
