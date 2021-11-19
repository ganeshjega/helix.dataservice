package com.helixbeat.provider.dataservice.jobs.downloaderCallback;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.helixbeat.provider.dataservice.AppConstants;
import com.helixbeat.provider.dataservice.jobs.AbstractDownloaderCallback;
import com.helixbeat.provider.dataservice.jobs.DownloadEvent;
import com.helixbeat.provider.util.JSONUtils;
import com.helixbeat.provider.util.RestClient;

public class StateCityZipCodeValueSetDownloaderCallback extends AbstractDownloaderCallback {

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
		JSONObject templateObject = new JSONObject();
		templateObject.put(AppConstants.KEYWORD_KEY, "city");
		templateObject.put(AppConstants.SECRET_KEY, AppConstants.DEFAULT_SECRET_KEY);
		JSONArray dataArray = new JSONArray();
		for (Object key : inputObject.keySet()) {
			// this key is the state code
			String stateCode = (String) key;
			JSONObject stateObject = (JSONObject) inputObject.get(stateCode);
			String stateName = (String) stateObject.get("name");
			JSONObject citiesObject = (JSONObject) stateObject.get("cities");
			StringBuilder zipCodeBuilder = new StringBuilder();
			for (Object cityKey : citiesObject.keySet()) {
				String cityName = (String) cityKey;
				JSONArray zipCodeArray = (JSONArray) citiesObject.get(cityKey);
				JSONObject dataObject = new JSONObject();
				for (int i=0;i<zipCodeArray.size();i++) {
					zipCodeBuilder.append(String.valueOf(zipCodeArray.get(i)));
					if (i<zipCodeArray.size()-1) {
						zipCodeBuilder.append(",");
					}
				}
				dataObject.put("StateCode", stateCode);
				dataObject.put("StateName", stateName);
				dataObject.put("City", cityName);
				dataObject.put("ZipCodeList", zipCodeBuilder.toString());
				zipCodeBuilder.setLength(0);
				dataArray.add(dataObject);
				templateObject.remove("data");
				if (dataArray.size() > 1000) {
					templateObject.put("data", dataArray);
					RestClient.executePost(templateObject.toString(), AppConstants.SAS_PROCESS_TXN_URL);
					dataArray.clear();
				}
			}
		}
		if (dataArray.size() > 0) {
			templateObject.put("data", dataArray);
			RestClient.executePost(templateObject.toString(), AppConstants.SAS_PROCESS_TXN_URL);
			dataArray.clear();
		}
	}
}