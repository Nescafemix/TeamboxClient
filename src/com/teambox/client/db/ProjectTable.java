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
import com.orm.dsl.Ignore;

/**
 * Persistent entity that would automatically map to a table named ProjectTable
 * using SugarORM.
 * 
 * @author Joan Fuentes
 * 
 */
public class ProjectTable extends SugarRecord<ProjectTable> {

	public String type;
	public String createdAt;
	public String updatedAt;
	public long projectId;
	public String permalink;
	public long organizationId;
	public boolean archived;
	public String name;
	public boolean tracksTime;
	public boolean publishPages;

	@Ignore
	public int tasksCountNormal; // It won't be persisted, neither will a
									// corresponding column be created for this
									// property

	@Ignore
	public int tasksCountUrgent; // It won't be persisted, neither will a
									// corresponding column be created for this
									// property

	public ProjectTable(Context context) {
		super(context);
	}

	public ProjectTable(Context context, long projectId, String name) {
		super(context);
		this.projectId = projectId;
		this.name = name;
	}

	public ProjectTable(Context context, String type, String createdAt,
			String updatedAt, long projectId, String permalink,
			long organizationId, boolean archived, String name,
			boolean tracksTime, boolean publishPages) {
		super(context);
		this.type = type;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.projectId = projectId;
		this.permalink = permalink;
		this.organizationId = organizationId;
		this.archived = archived;
		this.name = name;
		this.tracksTime = tracksTime;
		this.publishPages = publishPages;

	}

}
