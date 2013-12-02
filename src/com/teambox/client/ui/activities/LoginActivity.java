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

package com.teambox.client.ui.activities;

import java.io.IOException;
import java.util.concurrent.CancellationException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.api.client.auth.oauth2.Credential;
import com.teambox.client.Application;
import com.teambox.client.R;
import com.teambox.client.managers.OAuthGlobalManager;
import com.teambox.client.managers.OAuthTeamBoxManager;

/**
 * Login Screen
 * 
 * @author Joan Fuentes
 * 
 */
public class LoginActivity extends FragmentActivity {

	private class LoginManager extends AsyncTask<Void, Void, LoginResult> {
		protected LoginResult doInBackground(Void... urls) {
			LoginResult result = new LoginResult();
			OAuthGlobalManager oauth = OAuthTeamBoxManager.newInstance(
					getApplicationContext(), getSupportFragmentManager());
			try {
				Credential credential = oauth.authorizeExplicitly("userId",
						null, null).getResult();
				result.token = credential.getAccessToken();
				result.succesfully = Boolean.TRUE;
			} catch (CancellationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;
		}

		protected void onPostExecute(LoginResult result) {
			if (result.succesfully) {
				Application.setOAuthAccessToken(getApplicationContext(),
						result.token);

				loadSessionScreen();
			} else {
				Application.deleteCookies(getApplicationContext());
			}
		}

	}

	private class LoginResult {
		public String token;
		public boolean succesfully = Boolean.FALSE;
	}

	private boolean isUserLogged() {
		SharedPreferences sharedPreferences = getSharedPreferences(
				Application.APP_STORE_PREF_FILE, Application.MODE_PRIVATE);

		return sharedPreferences.contains(Application.TOKEN_PREF_KEY);
	}

	private void loadSessionScreen() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isUserLogged()) {
			loadSessionScreen();
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}

		setContentView(R.layout.activity_login);

		Button button = (Button) findViewById(R.id.buttonLogin);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new LoginManager().execute();

			}
		});
	}

}
