package com.helixbeat.provider.dataservice.jobs.downloaderCallback;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.helixbeat.provider.dataservice.AppConstants;
import com.helixbeat.provider.dataservice.jobs.AbstractDownloaderCallback;
import com.helixbeat.provider.dataservice.jobs.DownloadEvent;
import com.helixbeat.provider.util.JSONUtils;
import com.helixbeat.provider.util.RestClient;

public class LanguageValueSetDownloaderCallback extends AbstractDownloaderCallback {

	@Override
	public void downloadStarted(DownloadEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadResumed(DownloadEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadPaused(DownloadEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInterrupted(DownloadEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadComplete(DownloadEvent event) {
		JSONObject inputObject = JSONUtils.parseObjectFromFileSystem(event.attributes().get("download.save.path"));
		JSONObject composeObject = (JSONObject) inputObject.get("compose");
		JSONArray includeArray = (JSONArray) composeObject.get("include");
		JSONArray conceptArray = (JSONArray) ((JSONObject) includeArray.get(0)).get("concept");
		JSONObject templateObject = new JSONObject();
		templateObject.put(AppConstants.KEYWORD_KEY, "valueset");
		templateObject.put(AppConstants.SECRET_KEY, AppConstants.DEFAULT_SECRET_KEY);
		for (int i = 0; i < conceptArray.size(); i++) {
			JSONArray dataArray = new JSONArray();
			JSONObject dataObject = new JSONObject();
			dataObject.put("CategoryCode", "LANGUAGE");
			dataObject.put("Code", ((JSONObject) conceptArray.get(i)).get("code"));
			dataObject.put("Display", ((JSONObject) conceptArray.get(i)).get("display"));
			dataArray.add(dataObject);
			templateObject.remove("data");
			templateObject.put("data", dataArray);
			RestClient.executePost(templateObject.toString(), AppConstants.SAS_PROCESS_TXN_URL);
		}
	}
}