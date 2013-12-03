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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teambox.client.R;
import com.teambox.client.db.ProjectTable;

/**
 * ListAdapter used to adapter a list of projects in a listView
 * 
 * @author Joan Fuentes
 * 
 */
public class ProjectListAdapter extends BaseAdapter {

	private List<ProjectTable> mProjects;
	private int mLayoutResourceId;
	private Context mContext;

	public ProjectListAdapter(Context context, int layoutResourceId,
			List<ProjectTable> projects) {
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

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);

			holder = new TaskHolder();
			holder.name = (TextView) row.findViewById(R.id.textViewProjectName);
			holder.tasksCountNormal = (TextView) row
					.findViewById(R.id.TextViewTasksCounterNormal);
			holder.tasksCountUrgent = (TextView) row
					.findViewById(R.id.TextViewTasksCounterUrgent);

			row.setTag(holder);
		} else {
			holder = (TaskHolder) row.getTag();
		}

		setValuesInViews(holder, project);

		return row;
	}

	private void setValuesInViews(TaskHolder holder, ProjectTable project) {
		holder.name.setText(project.name);

		holder.tasksCountNormal.setText(String
				.valueOf(project.tasksCountNormal));
		if (project.tasksCountNormal > 0) {
			holder.tasksCountNormal.setVisibility(View.VISIBLE);
		} else {
			holder.tasksCountNormal.setVisibility(View.GONE);
		}

		holder.tasksCountUrgent.setText(String
				.valueOf(project.tasksCountUrgent));
		if (project.tasksCountUrgent > 0) {
			holder.tasksCountUrgent.setVisibility(View.VISIBLE);
			holder.tasksCountUrgent.setText(String
					.valueOf(project.tasksCountUrgent));
		} else {
			holder.tasksCountUrgent.setVisibility(View.GONE);
		}
	}

	static class TaskHolder {
		TextView name;
		TextView tasksCountNormal;
		TextView tasksCountUrgent;
	}
}