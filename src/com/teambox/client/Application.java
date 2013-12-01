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
package com.teambox.client;

import com.teambox.client.db.AccountTable;
import com.teambox.client.db.ProjectTable;
import com.teambox.client.db.TaskTable;
import com.teambox.client.oauth.OAuth;
import com.teambox.client.oauth.OAuthTeamBox;
import com.teambox.client.ui.activities.LoginActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class Application extends android.app.Application {
	private static Application application = null;

	public static final String CREDENTIALS_STORE_PREF_FILE = "credentials_pref";
	public static final String APP_STORE_PREF_FILE = "app_pref";

	public static final String TOKEN_PREF_KEY = "oauth_token";
	public static final String FILTER_TASK_STATUS_KEY = "filter_task_status";

	/**
	 * Delete Cookies
	 * 
	 * @param context
	 */
	public static void deleteCookies(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	/**
	 * Delete database used to store user data
	 * 
	 */
	private static void deleteDB() {
		AccountTable.deleteAll(AccountTable.class);
		ProjectTable.deleteAll(ProjectTable.class);
		TaskTable.deleteAll(TaskTable.class);
	}

	/**
	 * Delete OAuth access token from SharedPreferences
	 * 
	 * @param context
	 */
	private static void deleteOAuthAccessToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_STORE_PREF_FILE, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(TOKEN_PREF_KEY);
		editor.commit();
	}

	/**
	 * Delete OAuth Credentials
	 * 
	 * @param context
	 * @param fragmentManager
	 */
	private static void deleteOAuthCredential(Context context,
			FragmentManager fragmentManager) {
		OAuth oAuth = OAuthTeamBox.newInstance(context, fragmentManager);
		oAuth.deleteCredential("userId");
	}

	/**
	 * Get instance of Application
	 * 
	 * @return
	 */
	public static Application getInstance() {
		return application;
	}

	/**
	 * Get the OAuth access token from SharedPreferences
	 * 
	 * @param context
	 * @return String Access token
	 */
	public static String getOAuthAccessToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_STORE_PREF_FILE, MODE_PRIVATE);

		return preferences.getString(TOKEN_PREF_KEY, "0");
	}

	/**
	 * Get task status filter selection from SharedPreferences
	 * 
	 * @param context
	 * @return int position of selected value in the status array (first
	 *         element: 1)
	 */
	public static int getTaskStatusFilterSelection(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_STORE_PREF_FILE, MODE_PRIVATE);

		return preferences.getInt(FILTER_TASK_STATUS_KEY, 1);

	}

	/**
	 * Get task status filter value needed for api requests.
	 * 
	 * @param context
	 * @return
	 */
	public static long getTaskStatusFilterValue(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_STORE_PREF_FILE, MODE_PRIVATE);
		int positionStored = preferences.getInt(FILTER_TASK_STATUS_KEY, 1);

		return Long.valueOf(
				context.getResources().getStringArray(
						R.array.task_status_values_array)[positionStored - 1])
				.longValue();

	}

	/**
	 * Terminate user session. Delete cokies, oauth credentials & access token,
	 * DB and return the user to the Login screen
	 * 
	 * @param context
	 * @param fragmentManager
	 */
	public static void processLogout(Context context,
			FragmentManager fragmentManager) {

		deleteCookies(context);

		deleteOAuthAccessToken(context);

		deleteOAuthCredential(context, fragmentManager);

		deleteDB();

		returnToLoginActivity(context);
	}

	/**
	 * Return the user to the Login screen
	 * 
	 * @param context
	 */
	private static void returnToLoginActivity(Context context) {
		Intent intent;
		intent = new Intent(context, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * Set task status filter selection in SharedPreferences
	 * 
	 * @param context
	 * @param value
	 *            position of selected value in the status array (first
	 *            element:1)
	 */
	public static void setTaskStatusFilterSelection(Context context, int value) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_STORE_PREF_FILE, MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(FILTER_TASK_STATUS_KEY, value);
		editor.commit();

	}

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}

}
