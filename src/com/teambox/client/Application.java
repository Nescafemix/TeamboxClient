package com.teambox.client;

public class Application extends android.app.Application {
	private static Application application = null;

	public static final String CREDENTIALS_STORE_PREF_FILE = "credentials_pref";
	public static final String APP_STORE_PREF_FILE = "app_pref";
	public static final String TOKEN_PREF_KEY = "oauth_token";
	public static final String ARG_FILTER_TASK_STATUS = "filter_task_status";

	
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}

	public static Application getInstance() {
		return application;
	}
	
}
