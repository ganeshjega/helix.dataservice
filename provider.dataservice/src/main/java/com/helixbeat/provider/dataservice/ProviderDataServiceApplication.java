package com.helixbeat.provider.dataservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Application should browse through database and collect all scheduled download jobs for the day
 * For each record
 * 		Create a FileDownloadJob object for each job record
 * 		Create a Callback object that parses the file and pushes the data into Database using RestClient.executePost()
 * 		Attach the Callback object to FileDownloadJob
 * 		Start the FileDownloadJob Thread
 * 		Call Thread.join() so that it gets killed automatically as soon as the execution ends
 * @author ganesh jegadheesan
 *
 */

@SpringBootApplication
public class ProviderDataServiceApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		new ProviderDataServiceApplication()
				.configure(new SpringApplicationBuilder(ProviderDataServiceApplication.class)).run(args);
	}
}