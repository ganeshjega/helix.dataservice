package com.helixbeat.provider.dataservice.jobs;

import java.io.File;
import java.util.Iterator;

import com.helixbeat.provider.dataservice.location.City;
import com.helixbeat.provider.dataservice.location.StateofCountry;

public class StateSasCleanerThread extends ManagerThread {

	@Override
	public void process() {
		StateofCountry state = new StateofCountry(getManagedDir().getParentFile(), getManagedDir().getName());
		Iterator<File> cities = state.cities();
		while(cities.hasNext()) {
			File cityFile = cities.next();
			City city = new City(state, cityFile.getName());
			CitySasCleanerThread citySasCleanerThread = new CitySasCleanerThread(city);
			citySasCleanerThread.start();
			try {
				citySasCleanerThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}