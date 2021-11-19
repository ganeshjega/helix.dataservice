package com.helixbeat.provider.dataservice.jobs;

import java.util.EventObject;
import java.util.Map;

public class DownloadEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	protected DownloadEventStatus status = DownloadEventStatus.YET_TO_START;
	
	protected String message = null;
	
	public DownloadEvent(Object source) {
		super(source);
	}
	
	public enum DownloadEventStatus {
		YET_TO_START, STARTED, COMPLETE, INTERRUPTED, PAUSED, RESUMED
	}

	public DownloadEventStatus getStatus() {
		return status;
	}

	public DownloadEvent setStatus(DownloadEventStatus status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public DownloadEvent setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public Map<String,String> attributes() {
		return ((AbstractJob) source).getAttributes();
	}
}