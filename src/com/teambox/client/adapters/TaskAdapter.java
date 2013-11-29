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
import com.teambox.client.db.TaskTable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * @author Joan Fuentes
 *
 */
 
public class TaskAdapter extends BaseAdapter {
 
    private List<TaskTable> mTasks;
    private int mLayoutResourceId;    
    private Context mContext; 
 
    public TaskAdapter(Context context, int layoutResourceId, List<TaskTable> tasks) {
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mTasks    = tasks;
 
        // We must notify that we've changed the elements
        notifyDataSetChanged();
    }
 
    public int getCount() {
        return mTasks.size();
    }
 
    public Object getItem(int position) {
        return mTasks.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);
            
            holder = new TaskHolder();
            holder.name = (TextView)row.findViewById(R.id.textViewTaskName);
            holder.commentsCount = (TextView)row.findViewById(R.id.textViewTaskCommentsCount);
            holder.commentsCountTitle = (TextView)row.findViewById(R.id.textViewTaskCommentsCountTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (TaskHolder)row.getTag();
        }
        
        TaskTable task = mTasks.get(position);
        holder.name.setText(task.name);
        holder.commentsCount.setText(String.valueOf(task.commentsCount));
        holder.commentsCountTitle.setText(
        		(task.commentsCount>1?
        				mContext.getString(R.string.fragment_task_list_textview_task_list_comments_count_title_plural):
        					mContext.getString(R.string.fragment_task_list_textview_task_list_comments_count_title_singular)));
        
        return row;
    }
    
    static class TaskHolder
    {
        TextView name;
        TextView commentsCount;
        TextView commentsCountTitle;
    }
}