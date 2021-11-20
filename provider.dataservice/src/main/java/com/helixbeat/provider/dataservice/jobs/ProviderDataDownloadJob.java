package com.helixbeat.provider.dataservice.jobs;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.helixbeat.provider.util.JSONUtils;

public class ProviderDataDownloadJob extends RelayDownloadJob {

	protected String zipCode = null;
	
	public ProviderDataDownloadJob(String url, String path) {
		super(url, path);
	}

	@Override
	public void processBody(File downloadedFile) {
		JSONObject providerObject = JSONUtils.parseObjectFromFileSystem(downloadedFile.getAbsolutePath());
		JSONArray linkArray = (JSONArray) providerObject.get("link");
		boolean nextExists = false;
		if (linkArray != null) {
			for (int i=0;i<linkArray.size();i++) {
				JSONObject linkObject = (JSONObject) linkArray.get(i);
				if ("next".equals(linkObject.get("relation"))) {
					nextExists = true;
					currentIndex++;
					setNextUrl((String) linkObject.get("url"));
					setNextPath(downloadedFile.getParentFile().getAbsolutePath() + File.separator + PROVIDER_FILENAME_TEMPLATE.replace("$index", String.valueOf(currentIndex)));
					System.out.println("ProviderDataDownloadJob.processBody() " + getNextUrl() + " to be saved to " + getNextPath());
					break;
				}
			}
		}
		if (!nextExists) {
			setNextPath(null);
			setNextUrl(null);
		}
//		System.out.println("ProviderDataDownloadJob.processBody() " + providerObject);
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}