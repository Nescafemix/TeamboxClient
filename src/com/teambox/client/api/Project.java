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

/**
 * Object which define the project info downloaded from Teambox with their API.
 * This class is used by Retrofit to define the destiny type of a parsed JSON
 * file.
 * 
 * @author Joan Fuentes
 * 
 */
public class Project {

	public String type;
	public String created_at;
	public String updated_at;
	public long id;
	public String permalink;
	public long organization_id;
	public boolean archived;
	public String name;
	public boolean tracks_time;
	public boolean publish_pages;
	public Object settings;
	public Object metadata;

}
