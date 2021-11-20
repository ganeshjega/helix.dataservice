package com.helixbeat.provider.dataservice;

public interface AppConstants {
	public static final String KEYWORD_KEY = "_keyword_";

	public static final String SECRET_KEY = "_secret-key_";

	public static final String SAS_API_URL = System.getProperty("sas.api.url", "http://localhost:8880/SigmaAPIServices/");
	
	public static final String SAS_PROCESS_TXN_URL = SAS_API_URL + "GenericTransactionService/processTransaction";
	
	public static final String SAS_BUILD_RESULTS_URL = SAS_API_URL + "GenericResultBuilderService/buildResults";

	public static final String DEFAULT_SECRET_KEY = "2bf52be7-9f68-4d52-9523-53f7f267153b";
	
	public static final String PROVIDER_DATA_DOWNLOAD_PATH = System.getProperty("provider.data.downloadPath", System.getProperty("java.io.tmpdir"));

	public static final String PROVIDER_FILENAME_TEMPLATE = "ProviderData_$index.json";
}