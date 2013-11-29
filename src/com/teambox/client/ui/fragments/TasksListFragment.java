package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.teambox.client.R;
import com.teambox.client.adapters.TaskAdapter;
import com.teambox.client.db.TaskTable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class TasksListFragment extends BaseListFragment{
    public static final String ARG_PROJECT_FILTER = "project_filter_id";
	
    private TaskAdapter taskAdapter;
	private List<TaskTable> infoToLoad = new ArrayList<TaskTable>();
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    taskAdapter = new TaskAdapter(getActivity(), R.layout.task_list_item, infoToLoad);
	    setListAdapter(taskAdapter);

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

		private void refreshInfoToShow(List<TaskTable> taskList){
			List<TaskTable> tasks = TaskTable.listAll(TaskTable.class);

			taskList.clear();
			for (int i = 0; i < tasks.size(); i++) {
				taskList.add (tasks.get(i));
			}
		}
	}
	
	
}
