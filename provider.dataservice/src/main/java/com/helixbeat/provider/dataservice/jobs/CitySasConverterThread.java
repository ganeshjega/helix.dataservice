package com.helixbeat.provider.dataservice.jobs;

import java.io.File;
import java.io.FileFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.helixbeat.provider.dataservice.location.City;
import com.helixbeat.provider.util.JSONUtils;
import com.helixbeat.provider.util.Utils;

public class CitySasConverterThread extends ManagerThread {
	
	protected City city = null;
	
	public CitySasConverterThread(City city) {
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
		
		File[] rawDirContents = rawDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".json");
			}
		});
		
		if (rawDirContents == null || rawDirContents.length == 0) {
//			System.out.println("CitySasConverterThread.process() " + rawDir.getAbsolutePath() + " Nothing to process.");
		} else {
			int count = 0;
			for (int i=0; i<rawDirContents.length;i++) {
				JSONObject inputObject = (JSONObject) JSONUtils.parseObjectFromFileSystem(rawDirContents[i].getAbsolutePath());
				ProviderDataObject providerDataObject = new ProviderDataObject(inputObject);
				JSONObject targetObject = providerDataObject.toSasFormat();
				JSONArray dataArray = (JSONArray) targetObject.get("data");
				if (dataArray != null && dataArray.size() > 0) {
					Utils.putBytes(targetObject.toString().getBytes(), sasDir.getAbsolutePath() + File.separator + rawDirContents[i].getName());
					count++;
				}
			}
			System.out.println("CitySasConverterThread.process() Processed " + count + " files from " + rawDir.getParentFile().getAbsolutePath());
		}
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}