package com.teambox.client.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.adapters.DropDrownProjectsListAdapter;
import com.teambox.client.adapters.TaskAdapter;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;
import com.teambox.client.ui.activities.MainActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.view.View;
import android.widget.ListView;


public class TasksListFragment extends BaseListFragment {
    public static final String ARG_PROJECT_FILTER = "project_filter_id";
	private List<ProjectTable> infoToLoadAtDropDownListOfActionBar = new ArrayList<ProjectTable>();
	
    private TaskAdapter taskAdapter;
	private List<TaskTable> infoToLoad = new ArrayList<TaskTable>();
	private DropDrownProjectsListAdapter dropDownAdapter;
	private long projectIdToFilter;
	private SharedPreferences	sharedPreferences;
	private SharedPreferences.OnSharedPreferenceChangeListener	onSharedPreferenceChangeListener;
	
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

		projectIdToFilter = getArguments().getLong(ARG_PROJECT_FILTER);

		taskAdapter = new TaskAdapter(getActivity(), R.layout.list_item_task, infoToLoad);
	    setListAdapter(taskAdapter);

	    
	    setupActionBar();

	    new LoadDataInDropDownOfActionBarAsyncTask().execute();
	    //loadDataInViews();	    	    
	}


    @Override
    public void onResume() {
    	registerListenerToControlTaskStatusFilter();
    	super.onResume();
    }
    
    @Override
    public void onPause() {
		unregisterListenerToControlTaskStatusFilter();
    	super.onPause();
    }
    

	private void unregisterListenerToControlTaskStatusFilter() {
	    sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
	}

	private void registerListenerToControlTaskStatusFilter() {
		sharedPreferences = getActivity().getSharedPreferences(Application.APP_STORE_PREF_FILE, Activity.MODE_PRIVATE); 
	    onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
	    	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
	    		if(key.equalsIgnoreCase(Application.FILTER_TASK_STATUS_KEY))
	    		{
	    			new LoadDataInListViewAsyncTask().execute(projectIdToFilter,Application.getTaskStatusFilterValue(getActivity()));
	    		}
	    	}

	    };
	    sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
	}

    
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	
	@Override
	public void refreshDataInViews() {		
	    new LoadDataInListViewAsyncTask().execute(projectIdToFilter,Application.getTaskStatusFilterValue(getActivity()));
	    new LoadDataInDropDownOfActionBarAsyncTask().execute();
	}

	
	private class LoadDataInListViewAsyncTask extends AsyncTask<Long, Void, Void>{

		@Override
		protected Void doInBackground(Long... params) {
			Long projectIdToFilter = params[0];	// With projectIdToFilter==0 no filter by project is needed 
			Long taskStatusToFilter = params[1]; 	// With taskStatusToFilter==-1 no filter by task status is needed
			projectIdToFilter = (projectIdToFilter == 0 ? null : projectIdToFilter);
			taskStatusToFilter = (taskStatusToFilter == -1 ? null : taskStatusToFilter);
			
			refreshInfoToShow(infoToLoad, projectIdToFilter,taskStatusToFilter);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
		    
			notifyDataSetChangedToAdapter();
		    
		}		 
		
		private void refreshInfoToShow(List<TaskTable> taskList, Long projectIdToFilter,Long taskStatusToFilter){
			
			List<TaskTable> tasks = getFilteredTasks(projectIdToFilter, taskStatusToFilter);

			updateSourceData(taskList, tasks);
		}

		private void updateSourceData(List<TaskTable> taskList, List<TaskTable> tasks) {
			taskList.clear();
			for (int i = 0; i < tasks.size(); i++) {
				taskList.add (tasks.get(i));
			}
		}

		private List<TaskTable> getFilteredTasks(Long projectIdToFilter, Long taskStatusToFilter) {
			String filterWhereSQL = "";

			if (projectIdToFilter != null)
			{
				filterWhereSQL = filterWhereSQL + "project_id = " + projectIdToFilter.longValue() + " AND ";
			}
			
			if (taskStatusToFilter != null)
			{
				filterWhereSQL = filterWhereSQL + "status = " + taskStatusToFilter.longValue() + " AND ";
			}
			
			List<TaskTable> tasks;
			if(filterWhereSQL.length()>0)
			{
				filterWhereSQL = filterWhereSQL.substring(0, filterWhereSQL.length()-5);
				tasks = TaskTable.find(TaskTable.class,filterWhereSQL);
				
			}else{
				tasks = TaskTable.listAll(TaskTable.class);
			}
			
			return tasks;
		}

	}
	
	private class LoadDataInDropDownOfActionBarAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
	
			refreshInfoAtDropDownListOfActionBar(infoToLoadAtDropDownListOfActionBar);
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			((MainActivity) getActivity()).getSupportActionBar().setSelectedNavigationItem(getPositionInList(projectIdToFilter));
			
			dropDownAdapter.notifyDataSetChanged();

		}		 

		private void refreshInfoAtDropDownListOfActionBar(List<ProjectTable> projectList){
			List<ProjectTable> projects = ProjectTable.listAll(ProjectTable.class);

			projectList.clear();
			for (int i = 0; i < projects.size(); i++) {
				projectList.add (projects.get(i));
			}
			
			projectList.add(0, new ProjectTable(getActivity(),0,getString(R.string.dropdown_list_all_projects_element)));
		}
	}
	
	private void setupActionBar() {
	    // Set up the action bar to show a dropdown list.
	    final ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
	    
	    // Specify a SpinnerAdapter to populate the dropdown list.
	    dropDownAdapter = new DropDrownProjectsListAdapter(actionBar.getThemedContext(),
	        android.R.layout.simple_spinner_item, infoToLoadAtDropDownListOfActionBar);

	    OnNavigationListener listener = new OnNavigationListener() {
			
			@Override
			public boolean onNavigationItemSelected(int position, long id) {
					// When the Navigation mode is activated, the source of DropDownList (infoToLoadAtDropDownListOfActionBar) is empty, because it is populate in an asynctask.
					// This condition must be check to avoid error.
					if (isDropDownListLoaded())
					{
						projectIdToFilter = infoToLoadAtDropDownListOfActionBar.get(position).projectId;
						new LoadDataInListViewAsyncTask().execute(projectIdToFilter,Application.getTaskStatusFilterValue(getActivity()));
					}
				return true;
			}
		};

		
	    // Set up the dropdown list navigation in the action bar.
	    actionBar.setListNavigationCallbacks(dropDownAdapter, listener);

	    // use getActionBar().getThemedContext() to ensure
	    // that the text color is always appropriate for the action bar
	    // background rather than the activity background.

	}
	
	String[] getProjectNames(List<ProjectTable> projects){
		String[] result = new String[projects.size()];
		for (int i = 0; i < projects.size(); i++) {
			result[i] = projects.get(i).name;
		}
		return result;
	}
	
	public int getPositionInList(long projectId){
		int position = -1;
		int i = 0;
		while ((i < infoToLoadAtDropDownListOfActionBar.size()) && (position < 0)) {
			if(infoToLoadAtDropDownListOfActionBar.get(i).projectId==projectId)
			{
				position = i;
			}
			i++;
		}
		
		return position;
	}
	
	public boolean isDropDownListLoaded(){
		return (infoToLoadAtDropDownListOfActionBar.size()>0 ? Boolean.TRUE : Boolean.FALSE);
	}
	
}
