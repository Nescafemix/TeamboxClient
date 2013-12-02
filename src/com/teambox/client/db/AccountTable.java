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
 * Persistent entity that would automatically map to a table named AccountTable
 * using SugarORM.
 * 
 * @author Joan Fuentes
 * 
 */
public class AccountTable extends SugarRecord<AccountTable> {
	public int accountId;
	public String firstName;
	public String lastName;
	public String email;
	public String username;
	public String profileAvatarUrl;
	public String profileAvatarUrlCached;
	public String profileAvatarUrlLocalFile;

	public AccountTable(Context context) {
		super(context);
	}

	public AccountTable(Context context, int accountId, String firstName,
			String lastName, String email, String username,
			String profileAvatarUrl) {
		super(context);
		this.accountId = accountId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.profileAvatarUrl = profileAvatarUrl;

	}

	public void update(int accountId, String firstName, String lastName,
			String email, String username, String profileAvatarUrl) {
		this.accountId = accountId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.profileAvatarUrl = profileAvatarUrl;
	}

}
