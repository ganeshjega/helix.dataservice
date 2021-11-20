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

public class CitySasUploaderThread extends ManagerThread {
	
	protected City city = null;
	
	public CitySasUploaderThread(City city) {
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
			for (int i=0; i<sasDirContents.length;i++) {
				JSONObject inputObject = (JSONObject) JSONUtils.parseObjectFromFileSystem(sasDirContents[i].getAbsolutePath());
				RestClient.executePost(inputObject.toString(), AppConstants.SAS_PROCESS_TXN_URL , new HashMap<String,String>());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("CitySasConverterThread.process() Processed " + sasDirContents.length + " files from " + sasDir.getParentFile().getAbsolutePath());
		}
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
}