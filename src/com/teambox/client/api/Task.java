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

public class Task {
	
	public String type;
	public String created_at;
	public String updated_at;
	public long id;
	public String name;
	public long task_list_id;
	public int comments_count;
	public long assigned_id; //
	public int status;
	public boolean is_private;
	public long project_id;
	public boolean urgent;
	public int hidden_comments_count;
	public long user_id;
	public int position;
	public long last_activity_id;
	public String record_conversion_type; //
	public int record_conversion_id; //
	public Object metadata;
	public int[] watcher_ids;
	public boolean deleted;
	public String due_on;
	public Object first_comment;
	public Object recent_comments;
	
}
