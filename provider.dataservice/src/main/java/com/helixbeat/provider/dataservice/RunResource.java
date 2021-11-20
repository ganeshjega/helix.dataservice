package com.helixbeat.provider.dataservice;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.boot.json.GsonJsonParser;

import com.google.gson.JsonObject;
import com.helixbeat.provider.dataservice.jobs.DownloadEvent;
import com.helixbeat.provider.dataservice.jobs.FileDownloadJob;
import com.helixbeat.provider.dataservice.jobs.IDownloadJobCallback;
import com.helixbeat.provider.dataservice.jobs.StateManagerThread;
import com.helixbeat.provider.dataservice.jobs.StateSasCleanerThread;
import com.helixbeat.provider.dataservice.jobs.StateSasConverterThread;
import com.helixbeat.provider.dataservice.jobs.StateSasUploaderThread;
import com.helixbeat.provider.dataservice.jobs.downloaderCallback.LanguageValueSetDownloaderCallback;
import com.helixbeat.provider.dataservice.jobs.downloaderCallback.StateCityZipCodeValueSetDownloaderCallback;
import com.helixbeat.provider.util.JSONUtils;
import com.helixbeat.provider.util.RestClient;
import com.helixbeat.provider.util.Utils;
 
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "run")
@Path("/run")
public class RunResource extends AbstractResource
{
    private static Map<Integer, Run> RUN_DB = new HashMap<>(); 
     
