package com.helixbeat.provider.dataservice.location;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.helixbeat.provider.util.Utils;

public class City {
	
	protected StateofCountry state = null;
	
	protected String name = null;
	
	protected File file = null;
	
	protected ArrayList<String> zipCodeList = new ArrayList<String>();
	
	public City(StateofCountry state, String name) {
		setState(state);
		setName(name);
		setFile(new File(state.getBaseDir().getAbsolutePath() + File.separator + state.getName() + File.separator + getName()));
		prepZipCodeList();
	}

	private void prepZipCodeList() {
		String zipCodeListStr = new String(Utils.getBytes(getFile().getAbsolutePath() + File.separator + "ZipCodeList.txt"));
		zipCodeListStr = zipCodeListStr.trim();
		zipCodeList.addAll(Arrays.asList(zipCodeListStr.split(",")));
	}

	public StateofCountry getState() {
		return state;
	}

	public void setState(StateofCountry state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getZipCodeList() {
		return zipCodeList;
	}

	public void setZipCodeList(ArrayList<String> zipCodeList) {
		this.zipCodeList = zipCodeList;
	}

	public Iterator<String> zipCodes() {
		return zipCodeList.iterator();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}