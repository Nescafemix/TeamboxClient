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

package com.teambox.client.db;

import android.content.Context;

import com.orm.SugarRecord;

/**
 * Persistent entity that would automatically map to a table named TaskTable
 * using SugarORM.
 * 
 * @author Joan Fuentes
 * 
 */
public class TaskTable extends SugarRecord<TaskTable> {

	public String type;
	public String createdAt;
	public String updatedAt;
	public long id;
	public String name;
	public long taskListId;
	public int commentsCount;
	public long assignedId; //
	public int status;
	public boolean isPrivate;
	public long projectId;
	public boolean urgent;
	public int hiddenCommentsCount;
	public long user_id;
	public int position;
	public long lastActivityId;
	public boolean deleted;
	public String dueOn;

	public TaskTable(Context context) {
		super(context);
	}

	public TaskTable(Context context, String type, String createdAt,
			String updatedAt, long id, String name, long taskListId,
			int commentsCount, long assignedId, int status, boolean isPrivate,
			long projectId, boolean urgent, int hiddenCommentsCount,
			long user_id, int position, long lastActivityId, boolean deleted,
			String dueOn) {
		super(context);

		this.type = type;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.id = id;
		this.name = name;
		this.position = position;
		this.projectId = projectId;
		this.taskListId = taskListId;
		this.commentsCount = commentsCount;
		this.assignedId = assignedId; //
		this.status = status;
		this.user_id = user_id;
		this.deleted = deleted;
		this.isPrivate = isPrivate;
		this.urgent = urgent;
		this.hiddenCommentsCount = hiddenCommentsCount;
		this.lastActivityId = lastActivityId;
		this.dueOn = dueOn;
	}

}
