package com.helixbeat.provider.dataservice.jobs;

import java.io.File;

import com.helixbeat.provider.util.FileDownload;
import com.helixbeat.provider.util.Utils;

public abstract class RelayDownloadJob extends AbstractJob {
	
	FileDownload download = null;
	
	protected String url = null;
	
	protected String path = null;
	
	protected String nextUrl = null;
	
	protected String nextPath = null;
	
	public RelayDownloadJob(String url, String path) {
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

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public String getNextPath() {
		return nextPath;
	}

	public void setNextPath(String nextPath) {
		this.nextPath = nextPath;
	}

	@Override
	public void execute() {
		String url = getAttribute("download.url");
		String path = getAttribute("download.save.path");
		FileDownload.downloadWithApacheCommons(url, path);
		processBody(new File(path));
		System.out.println("FileDownloadJob.execute() Saved contents to " + path);
		while (true) {
			if (getNextUrl() != null) {
				try {
					Thread.sleep(Utils.getLong(System.getProperty("thread.sleepTime","3000")));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				File file = next();
				processBody(file);
			} else {
				break;
			}
		}
	}
	
	public File next() {
		FileDownload.downloadWithApacheCommons(getNextUrl(), getNextPath());
		System.out.println("RelayDownloadJob.next() Saving contents to " + nextPath);
		return new File(nextPath);
	}
	
	public abstract void processBody(File downloadedFile);
}