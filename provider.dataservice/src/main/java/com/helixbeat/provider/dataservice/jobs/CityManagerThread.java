package com.helixbeat.provider.dataservice.jobs;

import java.io.File;
import java.util.Iterator;

import com.helixbeat.provider.dataservice.AppConstants;
import com.helixbeat.provider.dataservice.location.City;

public class CityManagerThread extends ManagerThread {
	
	protected City city = null;
	
	public CityManagerThread(City city) {
		setCity(city);
		setManagedDir(city.getFile());
	}

	@Override
	public void process() {
		File rawDir = new File(getManagedDir().getAbsolutePath() + File.separator + "raw");
		if (!rawDir.exists()) {
			rawDir.mkdirs();
		}
		File sasDir = new File(getManagedDir().getAbsolutePath() + File.separator + "sas");
		if (!sasDir.exists()) {
			sasDir.mkdirs();
		}
		Iterator<String> zipCodeIterator = city.zipCodes();
		while (zipCodeIterator.hasNext()) {
			ProviderDataDownloadJob providerDataDownloadJob = new ProviderDataDownloadJob("https://p-hi2.digitaledge.cigna.com/ProviderDirectory/v1/Location?address-postalcode=" + zipCodeIterator.next(), rawDir.getAbsolutePath() + File.separator + AppConstants.PROVIDER_FILENAME_TEMPLATE.replace("$index","0"));
			providerDataDownloadJob.setAttribute("download.url", providerDataDownloadJob.getUrl());
			providerDataDownloadJob.setAttribute("download.save.path", providerDataDownloadJob.getPath());
			providerDataDownloadJob.start();
		}
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}