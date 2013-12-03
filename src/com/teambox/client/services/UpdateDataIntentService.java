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

package com.teambox.client.services;

import retrofit.RetrofitError;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.managers.DownloadDataManager;

/**
 * IntentService that launches the data update process and broadcast the result
 * with a LocalBroadcast
 * 
 * @author Joan Fuentes
 * 
 */
public class UpdateDataIntentService extends IntentService {

	public static final String NOTIFICATION_UPDATE_STATUS = "com.teambox.client.service.update_status_receiver";

	public static final String EXTRA_BROADCAST_TYPE = "com.teambox.client.broadcast.type";
	public static final String EXTRA_UPDATE_STATUS = "com.teambox.client.update_status";
	public static final String EXTRA_ERROR_MESSAGE = "com.teambox.client.error_message";

	public static final int STATUS_DOWNLOADING = 1;
	public static final int STATUS_COMPLETED = 2;
	public static final int STATUS_COMPLETED_WITH_ERRORS = 3;

	public static final int TYPE_UPLOAD_STATUS_CHANGE = 1;
	public static final int TYPE_ERROR_ALERT = 2;

	public UpdateDataIntentService() {
		super("UpdateDataService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		boolean success = Boolean.TRUE;

		DownloadDataManager updateManager = new DownloadDataManager(
				getApplicationContext(),
				Application.getOAuthAccessToken(getApplicationContext()));

		try {
			updateManager.updateAccount();
		} catch (RetrofitError e) {
			success = Boolean.FALSE;
			publishErrorAlert(getString(R.string.service_update_data_error_account));
		}

		try {
			updateManager.updateProjects();
		} catch (Exception e) {
			success = Boolean.FALSE;
			publishErrorAlert(getString(R.string.service_update_data_error_projects));
		}

		try {
			updateManager.updateMyAssignedTasks();
		} catch (Exception e) {
			success = Boolean.FALSE;
			publishErrorAlert(getString(R.string.service_update_data_error_tasks));
		}

		if (success) {
			publishUpdateCompleted();
		} else {
			publishUpdateCompletedWithErrors();
		}
	}

	/**
	 * Send a local broadcast to inform about an specific error in the update
	 * process.
	 * 
	 * @param message
	 */
	private void publishErrorAlert(String message) {
		Intent intent = new Intent(NOTIFICATION_UPDATE_STATUS);
		intent.putExtra(EXTRA_BROADCAST_TYPE, TYPE_ERROR_ALERT);
		intent.putExtra(EXTRA_ERROR_MESSAGE, message);

		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

	}

	/**
	 * Send a local broadcast to inform about an update successfully.
	 */
	private void publishUpdateCompleted() {
		Intent intent = new Intent(NOTIFICATION_UPDATE_STATUS);
		intent.putExtra(EXTRA_BROADCAST_TYPE, TYPE_UPLOAD_STATUS_CHANGE);
		intent.putExtra(EXTRA_UPDATE_STATUS, STATUS_COMPLETED);

		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	/**
	 * Send a local broadcast to inform about an update unsuccessfully. It
	 * contained errors.
	 */
	private void publishUpdateCompletedWithErrors() {
		Intent intent = new Intent(NOTIFICATION_UPDATE_STATUS);
		intent.putExtra(EXTRA_BROADCAST_TYPE, TYPE_UPLOAD_STATUS_CHANGE);
		intent.putExtra(EXTRA_UPDATE_STATUS, STATUS_COMPLETED_WITH_ERRORS);

		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

}
