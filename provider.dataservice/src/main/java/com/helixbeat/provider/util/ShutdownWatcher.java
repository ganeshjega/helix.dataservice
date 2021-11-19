package com.helixbeat.provider.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ShutdownWatcher extends Thread implements FileSystemEventListener {

	protected String fileName = null;
	
	public ShutdownWatcher(String fileName) {
		setFileName(fileName);
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void fileCreated(String path) {
		if (path.endsWith(fileName)) {
			System.out.println("ShutdownWatcher.fileCreated() [" + path + "]");
			new File(path).delete();
			System.exit(0);
		}
	}

	@Override
	public void fileDeleted(String arg0) {
	}

	@Override
	public void fileModified(String arg0) {
	}
	
	public void run() {
		try {
			WatchDir watchDir = new WatchDir(Paths.get(new File(".").toURI()), false);
			watchDir.setFileSystemEventListener(this);
			watchDir.processEvents();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}