package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.teambox.client.R;
import com.teambox.client.adapters.ProjectAdapter;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class ProjectsListFragment extends BaseListFragment{
    private ProjectAdapter projectAdapter;
	private List<ProjectTable> infoToLoad = new ArrayList<ProjectTable>();
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	    projectAdapter = new ProjectAdapter(getActivity(), R.layout.project_list_item, infoToLoad);
	    setListAdapter(projectAdapter);

	    loadDataInViews();	    
	}

    
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	
	@Override
	public void loadDataInViews() {		
	    new LoadDataInViewsAsyncTask().execute();
	}

	
	private class LoadDataInViewsAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
	
			refreshInfoToShow(infoToLoad);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
		    
			BaseListFragment fragment = (BaseListFragment) getFragmentManager().findFragmentById(R.id.content_frame);
		    ((BaseAdapter) fragment.getListAdapter()).notifyDataSetChanged();
		    
		}		 

		private void refreshInfoToShow(List<ProjectTable> projectList){
			List<ProjectTable> projects = ProjectTable.listAll(ProjectTable.class);

			projectList.clear();
			for (int i = 0; i < projects.size(); i++) {
				ProjectTable project = projects.get(i);
				project.tasksCount = TaskTable.find(TaskTable.class, "project_id = ?", String.valueOf(project.projectId)).size();
				projectList.add (projects.get(i));
			}
		}
	}
	
	
}
