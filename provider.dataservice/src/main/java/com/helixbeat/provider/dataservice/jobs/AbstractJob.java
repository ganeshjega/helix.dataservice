package com.helixbeat.provider.dataservice.jobs;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.helixbeat.provider.dataservice.jobs.DownloadEvent.DownloadEventStatus;

public abstract class AbstractJob extends Thread {

	protected String identity = null;

	protected Date start = null;

	protected Date end = null;

	protected boolean paused = false;

	protected boolean killed = false;
	
	protected HashMap<String,String> attributes = new HashMap<String,String>();
	
	protected IDownloadJobCallback callback = null;
	
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isKilled() {
		return killed;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
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
	
	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public IDownloadJobCallback getCallback() {
		return callback;
	}

	public void setCallback(IDownloadJobCallback callback) {
		this.callback = callback;
	}
	
	public abstract void execute();
	
	public void run() {
		if (callback != null) {
			callback.downloadStarted(new DownloadEvent(this).setMessage("Started download of " + attributes).setStatus(DownloadEventStatus.STARTED));
		}
		execute();
		if (callback != null) {
			callback.downloadComplete(new DownloadEvent(this).setMessage("Finished download of " + attributes).setStatus(DownloadEventStatus.COMPLETE));
		}
	}
}