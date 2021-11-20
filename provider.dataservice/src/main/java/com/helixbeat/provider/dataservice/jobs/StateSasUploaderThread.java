package com.helixbeat.provider.dataservice.jobs;

import java.io.File;
import java.util.Iterator;

import com.helixbeat.provider.dataservice.location.City;
import com.helixbeat.provider.dataservice.location.StateofCountry;

public class StateSasUploaderThread extends ManagerThread {

	@Override
	public void process() {
		StateofCountry state = new StateofCountry(getManagedDir().getParentFile(), getManagedDir().getName());
		Iterator<File> cities = state.cities();
		while(cities.hasNext()) {
			File cityFile = cities.next();
			City city = new City(state, cityFile.getName());
			CitySasUploaderThread citySasUploaderThread = new CitySasUploaderThread(city);
			citySasUploaderThread.start();
			try {
				citySasUploaderThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}