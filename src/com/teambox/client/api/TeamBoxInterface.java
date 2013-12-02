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

package com.teambox.client.api;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Interface used by Retrofit to recognize requests (GET, POST, ...) and know to
 * which object should parse the resulting JSON.
 * 
 * @author Joan Fuentes
 * 
 */
public interface TeamBoxInterface {

	/**
	 * Download and parse the account info in a Account object
	 * 
	 * @param token
	 *            OAuth access token
	 * @return
	 */
	@GET("/account")
	Account account(@Query("access_token") String token);

	/**
	 * Download and parse the projects info in a list of Project objects
	 * 
	 * @param token
	 *            OAuth access token
	 * @return
	 */
	@GET("/projects")
	List<Project> projects(@Query("access_token") String token);

	/**
	 * Download and parse the tasks info in a list of Task objects
	 * 
	 * @param token
	 *            OAuth access token
	 * @param scope
	 *            Used to filter tasks by status. Must be one of: all, active,
	 *            archived, unarchived, due_today, overdue, unassigned_date,
	 *            urgent, upcoming, with_date.
	 * @param count
	 *            Number of tasks to retrieve. 0 means all matched.
	 * @return
	 */
	@GET("/tasks")
	List<Task> tasks(@Query("access_token") String token,
			@Query("scope") String scope, @Query("count") String count);

}