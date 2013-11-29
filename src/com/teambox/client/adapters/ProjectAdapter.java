/*
 * Copyright 2013 Joan Fuentes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.teambox.client.adapters;

import java.util.List;

import com.teambox.client.R;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.ui.activities.MainActivity;
import com.teambox.client.ui.fragments.TasksListFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
/**
 * @author Joan Fuentes
 *
 */
 
public class ProjectAdapter extends BaseAdapter {
 
    private List<ProjectTable> mProjects;
    private int mLayoutResourceId;    
    private Context mContext; 
 
    public ProjectAdapter(Context context, int layoutResourceId, List<ProjectTable> projects) {
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mProjects = projects;
 
        // We must notify that we've changed the elements
        notifyDataSetChanged();
    }
 
    public int getCount() {
        return mProjects.size();
    }
 
    public Object getItem(int position) {
        return mProjects.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;
        final ProjectTable project = mProjects.get(position);

        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            
            holder = new TaskHolder();
            holder.name = (TextView)row.findViewById(R.id.textViewProjectName);
            holder.tasksCount = (Button)row.findViewById(R.id.buttonTasksCount);
            
            row.setTag(holder);
        }
        else
        {
            holder = (TaskHolder)row.getTag();
        }
        
        holder.name.setText(project.name);
        holder.tasksCount.setText(String.valueOf(project.tasksCount));
        holder.tasksCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				TasksListFragment fragment = new TasksListFragment();
				Bundle arguments = new Bundle();
				arguments.putLong(TasksListFragment.ARG_PROJECT_FILTER, project.projectId);
				fragment.setArguments(arguments);
				((MainActivity) mContext).getSupportFragmentManager()
					.beginTransaction()
						.replace(R.id.content_frame, fragment)
						.commit();	
				
				//Update the item selected in Drawer (and ActionBar)
				((MainActivity) mContext).updateSelectedItemInDrawer(MainActivity.FRAGMENT_TASKS);
			}
		});
        
        return row;
    }
    
    static class TaskHolder
    {
        TextView name;
        Button tasksCount;
    }
}