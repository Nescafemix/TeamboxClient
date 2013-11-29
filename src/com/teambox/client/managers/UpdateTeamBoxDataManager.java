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

package com.teambox.client.managers;

import java.util.List;

import retrofit.RestAdapter;

import android.content.Context;
import android.util.Log;

import com.teambox.client.api.Account;
import com.teambox.client.api.Project;
import com.teambox.client.api.ServiceInfo;
import com.teambox.client.api.Task;
import com.teambox.client.api.TeamBoxInterface;
import com.teambox.client.db.AccountTable;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;

public class UpdateTeamBoxDataManager {

	private RestAdapter mRestAdapter;
	private TeamBoxInterface mTeambox;
	private String mToken;
	private Context mContext;
	
	public UpdateTeamBoxDataManager(Context context, String token){
		mContext = context;
		mToken = token;
		
		// Create a very simple REST adapter which points the Teambox API endpoint.
	    mRestAdapter = new RestAdapter.Builder()
	    	.setServer(ServiceInfo.API_URL)
	        .build();

	    // Create an instance of our TeamBox API interface.
	    mTeambox = mRestAdapter.create(TeamBoxInterface.class);
	}
	
	
	public boolean updateAccount()
	{
	    // Fetch the account info.
		Account account = mTeambox.account(mToken);
		
	    // Update data in Database
		AccountTable.deleteAll(AccountTable.class);
		AccountTable accountRow = new AccountTable(
				mContext,
				account.id,
				account.first_name,
				account.last_name,
				account.email,
				account.username,
				account.avatar_url,
				account.micro_avatar_url);
	    accountRow.save();
		
		Log.v("TeamBoxClient", "Account from " + account.first_name + " " + account.last_name + " updated in DB");
	    
		return false;
		
	}
	
	public boolean updateProjects()
	{
	    // Fetch a list of the projects of this user.
	    List<Project> projects = mTeambox.projects(mToken);
	    
	    // Update data in Database
	    ProjectTable.deleteAll(ProjectTable.class);
	    for (int i = 0; i < projects.size(); i++) {
			Project project = projects.get(i);
			
			ProjectTable projectRow = new ProjectTable(
					mContext, 
					project.type, 
					project.created_at, 
					project.updated_at,
					project.id,
					project.permalink,
					project.organization_id,
					project.archived,
					project.name,
					project.tracks_time,
					project.publish_pages);
			projectRow.save();
		}
	    
		Log.v("TeamBoxClient", "Projects updated in DB. First: " + projects.get(0).name + ". Last: " + projects.get(projects.size()-1).name);

	    return false;
	}
	
	public boolean updateTasks()
	{
	    // Fetch a list of the tasks of this user.
	    List<Task> tasks = mTeambox.tasks(mToken,"all","0");
	    
	    // Update data in Database
	    TaskTable.deleteAll(TaskTable.class);
	    for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			
			TaskTable taskRow = new TaskTable(
					mContext,
					task.type,
					task.created_at,
					task.updated_at,
					task.id,
					task.name,
					task.task_list_id,
					task.comments_count,
					task.assigned_id,
					task.status,
					task.is_private,
					task.project_id,
					task.urgent,
					task.hidden_comments_count,
					task.user_id,
					task.position,
					task.last_activity_id,
					task.deleted,
					task.due_on);
			taskRow.save();
		}
	    
		Log.v("TeamBoxClient", "Tasks updated in DB. First: " + tasks.get(0).name + ". Last: " + tasks.get(tasks.size()-1).name);

	    return false;
	}
}
