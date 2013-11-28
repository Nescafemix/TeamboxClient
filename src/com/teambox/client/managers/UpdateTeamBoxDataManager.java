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

import android.util.Log;

import com.teambox.client.api.Account;
import com.teambox.client.api.Project;
import com.teambox.client.api.ServiceInfo;
import com.teambox.client.api.TeamBoxInterface;

public class UpdateTeamBoxDataManager {

	private RestAdapter mRestAdapter;
	private TeamBoxInterface mTeambox;
	private String mToken;
	
	public UpdateTeamBoxDataManager(String token){
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
	    // TODO
		Log.v("TeamBoxClient", "Account from " + account.first_name + " " + account.last_name + " updated in BBDD (it's not true)");
	    
		return false;
		
	}
	
	public boolean updateProjects()
	{
	    // Fetch a list of the projects of this user.
	    List<Project> projects = mTeambox.projects(mToken);
	    
	    // Update data in Database
	    // TODO
		Log.v("TeamBoxClient", "Projects updated in BBDD (it's not true). First: " + projects.get(0).name + ". Last: " + projects.get(projects.size()-1).name);

	    return false;
	}
}
