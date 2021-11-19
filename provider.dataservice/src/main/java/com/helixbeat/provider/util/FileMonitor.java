package com.helixbeat.provider.util;

import java.io.*;

public class FileMonitor implements FileSystemEventListener {

    public void fileCreated(String path) {
	    System.out.println("[FileMonitor] -------- File created : " + path);
		File file = new File(path);
		if (file.getName().endsWith(".exit")) {
			file.delete();
			System.exit(0);
		}
	}
	
	public void fileDeleted(String path) {}
	
	public void fileModified(String path) {}
}