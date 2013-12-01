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

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.ui.activities.MainActivity;

import android.app.Activity;
import android.content.Context;
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
    }
 
    public int getCount() {
        return mProjects.size();
    }
 
    public Object getItem(int position) {
        return mProjects.get(position);
    }
 
    public long getItemId(int position) {
        return position;
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
            holder.tasksCountNew = (Button)row.findViewById(R.id.buttonTasksCountNew);
            holder.tasksCountOpen = (Button)row.findViewById(R.id.buttonTasksCountOpen);
            holder.tasksCountHold = (Button)row.findViewById(R.id.buttonTasksCountHold);
            holder.tasksCountResolved = (Button)row.findViewById(R.id.buttonTasksCountResolved);
            holder.tasksCountRejected = (Button)row.findViewById(R.id.buttonTasksCountRejected);
            
            row.setTag(holder);
        }
        else
        {
            holder = (TaskHolder)row.getTag();
        }
        
        holder.name.setText(project.name);
        holder.tasksCountNew.setText(String.valueOf(project.tasksCountNew));
        holder.tasksCountOpen.setText(String.valueOf(project.tasksCountOpen));
        holder.tasksCountHold.setText(String.valueOf(project.tasksCountHold));
        holder.tasksCountResolved.setText(String.valueOf(project.tasksCountResolved));
        holder.tasksCountRejected.setText(String.valueOf(project.tasksCountRejected));
        holder.tasksCountNew.setOnClickListener(getOnClickListener(project,2));
        holder.tasksCountOpen.setOnClickListener(getOnClickListener(project,3));
        holder.tasksCountHold.setOnClickListener(getOnClickListener(project,4));
        holder.tasksCountResolved.setOnClickListener(getOnClickListener(project,5));
        holder.tasksCountRejected.setOnClickListener(getOnClickListener(project,5));
        
        return row;
    }

	private OnClickListener getOnClickListener(final ProjectTable project, final int taskStatusSelectedPositionInArray) {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Application.setTaskStatusFilterSelection(mContext, taskStatusSelectedPositionInArray);
				
				((MainActivity) mContext).loadNewFragments(MainActivity.FRAGMENT_TASKS, project.projectId);
				
			}
		};
	}
    
    static class TaskHolder
    {
        TextView name;
        Button tasksCountNew;
        Button tasksCountOpen;
        Button tasksCountHold;
        Button tasksCountResolved;
        Button tasksCountRejected;
        
    }
}