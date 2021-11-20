package com.helixbeat.provider.dataservice.jobs;

import java.io.File;

public abstract class ManagerThread extends Thread {
	
	protected File managedDir = null;

	public File getManagedDir() {
		return managedDir;
	}

	public void setManagedDir(File managedDir) {
		this.managedDir = managedDir;
	}
	
	public void run() {
		process();
	}

	public abstract void process();
}