package com.helixbeat.provider.dataservice.jobs;

import com.helixbeat.provider.util.FileDownload;

public class FileDownloadJob extends AbstractJob {
	
	FileDownload download = null;
	
	protected String url = null;
	
	protected String path = null;
	
	public FileDownloadJob(String url, String path) {
		setUrl(url);
		setPath(path);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void execute() {
		String url = getAttribute("download.url");
		String path = getAttribute("download.save.path");
		FileDownload.downloadWithApacheCommons(url, path);
		System.out.println("FileDownloadJob.execute() Saved contents to " + path);
	}
}