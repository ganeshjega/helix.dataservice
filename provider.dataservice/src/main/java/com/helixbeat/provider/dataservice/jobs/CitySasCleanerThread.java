package com.helixbeat.provider.dataservice.jobs;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.helixbeat.provider.dataservice.AppConstants;
import com.helixbeat.provider.dataservice.location.City;
import com.helixbeat.provider.util.JSONUtils;
import com.helixbeat.provider.util.RestClient;
import com.helixbeat.provider.util.Utils;

public class CitySasCleanerThread extends ManagerThread {
	
	protected City city = null;
	
	public CitySasCleanerThread(City city) {
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
		
		File[] sasDirContents = sasDir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".json");
			}
		});
		
		if (sasDirContents == null || sasDirContents.length == 0) {
//			System.out.println("CitySasUploaderThread.process() " + rawDir.getAbsolutePath() + " Nothing to process.");
		} else {
			int count = sasDirContents.length;
			for (int i=0; i<sasDirContents.length;i++) {
				sasDirContents[i].delete();
			}
			System.out.println("CitySasConverterThread.process() Processed " + count + " files from " + sasDir.getParentFile().getAbsolutePath());
		}
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}