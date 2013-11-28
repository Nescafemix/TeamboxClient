package com.teambox.client;

public class Application extends android.app.Application {
	private static Application application = null;

	public static String CREDENTIALS_STORE_PREF_FILE = "credentials_pref";
	public static String APP_STORE_PREF_FILE = "app_pref";
	public static String TOKEN_PREF_KEY = "oauth_token";
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
	}

	public static Application getInstance() {
		return application;
	}
	
}
