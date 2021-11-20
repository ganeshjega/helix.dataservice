package com.helixbeat.provider.dataservice.location;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;

public class StateofCountry {

	protected File baseDir = null;
	
	protected String name = null;
	
	public StateofCountry(File baseDir, String name) {
		setBaseDir(baseDir);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Iterator<File> cities() {
		return Arrays.asList(new File(baseDir, name).listFiles(new FileFilter() {
			@Override
			public boolean accept(File child) {
				return child.isDirectory();
			}
		})).iterator();
	}

	public File getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}
}