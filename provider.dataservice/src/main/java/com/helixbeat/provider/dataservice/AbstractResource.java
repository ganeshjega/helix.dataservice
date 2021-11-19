package com.helixbeat.provider.dataservice;

import java.io.File;

import com.helixbeat.provider.util.Utils;

public abstract class AbstractResource {

	public static final String TEMPLATE_PAYLOAD_DIR = System.getProperty("template.payload.dir",System.getProperty("java.io.tmpdir"));
	
	public String getResponse(String keyword) {
		return new String(Utils.getBytes(Utils.getResourceAsStream("/payloads/" + keyword + "-response.json")));
	}

	public String getResponseAbsolute(String keyword) {
		return new String(Utils.getBytes(TEMPLATE_PAYLOAD_DIR + File.separator + keyword + "-response.json"));
	}
}