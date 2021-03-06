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
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.teambox.client.db.ProjectTable;

/**
 * ListAdapter used to adapter a list of projects in a dropdown element
 * 
 * @author Joan Fuentes
 * 
 */
public class DropDrownProjectsListAdapter extends BaseAdapter implements
		SpinnerAdapter {

	private List<ProjectTable> mProjects;
	private int mLayoutResourceId;
	private Context mContext;

	public DropDrownProjectsListAdapter(Context context, int layoutResourceId,
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

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View row = inflater.inflate(
				android.R.layout.simple_spinner_dropdown_item, parent, false);
		((TextView) row).setText(mProjects.get(position).name);

		return row;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TaskHolder holder = null;
		final ProjectTable project = mProjects.get(position);

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayoutResourceId, parent, false);

			holder = new TaskHolder();
			holder.name = (TextView) row;
			row.setTag(holder);
		} else {
			holder = (TaskHolder) row.getTag();
		}

		if (project.name != null) {
			holder.name.setText(project.name);
		}
		return row;
	}

	static class TaskHolder {
		TextView name;
	}
}