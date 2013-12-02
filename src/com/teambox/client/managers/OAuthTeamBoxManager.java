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

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.teambox.client.api.Configuration;

/**
 * Manager OAuth for Teambox API. It is used to manage the OAuth request to
 * Teambox services.
 * 
 * @author Joan Fuentes
 * 
 */
public class OAuthTeamBoxManager {

	public static OAuthGlobalManager newInstance(Context context,
			FragmentManager fragmentManager) {

		return OAuthGlobalManager.newInstance(context, fragmentManager,
				new ClientParametersAuthentication(Configuration.CLIENT_ID,
						Configuration.CLIENT_SECRET),
				Configuration.AUTHORIZATION_REQUEST_URL,
				Configuration.TOKEN_REQUEST_URL, Configuration.REDIRECT_URL,
				Configuration.SCOPES);

	}
}