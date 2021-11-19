package com.helixbeat.provider.dataservice.jobs;

public interface IDownloadJobCallback {
	public void downloadStarted(DownloadEvent event);
	public void downloadInterrupted(DownloadEvent event);
	public void downloadPaused(DownloadEvent event);
	public void downloadResumed(DownloadEvent event);
	public void downloadComplete(DownloadEvent event);
}