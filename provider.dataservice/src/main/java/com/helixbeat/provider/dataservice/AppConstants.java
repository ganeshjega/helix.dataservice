package com.helixbeat.provider.dataservice;

public interface AppConstants {
	public static final String KEYWORD_KEY = "_keyword_";

	public static final String SECRET_KEY = "_secret-key_";

	public static final String SAS_API_URL = System.getProperty("sas.api.url", "http://localhost:8880/SigmaAPIServices/");
	
	public static final String SAS_PROCESS_TXN_URL = SAS_API_URL + "GenericTransactionService/processTransaction";
	
	public static final String DEFAULT_SECRET_KEY = "2bf52be7-9f68-4d52-9523-53f7f267153b";
}