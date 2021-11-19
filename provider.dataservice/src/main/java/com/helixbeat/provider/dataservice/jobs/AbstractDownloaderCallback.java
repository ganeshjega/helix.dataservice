package com.helixbeat.provider.dataservice.jobs;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractDownloaderCallback implements IDownloadJobCallback {
	
	protected HashMap<String,String> attributes = new HashMap<String,String>();

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public void setAttribute(String name, String value) {
		attributes.put(name, value);
	}
	
	public String getAttribute(String name) {
		return attributes.get(name);
	}
	
	public Set<String> attributeNames() {
		return attributes.keySet();
	}
}