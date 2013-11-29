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

import java.util.Arrays;
import java.util.List;

public class ServiceInfo {

	public static final String CLIENT_ID="MDfMUJAJCH7wCR4LTnEmIGbVh8o7r9q3FS4AfUTQ";
	public static final String CLIENT_SECRET="C34LuKYGyblsWcOCstpZzhYRuOCUtopRwjHCgv5G";
	
	public static final String TOKEN_REQUEST_URL="https://teambox.com/oauth/token";
	public static final String AUTHORIZATION_REQUEST_URL="https://teambox.com/oauth/authorize";

	public static final String REDIRECT_URL="http://www.teamboxunofficial.com/auth";
	public static final List<String> SCOPES= Arrays.asList("read_projects", "write_projects");

	public static final String API_URL = "https://www.teambox.com/api/2";
}
