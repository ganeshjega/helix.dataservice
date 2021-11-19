package com.helixbeat.provider.util;

public interface FileSystemEventListener {

    public void fileCreated(String path);
	
	public void fileDeleted(String path);
	
	public void fileModified(String path);
}