    @GET
    @Produces("application/json")
    @Path("/all")
    public Runs getAllRuns() {
        Runs runs = new Runs();
        runs.setRuns(new ArrayList<>(RUN_DB.values()));
        return runs;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/syncValueSet")
    public Response syncValueSet(String str) throws URISyntaxException 
    {
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);
    	System.out.println("RunResource.createOld() " + data);

        String runUUID = UUID.randomUUID().toString();
        
        String fileName = System.currentTimeMillis() + "_" + (String) data.get("download.save.filename"); 
        
        String path = System.getProperty("java.io.tmpdir") + fileName;
        String url = (String) data.get("download.url");
        
        FileDownloadJob downloadJob = new FileDownloadJob(url, path);
        downloadJob.setAttribute("download.url", url);
        downloadJob.setAttribute("download.save.path", path);
        if ("language".equalsIgnoreCase((String) data.get("type"))) {
            downloadJob.setCallback(new LanguageValueSetDownloaderCallback());
        } else if ("city".equalsIgnoreCase((String) data.get("type"))) {
        	downloadJob.setCallback(new StateCityZipCodeValueSetDownloaderCallback());
        }
        downloadJob.start();
        try {
			downloadJob.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        JsonObject output = new JsonObject();
        output.addProperty("run.identifier", runUUID);
        output.addProperty("success", "true");
        return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/syncProviderData")
    public Response syncProviderData(String str) throws URISyntaxException {
        String runUUID = UUID.randomUUID().toString();
    	File downloadPath = new File(AppConstants.PROVIDER_DATA_DOWNLOAD_PATH + File.separator + runUUID);
    	if (!downloadPath.exists()) {
    		downloadPath.mkdirs();
    	}
    	
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);
    	System.out.println("RunResource.createOld() " + data);
        
        RestClient.executePostAndDownload(JSONUtils.buildFromStringArray(new String[] { AppConstants.KEYWORD_KEY, "city" }).toString(), AppConstants.SAS_BUILD_RESULTS_URL, downloadPath.getAbsolutePath() + File.separator + "cities.json");
        
        JSONObject cityData = JSONUtils.parseObjectFromFileSystem(downloadPath.getAbsolutePath() + File.separator + "cities.json");
        JSONArray dataArray = (JSONArray) cityData.get("data");
        for (int i=0;i<dataArray.size();i++) {
        	JSONObject cityObject = (JSONObject) dataArray.get(i);
        	File dir = new File(downloadPath + File.separator + cityObject.get("StateCode") + File.separator + cityObject.get("CityName"));
        	if (!dir.exists()) {
        		dir.mkdirs();
        	}
        	Utils.putBytes(((String) cityObject.get("ZipCodeList")).getBytes(), dir.getAbsolutePath() + File.separator + "zipcodelist.txt");
        	System.out.println("RunResource.syncProviderData() Done with city " + cityObject.get("CityName"));
        }
        
        JsonObject output = new JsonObject();
        output.addProperty("run.identifier", runUUID);
        output.addProperty("success", "true");
        return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/prepareProviderData")
    public Response prepareProviderData(String str) throws URISyntaxException {
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);

    	System.out.println("RunResource.createOld() " + data);
        
    	String runUUID = UUID.randomUUID().toString();
    	
    	File downloadPath = new File(AppConstants.PROVIDER_DATA_DOWNLOAD_PATH + File.separator + "all-cities");
    	if (downloadPath.exists()) {
    		File[] stateDirList = downloadPath.listFiles(new FileFilter() {
				@Override
				public boolean accept(File stateDir) {
					return stateDir.isDirectory();
				}
			});
    		if (stateDirList != null && stateDirList.length > 0) {
    			for (File stateDir : stateDirList) {
    				StateManagerThread stateManagerThread = new StateManagerThread();
    				stateManagerThread.setManagedDir(stateDir);
    				stateManagerThread.start();
    				try {
						stateManagerThread.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}
    	}
        
        JsonObject output = new JsonObject();
        output.addProperty("run.identifier", runUUID);
        output.addProperty("success", "true");
        return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/convertProviderDataFromRawToSas")
    public Response convertProviderDataFromRawToSas(String str) throws URISyntaxException {
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);

    	System.out.println("RunResource.createOld() " + data);
        
    	String runUUID = UUID.randomUUID().toString();
    	
    	File downloadPath = new File(AppConstants.PROVIDER_DATA_DOWNLOAD_PATH + File.separator + "all-cities");
    	if (downloadPath.exists()) {
    		File[] stateDirList = downloadPath.listFiles(new FileFilter() {
				@Override
				public boolean accept(File stateDir) {
					return stateDir.isDirectory();
				}
			});
    		if (stateDirList != null && stateDirList.length > 0) {
    			for (File stateDir : stateDirList) {
    				StateSasConverterThread stateSasConverterThread = new StateSasConverterThread();
    				stateSasConverterThread.setManagedDir(stateDir);
    				stateSasConverterThread.start();
    			}
    		}
    	}
        
        JsonObject output = new JsonObject();
        output.addProperty("run.identifier", runUUID);
        output.addProperty("success", "true");
        return Response.status(200).entity(output.toString()).build();
    }
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/uploadProviderDataFromSasToDatabase")
    public Response uploadProviderDataFromSasToDatabase(String str) throws URISyntaxException {
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);

    	System.out.println("RunResource.createOld() " + data);
        
    	String runUUID = UUID.randomUUID().toString();
    	
    	File downloadPath = new File(AppConstants.PROVIDER_DATA_DOWNLOAD_PATH + File.separator + "all-cities");
    	if (downloadPath.exists()) {
    		File[] stateDirList = downloadPath.listFiles(new FileFilter() {
				@Override
				public boolean accept(File stateDir) {
					return stateDir.isDirectory();
				}
			});
    		if (stateDirList != null && stateDirList.length > 0) {
    			for (File stateDir : stateDirList) {
    				StateSasUploaderThread stateSasUploaderThread = new StateSasUploaderThread();
    				stateSasUploaderThread.setManagedDir(stateDir);
    				stateSasUploaderThread.start();
    			}
    		}
    	}
        
        JsonObject output = new JsonObject();
        output.addProperty("run.identifier", runUUID);
        output.addProperty("success", "true");
        return Response.status(200).entity(output.toString()).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/cleanProviderDataFromSas")
    public Response cleanProviderDataFromSas(String str) throws URISyntaxException {
    	GsonJsonParser parser = new GsonJsonParser();
    	Map data = parser.parseMap(str);

    	System.out.println("RunResource.createOld() " + data);
        
    	String runUUID = UUID.randomUUID().toString();
    	
    	File downloadPath = new File(AppConstants.PROVIDER_DATA_DOWNLOAD_PATH + File.separator + "all-cities");
    	if (downloadPath.exists()) {
    		File[] stateDirList = downloadPath.listFiles(new FileFilter() {
				@Override
				public boolean accept(File stateDir) {
					return stateDir.isDirectory();
				}
			});
    		if (stateDirList != null && stateDirList.length > 0) {
    			for (File stateDir : stateDirList) {
    				StateSasCleanerThread stateSasCleanerThread = new StateSasCleanerThread();
    				stateSasCleanerThread.setManagedDir(stateDir);
    				stateSasCleanerThread.start();
    			}
    		}
    	}
        
        JsonObject output = new JsonObject();
        output.addProperty("run.identifier", runUUID);
        output.addProperty("success", "true");
        return Response.status(200).entity(output.toString()).build();
    }
    
    @POST
    @Consumes("application/json")
    public Response createRun(Run run) throws URISyntaxException 
    {
        if(run.getName() == null) {
            return Response.status(400).entity("Please provide all mandatory inputs").build();
        }
        run.setId(RUN_DB.values().size()+1);
        RUN_DB.put(run.getId(), run);
        return Response.status(201).contentLocation(new URI(run.getName())).build();
    }
 
    @GET
    @Path("/{id}")
    @Produces("application/json")
    public Response getRunByWorkspaceId(@PathParam("id") int id) throws URISyntaxException 
    {
    	
        String template = getResponse("run-get");
        template = template.replaceAll("[$]ORG_TS", new Timestamp(System.currentTimeMillis()).toString());
        template = template.replaceAll("[$]WS_TS", new Timestamp(System.currentTimeMillis()).toString());
        template = template.replace("$UUID", UUID.randomUUID().toString());
        template = template.replace("$WS_ID", Integer.toString(id));
        template = template.replace("$ORG_ID", Integer.toString(5));
    	
        return Response
                .status(200)
                .entity(template)
                .contentLocation(new URI("/runs/"+id)).build();
    }
 
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateRun(@PathParam("id") int id, Run run) throws URISyntaxException 
    {
        Run temp = RUN_DB.get(id);
        if (run == null) {
            return Response.status(404).build();
        }
        temp.setName(run.getName());
        RUN_DB.put(temp.getId(), temp);
        return Response.status(200).entity(temp).build();
    }
 
    @DELETE
    @Path("/{id}")
    public Response deleteRun(@PathParam("id") int id) throws URISyntaxException {
        Run run = RUN_DB.get(id);
        if (run != null) {
            RUN_DB.remove(run.getId());
            return Response.status(200).build();
        }
        return Response.status(404).build();
    }
     
    static
    {
        Run r1 = new Run();
        r1.setId(1);
        r1.setName("Apple");
 
        Run r2 = new Run();
        r2.setId(2);
        r2.setName("IBM");
         
        RUN_DB.put(r1.getId(), r1);
        RUN_DB.put(r2.getId(), r2);
    }
}